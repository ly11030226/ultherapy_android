package com.aimyskin.ultherapy_android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {

        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}
