package com.kks.imagecollector.di

import com.kks.data.search.remote.service.SearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitServiceModule {
    @Provides
    @Singleton
    fun providesSearchService(retrofit: Retrofit): SearchService =
        retrofit.create(SearchService::class.java)
}
