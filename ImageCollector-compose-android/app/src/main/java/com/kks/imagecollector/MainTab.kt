package com.kks.imagecollector

internal enum class MainTab(
    val iconResId: Int,
    internal val contentDescription: String,
    val route: String,
) {
    SEARCH(
        iconResId = R.drawable.ic_search,
        contentDescription = "검색",
        SearchRoute.route,
    ),
    BOOKMARK(
        iconResId = R.drawable.ic_bookmarks,
        contentDescription = "내 보관함",
        BookmarkRoute.route,
    );

    companion object {
        operator fun contains(route: String): Boolean {
            return values().map { it.route }.contains(route)
        }

        fun find(route: String): MainTab? {
            return values().find { it.route == route }
        }
    }
}
