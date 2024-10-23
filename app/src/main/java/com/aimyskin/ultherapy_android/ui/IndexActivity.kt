package com.aimyskin.ultherapy_android.ui

import android.content.Intent
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
            addListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addListener() {
        binding.ivIndexHome.setOnClickListener {
            startActivity(Intent(this@IndexActivity, AwaitActivity::class.java))
            finish()
        }
        binding.ivIndexTreatment.setOnClickListener {
            startActivity(Intent(this@IndexActivity, AwaitActivity::class.java))
            finish()
        }
        binding.ivIndexUser.setOnClickListener {

        }
    }
}