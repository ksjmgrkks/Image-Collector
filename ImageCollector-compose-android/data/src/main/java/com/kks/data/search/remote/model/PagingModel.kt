package com.kks.data.search.remote.model

import com.kks.core.data.SearchItem


data class PagingModel(
    val searchItemList: List<SearchItem>,
    val totalSize: Int,
    val exception: Exception?,
)
