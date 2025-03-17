package com.loc.newsapp.presentation.home

import com.loc.newsapp.domain.model.Article

sealed class HomeEvent {
    data class UpdateScrollValue(val newValue: Int): HomeEvent()
    data class UpdateMaxScrollingValue(val newValue: Int): HomeEvent()
    data class OnBookmarkClick(val article: Article, val userId: String): HomeEvent()
}