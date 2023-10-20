package com.kks.core.data

data class SearchUiState (
    val isLoading: Boolean = false, /* ProgressBar 로딩 상태 */
    val currentQuery: String = "", /* 현재 검색되고 있는 Query' */
    val isGuideMessageVisible: Boolean = true, /* 검색탭 가이드 메시지 보여지는 상태 */
)