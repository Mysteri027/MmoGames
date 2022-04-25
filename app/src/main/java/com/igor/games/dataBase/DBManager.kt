package com.igor.games.dataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.igor.games.Game

class DBManager(context: Context) {

    private val dbHelper = DBHelper(context)
    private val sqlDataBase: SQLiteDatabase = dbHelper.writableDatabase

    fun insertToDB(game: Game) {

        val values = ContentValues().apply {
            put(GameConstants.TITLE, game.title)
            put(GameConstants.THUMBNAIL, game.thumbnail)
            put(GameConstants.SHORT_DESCRIPTION, game.short_description)
            put(GameConstants.GAME_URL, game.game_url)
            put(GameConstants.GENRE, game.genre)
            put(GameConstants.PLATFORM, game.platform)
            put(GameConstants.PUBLISHER, game.publisher)
            put(GameConstants.DEVELOPER, game.developer)
            put(GameConstants.RELEASE_DATE, game.release_date)
            put(GameConstants.PROFILE_URL, game.profile_url)
        }
        sqlDataBase.insert(GameConstants.TABLE_NAME, null, values)
    }

    @SuppressLint("Range")
    fun readFromDB(): ArrayList<Game> {
        val games: ArrayList<Game> = ArrayList()
        val cursor: Cursor =
            sqlDataBase.query(GameConstants.TABLE_NAME,null,null,null,null,null,null)

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(GameConstants.ID))
                val title = getString(getColumnIndex(GameConstants.TITLE))
                val thumbnail =  getString(getColumnIndex(GameConstants.THUMBNAIL))
                val short_description =  getString(getColumnIndex(GameConstants.SHORT_DESCRIPTION))
                val game_url =  getString(getColumnIndex(GameConstants.GAME_URL))
                val genre =  getString(getColumnIndex(GameConstants.GENRE))
                val platform = getString(getColumnIndex(GameConstants.PLATFORM))
                val publisher = getString(getColumnIndex(GameConstants.PUBLISHER))
                val developer = getString(getColumnIndex(GameConstants.DEVELOPER))
                val release_date = getString(getColumnIndex(GameConstants.RELEASE_DATE))
                val profile_url = getString(getColumnIndex(GameConstants.PROFILE_URL))

                val game = Game(id,
                                title,
                                thumbnail,
                                short_description,
                                game_url,
                                genre,
                                platform,
                                publisher,
                                developer,
                                release_date,
                                profile_url,
                )
                games.add(game)
            }
        }
        if(!cursor.isClosed){
            cursor.close()
        }
        return games
    }

    fun closeDB() {
        dbHelper.close()
    }
}

