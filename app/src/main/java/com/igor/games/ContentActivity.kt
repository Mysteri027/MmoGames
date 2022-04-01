package com.igor.games

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
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
        downloadImage(game)
    }

    fun downloadImage(game: Game) {
        val request =  Request.Builder()
                .url(game.thumbnail)
                .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {

                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        val bitmap = BitmapFactory.decodeStream(response.body!!.byteStream())
                            binding.gameImage.post {
                            binding.gameImage.setImageBitmap(bitmap)
                        }
                    }

                }

        })
    }
}