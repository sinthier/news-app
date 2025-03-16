package com.loc.newsapp.data.bookmark

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.domain.model.Source
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkDatabaseHelper @Inject constructor(
    @ApplicationContext context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        try {
            // Force database creation by getting readable database
            val db = readableDatabase
            db.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val DATABASE_NAME = "newsapp.db"
        const val DATABASE_VERSION = 1

        const val TABLE_BOOKMARK = "bookmarks"
        const val COLUMN_ID = "id"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_ARTICLE_URL = "article_url"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_SOURCE = "source"
        const val COLUMN_IMAGE_URL = "image_url"
        const val COLUMN_PUBLISHED_AT = "published_at"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val CREATE_BOOKMARK_TABLE = ("CREATE TABLE $TABLE_BOOKMARK (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_USER_ID INTEGER NOT NULL," +
                    "$COLUMN_ARTICLE_URL TEXT NOT NULL," +
                    "$COLUMN_TITLE TEXT NOT NULL," +
                    "$COLUMN_DESCRIPTION TEXT," +
                    "$COLUMN_CONTENT TEXT," +
                    "$COLUMN_AUTHOR TEXT," +
                    "$COLUMN_SOURCE TEXT," +
                    "$COLUMN_IMAGE_URL TEXT," +
                    "$COLUMN_PUBLISHED_AT TEXT);")
            db?.execSQL(CREATE_BOOKMARK_TABLE)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Failed to create database: ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKMARK")
        onCreate(db)
    }

    fun addBookmark(userId: Int, article: Article): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId)
            put(COLUMN_ARTICLE_URL, article.url)
            put(COLUMN_TITLE, article.title)
            put(COLUMN_DESCRIPTION, article.description)
            put(COLUMN_CONTENT, article.content)
            put(COLUMN_AUTHOR, article.author)
            put(COLUMN_SOURCE, article.source.name)
            put(COLUMN_IMAGE_URL, article.urlToImage)
            put(COLUMN_PUBLISHED_AT, article.publishedAt)
        }

        val result = db.insert(TABLE_BOOKMARK, null, values)
        db.close()
        return result != -1L
    }

    fun removeBookmark(userId: Int, articleUrl: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_BOOKMARK, "$COLUMN_USER_ID = ? AND $COLUMN_ARTICLE_URL = ?", 
            arrayOf(userId.toString(), articleUrl))
        db.close()
        return result > 0
    }

    fun getBookmarks(userId: Int): List<Article> {
        val bookmarks = mutableListOf<Article>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_BOOKMARK,
            null,
            "$COLUMN_USER_ID = ?",
            arrayOf(userId.toString()),
            null,
            null,
            "$COLUMN_ID DESC"
        )

        while (cursor.moveToNext()) {
            val article = Article(
                author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR)),
                content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                publishedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUBLISHED_AT)),
                source = Source(id = "", name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOURCE))),
                title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ARTICLE_URL)),
                urlToImage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
            )
            bookmarks.add(article)
        }
        cursor.close()
        db.close()
        return bookmarks
    }

    fun isBookmarked(userId: Int, articleUrl: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_BOOKMARK,
            null,
            "$COLUMN_USER_ID = ? AND $COLUMN_ARTICLE_URL = ?",
            arrayOf(userId.toString(), articleUrl),
            null,
            null,
            null
        )
        val isBookmarked = cursor.count > 0
        cursor.close()
        db.close()
        return isBookmarked
    }
}