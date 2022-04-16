package com.igor.games.DataBase

object GameConstants {
    const val TABLE_NAME = "Game"
    const val ID = "_id"
    const val TITLE = "title"
    const val THUMBNAIL = "thumbnail"
    const val SHORT_DESCRIPTION = "short_description"
    const val GAME_URL = "game_url"
    const val KEY_IMAGE = "key_image"
    const val GENRE = "genre"
    const val PLATFORM = "platform"
    const val PUBLISHER = "publisher"
    const val DEVELOPER = "developer"
    const val RELEASE_DATE = "release_date"
    const val PROFILE_URL = "profile_url"

    const val DB_NAME = "GameDB.db"
    const val DB_VERSION = 7

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE $TABLE_NAME (" +
                "$ID INTEGER PRIMARY KEY autoincrement," +
                "$TITLE TEXT," +
                "$THUMBNAIL TEXT," +
                "$SHORT_DESCRIPTION TEXT," +
                "$GAME_URL TEXT," +
                "$GENRE TEXT," +
                "$PLATFORM TEXT," +
                "$PUBLISHER TEXT," +
                "$DEVELOPER TEXT," +
                "$RELEASE_DATE TEXT," +
                "$PROFILE_URL TEXT," +
                "$KEY_IMAGE BLOB);"

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
}