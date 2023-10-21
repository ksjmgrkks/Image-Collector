package com.kks.search.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kks.search.SearchRoute

fun NavController.navigateSearch(navOptions: NavOptions) {
    navigate(SearchRoute.route, navOptions)
}

fun NavGraphBuilder.searchNavGraph(
    padding: PaddingValues,
    onSessionClick: () -> Unit,
    onContributorClick: () -> Unit,
    onShowSnackBar: (message: String?) -> Unit
) {
    composable(route = SearchRoute.route) {
        SearchRoute(
            padding,
            onSessionClick,
            onContributorClick,
            onShowSnackBar
        )
    }
}

object SearchRoute {
    const val route = "home"
}
