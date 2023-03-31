package com.kakaobank.imagecollector.di

import com.kakaobank.data.search.repository.SearchRepositoryImpl
import com.kakaobank.domain.search.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindsSearchRepository(repository: SearchRepositoryImpl): SearchRepository
}
