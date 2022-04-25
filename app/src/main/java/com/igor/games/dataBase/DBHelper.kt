package com.igor.games.dataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context,
                                                    GameConstants.TABLE_NAME,
                                                    null,
                                                    GameConstants.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(GameConstants.SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(GameConstants.SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}