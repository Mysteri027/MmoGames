package com.igor.games

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.igor.games.databinding.GameItemFragmentBinding

interface GameInfo {
    fun getGame(): Game
}

class GameItemFragment() : Fragment() {
     private lateinit var binding: GameItemFragmentBinding
     private lateinit var listener: GameInfo

     override fun onCreateView(
           inflater: LayoutInflater, container: ViewGroup?,
           savedInstanceState: Bundle?
    ): View {

        binding = GameItemFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

     override fun onStart() {
        super.onStart()
        val game = listener.getGame()


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
         Log.d("dsfsd", "Log")
     }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as GameInfo
        } catch (e: ClassCastException ) {
            throw ClassCastException(activity.toString() + " must implement GameInfo")
        }

    }
}