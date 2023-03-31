package com.kakaobank.domain.search.usecase

import android.util.Log
import androidx.paging.PagingData
import com.kakaobank.core.data.SearchItem
import com.kakaobank.domain.search.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class FirstSearchImagesUseCase(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String): Flow<PagingData<SearchItem>> {
        return repository.firstSearchImages(query)
    }
}
