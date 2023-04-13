package com.kakaobank.data.search.remote.service

import com.kakaobank.core.data.BaseResponse
import com.kakaobank.data.search.remote.model.ApiImagesResponse
import com.kakaobank.data.search.remote.model.ApiVideosResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchService {
    @GET("/v2/search/image")
    suspend fun searchImages(
        @Query("sort") sort : String,
        @Query("query") query : String,
        @Query("page") page : Int,
        @Query("size") size : Int,
    ): BaseResponse<ApiImagesResponse>

    @GET("/v2/search/vclip")
    suspend fun searchVideos(
        @Query("sort") sort : String,
        @Query("query") query : String,
        @Query("page") page : Int,
        @Query("size") size : Int,
    ): BaseResponse<ApiVideosResponse>

}