package com.kks.bookmarks.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kks.bookmarks.BookmarksRoute

fun NavController.navigateBookmark(navOptions: NavOptions) {
    navigate(BookmarkRoute.route, navOptions)
}

fun NavGraphBuilder.bookmarkNavGraph(
    onShowToast: (message: String) -> Unit
) {
    composable(route = BookmarkRoute.route) {
        BookmarksRoute(onShowToast)
    }
}

object BookmarkRoute {
    const val route = "bookmark"
}