package com.igor.games


import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.igor.games.databinding.GameItemBinding
import okhttp3.*
import okio.IOException


class GameAdapter(private val listener: Listener,
                  private val client: OkHttpClient) : RecyclerView.Adapter<GameAdapter.GameHolder>() {

    private var gameList = ArrayList<Game>()

    class GameHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = GameItemBinding.bind(view)

        fun bind(game: Game, listener: Listener) {

            binding.gameItemTextId.text = game.title

            itemView.setOnClickListener {
                listener.onClick(game)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.game_item, parent, false)

        return GameHolder(view)
    }

    override fun onBindViewHolder(holder: GameHolder, position: Int) {
        downloadImage(holder, gameList[position])
        holder.bind(gameList[position], listener)
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    fun addGame(game: Game) {
        gameList.add(game)
    }

    private fun downloadImage(holder: GameHolder, game: Game) {
        val request = game.thumbnail.let {
            Request.Builder()
                .url(it)
                .build()
        }

        holder.binding.gameItemImageId.tag = game.thumbnail
        holder.binding.gameItemImageId.setImageBitmap(null)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {

                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val bitmap = BitmapFactory.decodeStream(response.body!!.byteStream())
                    val thumb = holder.binding.gameItemImageId.tag as String

                     if (thumb == game.thumbnail) {
                        holder.binding.gameItemImageId.post {
                            holder.binding.gameItemImageId.setImageBitmap(bitmap)
                        }
                    }
                }

            }
        })
    }


    interface Listener {
        fun onClick(game: Game)
    }
}

