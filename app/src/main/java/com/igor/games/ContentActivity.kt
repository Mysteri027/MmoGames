package com.igor.games

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.igor.games.databinding.ActivityContentBinding
import okhttp3.*
import java.io.IOException

class ContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val game = intent.getSerializableExtra("game") as Game
        binding.apply {
            genre.text = game.genre
            gameTitle.text = game.title
            developer.text = game.developer
            shortDescription.text = game.short_description
            developer.text = game.developer
            publisher.text = game.publisher
            releaseDate.text = game.release_date
            platform.text = game.platform
        }
    }
}