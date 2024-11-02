package com.aimyskin.ultherapy_android.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.databinding.ActivityShowUsersBinding
import java.lang.Exception

/*
 用户列表页面
 */
class ShowUsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowUsersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}