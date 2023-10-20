package com.kks.domain.search.usecase

import androidx.paging.PagingData
import com.kks.core.data.SearchItem
import com.kks.domain.search.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class FirstSearchImagesUseCase(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String): Flow<PagingData<SearchItem>> {
        return repository.firstSearchImages(query)
    }
}
