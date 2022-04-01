package com.igor.games

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.WorkerThread
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.igor.games.databinding.ActivityMainBinding
import okhttp3.*
import okio.IOException
import org.json.JSONArray

class MainActivity : AppCompatActivity(), GameAdapter.Listener {

    private lateinit var binding: ActivityMainBinding
    private val adapter = GameAdapter(this)
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        downloadContentByApi()

    }

    private fun init() {
        binding.recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 2)
        binding.recyclerView.adapter = adapter
    }

    override fun onClick(game: Game) {
        startActivity(Intent(this, ContentActivity::class.java).apply {
            putExtra("game", game)
        })
    }

    private fun downloadContentByApi() {
        val url = "https://www.mmobomb.com/api1/games"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val jsonGames: JSONArray = JSONArray(response.body!!.string())

                val games: ArrayList<Game> = ArrayList()

                for (i in 0 until jsonGames.length()) {
                    val jsonGameItem = jsonGames.getJSONObject(i)

                    val id = jsonGameItem.getInt("id")
                    val title = jsonGameItem.getString("title")
                    val thumbnail = jsonGameItem.getString("thumbnail")
                    val shortDescription = jsonGameItem.getString("short_description")
                    val gameUrl = jsonGameItem.getString("game_url")
                    val genre = jsonGameItem.getString("genre")
                    val platform = jsonGameItem.getString("platform")
                    val publisher = jsonGameItem.getString("publisher")
                    val developer = jsonGameItem.getString("developer")
                    val releaseDate= jsonGameItem.getString("release_date")
                    val profileUrl = jsonGameItem.getString("profile_url")

                    val game = Game(
                        id,
                        title,
                        thumbnail,
                        shortDescription,
                        gameUrl,
                        genre,
                        platform,
                        publisher,
                        developer,
                        releaseDate,
                        profileUrl,
                    )
                    games.add(game)
                }
                for (i in 0 until games.size) {
                    runOnUiThread(Runnable {
                        adapter.addGame(games[i])
                        adapter.notifyDataSetChanged()
                    })

                }
            }

        })
    }
}