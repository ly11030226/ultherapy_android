package com.aimyskin.ultherapy_android.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.databinding.ActivityIndexBinding

class IndexActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIndexBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndexBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}