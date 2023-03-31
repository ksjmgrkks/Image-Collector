package com.kakaobank.data.search.remote.model

import com.kakaobank.core.data.SearchItem


data class PagingModel(
    val searchItemList: List<SearchItem>,
    val totalSize: Int,
    val exception: Exception?,
)
