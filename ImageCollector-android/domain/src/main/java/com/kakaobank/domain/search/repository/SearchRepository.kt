package com.kakaobank.domain.search.repository

import androidx.paging.PagingData
import com.kakaobank.core.data.SearchItem
import kotlinx.coroutines.flow.Flow

/**
 *  SearchRepository의 추상화를 통해 SearchRepositoryImpl내 함수들 (firstSearchImages, searchImages)
 *  의 내부적 구현이 바뀌더라도 해당 함수들에 의존성을 가지는 Usecase들 (FirstSearchImagesUseCase, SearchImagesUseCase)이 영향을 받지 않을 수 있음
 * */
interface SearchRepository {
    suspend fun firstSearchImages(query: String): Flow<PagingData<SearchItem>>
    suspend fun searchImages(query: String): Flow<PagingData<SearchItem>>
}
