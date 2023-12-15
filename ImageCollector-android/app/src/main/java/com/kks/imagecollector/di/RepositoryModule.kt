package com.kks.imagecollector.di

import com.kks.data.search.repository.SearchRepositoryImpl
import com.kks.domain.search.repository.SearchRepository
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
