package com.alex.carlauncher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alex.carlauncher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animación de entrada estilo DUDU (fade + scale)
        binding.root.alpha = 0f
        binding.root.scaleY = 0.95f
        binding.root.animate()
            .alpha(1f)
            .scaleY(1f)
            .setDuration(800)
            .start()
    }
}