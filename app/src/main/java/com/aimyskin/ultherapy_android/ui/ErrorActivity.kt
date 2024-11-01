package com.aimyskin.ultherapy_android.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityErrorBinding
import java.lang.Exception

class ErrorActivity : BaseActivity() {
    private lateinit var binding: ActivityErrorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}