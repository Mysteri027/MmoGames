package com.igor.games

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.igor.games.dataBase.DBManager
import com.igor.games.databinding.ActivityMainBinding
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import java.io.File

class MainActivity : AppCompatActivity(), GameAdapter.Listener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var client: OkHttpClient
    private lateinit var dbManager: DBManager
    private var games: ArrayList<Game> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbManager = DBManager(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val httpCacheDirectory = File(applicationContext.cacheDir, "http-cache")
        val cacheSize = 300L * 1024 * 1024 // 300 MiB
        client = OkHttpClient.Builder()
        .cache(Cache(
            directory = httpCacheDirectory,
            maxSize = cacheSize // 10 MiB
        ))
        .build()

        val adapter = GameAdapter(this, client)

        init(adapter)
        downloadContentByApi(adapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDB()
    }

    private fun init(adapter: GameAdapter) {
        with (binding) {
            recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 3)
            recyclerView.adapter = adapter
        }
    }

    override fun onClick(game: Game) {
        startActivity(Intent(this, ContentActivity::class.java).apply {
            putExtra("game", game)
        })
    }

    private fun downloadContentByApi(adapter: GameAdapter) {
        val url = "https://www.mmobomb.com/api1/games"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            @SuppressLint("NotifyDataSetChanged")
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                games = dbManager.readFromDB()

                runOnUiThread(Runnable {
                    for (i in 0 until games.size) {
                        adapter.addGame(games[i])
                    }
                    adapter.notifyDataSetChanged()
                })
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val jsonGames = JSONArray(response.body!!.string())


                    for (i in 0 until jsonGames.length()) {
                        val jsonGameItem = jsonGames.getJSONObject(i)

                        val id = jsonGameItem.getLong("id")
                        val title = jsonGameItem.getString("title")
                        val thumbnail = jsonGameItem.getString("thumbnail")
                        val shortDescription = jsonGameItem.getString("short_description")
                        val gameUrl = jsonGameItem.getString("game_url")
                        val genre = jsonGameItem.getString("genre")
                        val platform = jsonGameItem.getString("platform")
                        val publisher = jsonGameItem.getString("publisher")
                        val developer = jsonGameItem.getString("developer")
                        val releaseDate = jsonGameItem.getString("release_date")
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
                        dbManager.insertToDB(game)
                    }
                runOnUiThread(Runnable {
                    for (i in 0 until games.size) {
                        adapter.addGame(games[i])
                    }
                    adapter.notifyDataSetChanged()
                })
            }
        })
    }
}