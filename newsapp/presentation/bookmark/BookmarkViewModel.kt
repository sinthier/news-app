package com.loc.newsapp.presentation.bookmark
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.loc.newsapp.data.bookmark.BookmarkDbHelper
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.domain.model.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val context: Context
) : ViewModel() {

    private val dbHelper = BookmarkDbHelper(context)
    private val _state = mutableStateOf(BookmarkState())
    val state = _state

    init {
        getBookmarkedArticles()
    }

    private fun getBookmarkedArticles() {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            db = dbHelper.readableDatabase
            val userId = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getInt("user_id", -1)
            if (userId == -1) {
                _state.value = _state.value.copy(articles = emptyList())
                return
            }
            cursor = db.query(
                BookmarkDbHelper.TABLE_NAME,
                null,
                "${BookmarkDbHelper.COLUMN_USER_ID} = ?",
                arrayOf(userId.toString()),
                null, null, null
            )

            val bookmarkedArticles = mutableListOf<Article>()
            while (cursor.moveToNext()) {
                try {
                    bookmarkedArticles.add(
                        Article(
                            url = cursor.getString(cursor.getColumnIndexOrThrow(BookmarkDbHelper.COLUMN_ARTICLE_URL)),
                            title = cursor.getString(cursor.getColumnIndexOrThrow(BookmarkDbHelper.COLUMN_TITLE)),
                            urlToImage = cursor.getString(cursor.getColumnIndexOrThrow(BookmarkDbHelper.COLUMN_IMAGE_URL)),
                            description = cursor.getString(cursor.getColumnIndexOrThrow(BookmarkDbHelper.COLUMN_DESCRIPTION)),
                            content = cursor.getString(cursor.getColumnIndexOrThrow(BookmarkDbHelper.COLUMN_CONTENT)),
                            source = Source(
                                id = "", // Since we don't store source ID in DB
                                name = cursor.getString(cursor.getColumnIndexOrThrow(BookmarkDbHelper.COLUMN_SOURCE))
                            ),
                            publishedAt = cursor.getString(cursor.getColumnIndexOrThrow(BookmarkDbHelper.COLUMN_PUBLISH_DATE)),
                            author = null // Since we don't store author in DB
                        )
                    )
                } catch (e: Exception) {
                    // Skip invalid entries but continue processing
                    e.printStackTrace()
                }
            }
            _state.value = _state.value.copy(articles = bookmarkedArticles)
        } catch (e: Exception) {
            e.printStackTrace()
            _state.value = _state.value.copy(articles = emptyList())
        } finally {
            cursor?.close()
            db?.close()
        }
    }

    fun addBookmark(article: Article, userId: String) {
        val db = dbHelper.writableDatabase
        val values = android.content.ContentValues().apply {
            put(BookmarkDbHelper.COLUMN_USER_ID, userId)
            put(BookmarkDbHelper.COLUMN_ARTICLE_URL, article.url)
            put(BookmarkDbHelper.COLUMN_TITLE, article.title)
            put(BookmarkDbHelper.COLUMN_IMAGE_URL, article.urlToImage)
            put(BookmarkDbHelper.COLUMN_DESCRIPTION, article.description)
            put(BookmarkDbHelper.COLUMN_CONTENT, article.content)
            put(BookmarkDbHelper.COLUMN_SOURCE, article.source.name)
            put(BookmarkDbHelper.COLUMN_PUBLISH_DATE, article.publishedAt)
        }
        db.insertWithOnConflict(BookmarkDbHelper.TABLE_NAME, null, values, android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE)
        db.close()
        getBookmarkedArticles()
    }

    fun removeBookmark(articleUrl: String) {
        val db = dbHelper.writableDatabase
        db.delete(BookmarkDbHelper.TABLE_NAME, "${BookmarkDbHelper.COLUMN_ARTICLE_URL} = ?", arrayOf(articleUrl))
        db.close()
        getBookmarkedArticles()
    }
}