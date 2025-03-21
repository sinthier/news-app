package com.loc.newsapp.presentation.home

data class HomeState(
    val scrollValue: Int = 0,
    val maxScrollingValue: Int = 0,
    val bookmarkedArticles: Set<String> = emptySet(),
    val isBookmarked: Boolean = false
)