package com.igor.games

import android.graphics.Bitmap
import java.io.Serializable

data class Game(
    val id: Long,
    val title: String,
    val thumbnail: String,
    val short_description: String,
    val game_url: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    val developer: String,
    val release_date: String,
    val profile_url: String,
) : Serializable
