package com.loc.newsapp.presentation.bookmark

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.data.bookmark.BookmarkDatabaseHelper
import com.loc.newsapp.data.user.UserDatabaseHelper
import com.loc.newsapp.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkDatabaseHelper: BookmarkDatabaseHelper,
    private val userDatabaseHelper: UserDatabaseHelper
) : ViewModel() {


    private val _state = mutableStateOf(BookmarkState())
    val state: State<BookmarkState> = _state

    init {
        getArticles()
    }

    private fun getArticles() {
        viewModelScope.launch {
            val userId = getCurrentUserId()
            if (userId != null) {
                val bookmarks = bookmarkDatabaseHelper.getBookmarks(userId)
                _state.value = _state.value.copy(articles = bookmarks)
            }
        }
    }

    private fun getCurrentUserId(): Int? {
        // Get the current user ID from the database
        // For now, we'll return the first user's ID
        val db = userDatabaseHelper.readableDatabase
        val cursor = db.query(
            UserDatabaseHelper.TABLE_USER,
            arrayOf(UserDatabaseHelper.COLUMN_ID),
            null,
            null,
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(UserDatabaseHelper.COLUMN_ID)
            if (columnIndex >= 0) {
                val id = cursor.getInt(columnIndex)
                cursor.close()
                id
            } else {
                cursor.close()
                null
            }
        } else {
            cursor.close()
            null
        }
    }

    fun addBookmark(article: Article) {
        viewModelScope.launch {
            if (bookmarkDatabaseHelper.addBookmark(1, article)) { // TODO: Replace with actual user ID
                getArticles()
            }
        }
    }

    fun removeBookmark(article: Article) {
        viewModelScope.launch {
            if (bookmarkDatabaseHelper.removeBookmark(1, article.url)) { // TODO: Replace with actual user ID
                getArticles()
            }
        }
    }

    fun isBookmarked(articleUrl: String): Boolean {
        return bookmarkDatabaseHelper.isBookmarked(1, articleUrl) // TODO: Replace with actual user ID
    }

}