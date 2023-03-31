package com.kakaobank.data.search.remote.model


import android.content.Context
import com.kakaobank.core.data.SearchItem
import com.kakaobank.core.util.isBookMarked
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ApiMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun imageToSearchItem(apiEntity: ApiImagesResponse?): SearchItem {
        return SearchItem(
            imageUrl = apiEntity?.thumbnail_url.orEmpty(),
            datetime = apiEntity?.datetime.orEmpty(),
            bookmarked = context.isBookMarked(apiEntity?.thumbnail_url.orEmpty())
        )
    }

    fun videoToSearchItem(apiEntity: ApiVideosResponse?): SearchItem {
        return SearchItem(
            imageUrl = apiEntity?.thumbnail.orEmpty(),
            datetime = apiEntity?.datetime.orEmpty(),
            bookmarked = context.isBookMarked(apiEntity?.thumbnail.orEmpty())
        )
    }
}