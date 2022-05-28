package com.igor.games

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.igor.games.databinding.ActivityContentBinding
import okhttp3.*
import java.io.IOException

class ContentActivity : AppCompatActivity(), GameInfo {

    private lateinit var binding: ActivityContentBinding
    private lateinit var data: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getSerializableExtra("game") as Game
    }

    override fun getGame(): Game {
        return data
    }
}

