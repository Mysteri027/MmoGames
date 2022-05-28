package com.igor.games

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.igor.games.dataBase.DBManager
import com.igor.games.databinding.ActivityMainBinding
import com.igor.games.databinding.FragmentListBinding
import okhttp3.*
import org.json.JSONArray
import java.io.File
import java.io.IOException

class ListFragment : Fragment(), GameAdapter.Listener {

    private lateinit var binding: FragmentListBinding
    private lateinit var client: OkHttpClient
    private lateinit var dbManager: DBManager
    private var games: ArrayList<Game> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        dbManager = DBManager(requireActivity().applicationContext)

        val httpCacheDirectory = File(requireActivity().applicationContext.cacheDir, "http-cache")
        val cacheSize = 300L * 1024 * 1024 // 300 MiB
        client = OkHttpClient.Builder()
        .cache(
            Cache(
            directory = httpCacheDirectory,
            maxSize = cacheSize // 10 MiB
        )
        )
        .build()

        val adapter = GameAdapter(this, client)

         with(binding) {
            recyclerViewFragment.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
            recyclerViewFragment.adapter = adapter
        }

        downloadContentByApi(adapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDB()
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

                requireActivity().runOnUiThread(Runnable {
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
                requireActivity().runOnUiThread(Runnable {
                    for (i in 0 until games.size) {
                        adapter.addGame(games[i])
                    }
                    adapter.notifyDataSetChanged()
                    Log.d("XUI", "Rabotaet")
                })
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListFragment()
    }

    override fun onClick(game: Game) {
        val intent = Intent(activity, ContentActivity::class.java)
        startActivity(intent.apply {
            putExtra("game", game)
        })
    }
}