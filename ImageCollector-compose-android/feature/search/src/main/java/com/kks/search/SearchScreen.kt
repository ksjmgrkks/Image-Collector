package com.kks.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kks.core.data.SearchUiState

@Composable
internal fun SearchRoute(
    padding: PaddingValues,
    onShowToast: (message: String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchUiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        padding = padding,
        searchUiState = searchUiState,
        onShowToast = onShowToast
    )
}

@Composable
private fun SearchScreen(
    padding: PaddingValues,
    searchUiState: SearchUiState,
    onShowToast: (message: String) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .padding(padding)
            .padding(horizontal = 8.dp)
            .verticalScroll(scrollState)
            .padding(bottom = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {

    }
}