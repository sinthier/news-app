package com.loc.newsapp.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.loc.newsapp.domain.usecases.news.NewsUseCases
import com.loc.newsapp.presentation.bookmark.BookmarkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    newsUseCases: NewsUseCases,
    private val bookmarkViewModel: BookmarkViewModel
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    val news = newsUseCases.getNews(
        sources = listOf("bbc-news", "abc-news", "al-jazeera-english")
    ).cachedIn(viewModelScope)
    fun onEvent(event: HomeEvent){
        when(event){
            is HomeEvent.UpdateScrollValue -> updateScrollValue(event.newValue)
            is HomeEvent.UpdateMaxScrollingValue -> updateMaxScrollingValue(event.newValue)
            is HomeEvent.OnBookmarkClick -> {
                bookmarkViewModel.addBookmark(event.article, event.userId)
                _state.value = state.value.copy(
                    bookmarkedArticles = state.value.bookmarkedArticles + event.article.url,
                    isBookmarked = true
                )
            }
        }
    }
    private fun updateScrollValue(newValue: Int){
        _state.value = state.value.copy(scrollValue = newValue)
    }
    private fun updateMaxScrollingValue(newValue: Int){
        _state.value = state.value.copy(maxScrollingValue = newValue)
    }

}