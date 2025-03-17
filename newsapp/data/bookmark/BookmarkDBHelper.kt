package com.loc.newsapp.data.bookmark

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BookmarkDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "newsapp.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "bookmarks"
        const val COLUMN_ID = "id"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_ARTICLE_URL = "article_url"
        const val COLUMN_TITLE = "title"
        const val COLUMN_IMAGE_URL = "image_url"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_SOURCE = "source"
        const val COLUMN_PUBLISH_DATE = "publish_date"
    }
    override fun onCreate(db: SQLiteDatabase) {
        val createBookmarkTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID INTEGER,
                $COLUMN_ARTICLE_URL TEXT UNIQUE,
                $COLUMN_TITLE TEXT,
                $COLUMN_IMAGE_URL TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_CONTENT TEXT,
                $COLUMN_SOURCE TEXT,
                $COLUMN_PUBLISH_DATE TEXT,
                FOREIGN KEY($COLUMN_USER_ID) REFERENCES user(id)
            )"""
        db.execSQL(createBookmarkTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}