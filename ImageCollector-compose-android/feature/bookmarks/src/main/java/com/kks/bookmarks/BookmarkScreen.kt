package com.kks.bookmarks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kks.core.ui.ActivityViewModel

@Composable
internal fun BookmarksRoute(
    onShowToast: (message: String) -> Unit,
    viewModel: ActivityViewModel = hiltViewModel()
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        BookmarkContent(
        )
    }
}

@Composable
private fun BookmarkContent(
) {

}