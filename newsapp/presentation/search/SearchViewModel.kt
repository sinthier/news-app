package com.loc.newsapp.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.loc.newsapp.domain.usecases.news.NewsUseCases
import com.loc.newsapp.domain.usecases.news.SearchNews
import com.loc.newsapp.presentation.bookmark.BookmarkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases,
    private val bookmarkViewModel: BookmarkViewModel
) : ViewModel() {

    private val _state = mutableStateOf(SearchState())
    val state: State<SearchState> = _state

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.UpdateSearchQuery -> {
                _state.value = state.value.copy(searchQuery = event.searchQuery)
            }

            is SearchEvent.SearchNews -> {
                searchNews()
            }

            is SearchEvent.OnBookmarkClick -> {
                // Add the article to bookmarks using the BookmarkViewModel
                bookmarkViewModel.addBookmark(event.article, event.userId)
                // Update the state to reflect that the article is bookmarked
                _state.value = state.value.copy(
                    bookmarkedArticles = state.value.bookmarkedArticles + event.article.url,
                    isBookmarked = true
                )
            }
        }
    }

    private fun searchNews() {
        val articles = newsUseCases.searchNews(
            searchQuery = state.value.searchQuery,
            sources = listOf("bbc-news", "abc-news", "al-jazeera-english")
        ).cachedIn(viewModelScope)
        _state.value = state.value.copy(articles = articles)
    }

}