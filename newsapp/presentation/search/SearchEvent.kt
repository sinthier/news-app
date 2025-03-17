package com.loc.newsapp.presentation.search

import com.loc.newsapp.domain.model.Article

sealed class SearchEvent {
    data class UpdateSearchQuery(val searchQuery: String) : SearchEvent()
    object SearchNews : SearchEvent()
    data class OnBookmarkClick(val article: Article, val userId: String) : SearchEvent()
}