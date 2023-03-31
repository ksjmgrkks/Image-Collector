package com.kakaobank.imagecollector.di

import com.kakaobank.domain.search.repository.SearchRepository
import com.kakaobank.domain.search.usecase.FirstSearchImagesUseCase
import com.kakaobank.domain.search.usecase.SearchImagesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @ViewModelScoped
    @Provides
    fun providesSearchImagesUseCase(repository: SearchRepository): SearchImagesUseCase {
        return SearchImagesUseCase(repository)
    }

    @ViewModelScoped
    @Provides
    fun providesFirstSearchImagesUseCase(repository: SearchRepository): FirstSearchImagesUseCase {
        return FirstSearchImagesUseCase(repository)
    }
}
