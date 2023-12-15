package com.kks.data.search.remote.paging


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kks.core.data.SearchItem
import com.kks.core.util.Constants
import com.kks.data.search.remote.model.PagingModel


private const val SEARCH_STARTING_PAGE_INDEX = 1

class SearchItemPagingSource(
    private val pagingData: PagingModel
) : PagingSource<Int, SearchItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchItem> {
        pagingData.exception?.run {
            return LoadResult.Error(this)
        }
        val page = params.key ?: SEARCH_STARTING_PAGE_INDEX
        return try {
            val chunkedList = fetchImagesByPage(
                data = pagingData,
                page = page
            )
            LoadResult.Page(
                data = chunkedList,
                prevKey = if (page == SEARCH_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == calculateTotalPage(pagingData.searchItemList.size)) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    /**
     * [data]안의 searchItemList를 전체 페이지 수로 나누어 [page]에 해당하는 List<SearchItem>을 리턴하는 함수입니다.
     * @return chunked로 나눠진 리스트중 요청한 페이지에 대한 List<SearchItem>
     */
    private fun fetchImagesByPage(data: PagingModel, page: Int): List<SearchItem> {
        val chunkedList = data.searchItemList.chunked(Constants.PAGE_SIZE)
        Log.d("[kks]", "chunkedList 총 묶음 개수: ${chunkedList.size}")
        return chunkedList[page - 1]
    }

    /**
     * [listSize]와 한 페이지에 담긴 SearchItem의 개수를 바탕으로 전체 페이지 수를 계산하는 함수입니다.
     * @return 전체 페이지 수(Int)
     */
    private fun calculateTotalPage(listSize: Int): Int {
        Log.d("[kks]", "Page 개수 == chunkedList 총 묶음 개수: ${(listSize / Constants.PAGE_SIZE) + 1}")
        return (listSize / Constants.PAGE_SIZE) + 1
    }
}