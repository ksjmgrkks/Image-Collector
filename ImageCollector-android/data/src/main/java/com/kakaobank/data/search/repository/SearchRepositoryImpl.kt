package com.kakaobank.data.search.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kakaobank.core.data.SearchItem
import com.kakaobank.core.util.Constants
import com.kakaobank.core.util.IoDispatcher
import com.kakaobank.data.search.remote.model.ApiMapper
import com.kakaobank.data.search.remote.model.PagingModel
import com.kakaobank.data.search.remote.paging.SearchItemPagingSource
import com.kakaobank.data.search.remote.service.SearchService
import com.kakaobank.domain.search.repository.SearchRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val service: SearchService,
    private val apiMapper: ApiMapper,
): SearchRepository {

    /**
     * [query]를 바탕으로 첫번째 페이징 데이터를 생성하는 함수입니다.
     * @return FirstSearchImagesUseCase에서 사용할 Flow<PagingData<SearchItem>>
     */
    override suspend fun firstSearchImages(query: String): Flow<PagingData<SearchItem>> {
        val pagingData = firstMergeAndSortImagesWithVideos(
            service = service,
            apiMapper = apiMapper,
            query = query
        )

        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                initialLoadSize = Constants.PAGE_SIZE,
                pageSize = Constants.PAGE_SIZE
            ),
            pagingSourceFactory = { SearchItemPagingSource(pagingData) }
        ).flow
    }

    /**
     * [query]를 바탕으로 전체 페이징 데이터를 생성하는 함수입니다.
     * @return SearchImagesUseCase에서 사용할 Flow<PagingData<SearchItem>>
     */
    override suspend fun searchImages(query: String): Flow<PagingData<SearchItem>> {
        val pagingData = mergeAndSortImagesWithVideos(
            service = service,
            apiMapper = apiMapper,
            query = query
        )

        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                initialLoadSize = Constants.PAGE_SIZE,
                pageSize = Constants.PAGE_SIZE
            ),
            pagingSourceFactory = { SearchItemPagingSource(pagingData) }
        ).flow
    }

    /**
     * query로 검색할 수 있는 첫번째 페이지에 대한 데이터를 수집합니다.
     * 최신순(recency)으로 호출했기 때문에 첫번째 페이지가 가장 최신 데이터입니다.
     * @return 페이징 소스를 만드는데 필요한 정보를 담은 PagingModel
     */
    private suspend fun firstMergeAndSortImagesWithVideos(
        service: SearchService,
        apiMapper: ApiMapper,
        query: String
    ): PagingModel {
        try {
            val pagingModel = coroutineScope {
                /* async-await을 활용함으로써 값을 반환하는 병렬 처리 수행 */
                val imageDeferred = async {
                    service.searchImages(
                        sort = Constants.RECENCY_PARAM,
                        query = query,
                        page = 1,
                        size = Constants.IMAGE_API_SIZE_MAX
                    )
                }
                val videoDeferred = async {
                    service.searchVideos(
                        sort = Constants.RECENCY_PARAM,
                        query = query,
                        page = 1,
                        size = Constants.VIDEO_API_SIZE_MAX
                    )
                }
                val imageResponse = imageDeferred.await() /* imageResponse 올 때까지 기다림 */
                val videoResponse = videoDeferred.await() /* videoResponse 올 때까지 기다림 */

                /* 2개의 Response를 다 받은 후 로직 수행 */
                val mergedAndSortedList =
                    imageResponse.documents.map { apiMapper.imageToSearchItem(it) } /* imageResponse to SearchItemList */
                        .plus(videoResponse.documents.map { apiMapper.videoToSearchItem(it) }) /* videoResponse to SearchItemList + 두 리스트 합치기 */
                        .sortedByDescending { /* 최신순으로 정렬 */
                            OffsetDateTime.parse(
                                it.datetime,
                                DateTimeFormatter.ISO_OFFSET_DATE_TIME
                            )
                        }
                return@coroutineScope PagingModel(
                    searchItemList = mergedAndSortedList,
                    totalSize = mergedAndSortedList.size,
                    exception = null
                )
            }
            return pagingModel
        } catch (e: Exception) {
            return PagingModel(
                searchItemList = listOf(),
                totalSize = 0,
                exception = e
            )
        }
    }

    /**
     * query로 검색할 수 있는 모든 데이터를 수집합니다.
     * @return 페이징 소스를 만드는데 필요한 정보를 담은 PagingModel
     */
    private suspend fun mergeAndSortImagesWithVideos(service: SearchService,
                                              apiMapper: ApiMapper,
                                              query: String) : PagingModel {

        try {
            val pagingModel = coroutineScope {
                /* async-await을 활용함으로써 값을 반환하는 병렬 처리 수행 */
                val imageDeferred = async {
                    val imageList = mutableListOf<SearchItem>()
                    var imagePage = 1
                    var imageIsEnd = false
                    /* 이미지 검색 API에서 호출할 수 있는 최대 데이터까지 부르거나 마지막 페이지일때 까지 api 호출하기 */
                    while( imagePage <= Constants.IMAGE_API_PAGE_MAX && !imageIsEnd ){
                        val imageResponse = service.searchImages(
                            sort = Constants.RECENCY_PARAM,
                            query = query,
                            page = imagePage,
                            size = Constants.IMAGE_API_SIZE_MAX)
                        for(item in imageResponse.documents.map { apiMapper.imageToSearchItem(it) }){
                            imageList.add(item)
                        }
                        imageIsEnd = imageResponse.meta.isEnd
                        imagePage++
                    }
                    imageList/* 리턴 값 */
                }

                val videoDeferred = async {
                    val videoList = mutableListOf<SearchItem>()
                    var videoPage = 1
                    var videoIsEnd = false
                    /* 동영상 검색 API에서 호출할 수 있는 최대 데이터까지 부르거나 마지막 페이지일때 까지 api 호출하기 */
                    while( videoPage <= Constants.VIDEO_API_PAGE_MAX && !videoIsEnd ){
                        val videoResponse = service.searchVideos(
                            sort = Constants.RECENCY_PARAM,
                            query = query,
                            page = videoPage,
                            size = Constants.VIDEO_API_SIZE_MAX)
                        for(item in videoResponse.documents.map { apiMapper.videoToSearchItem(it) }){
                            videoList.add(item)
                        }
                        videoIsEnd = videoResponse.meta.isEnd
                        videoPage++
                    }
                    videoList/* 리턴 값 */
                }

                val totalImageList = imageDeferred.await()
                val totalVideoList = videoDeferred.await()

                /* 2개의 Response를 다 받은 후 로직 수행 */
                val mergedAndSortedList =
                    totalImageList.plus(totalVideoList) /* 두 리스트 합치기 */
                        .sortedByDescending { /* 최신순으로 정렬 */
                            OffsetDateTime.parse(
                                it.datetime,
                                DateTimeFormatter.ISO_OFFSET_DATE_TIME
                            )
                        }
                return@coroutineScope PagingModel(
                    searchItemList = mergedAndSortedList,
                    totalSize = mergedAndSortedList.size,
                    exception = null
                )
            }
            return pagingModel
        } catch(e: Exception){
            return PagingModel(
                searchItemList = listOf(),
                totalSize = 0,
                exception = e
            )
        }
    }
}