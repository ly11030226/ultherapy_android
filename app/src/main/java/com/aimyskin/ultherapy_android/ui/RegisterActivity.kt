package com.aimyskin.ultherapy_android.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityRegisterBinding
import com.aimyskin.ultherapy_android.pojo.Gender
import com.blankj.utilcode.util.LogUtils

/**
 * 注册用户页面
 */
class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    val genderList = arrayOf(Gender.FEMALE, Gender.MALE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initView()
            addListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
//        binding.spRegisterGender.onItemSelectedListener(object: AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//        })
    }

    private fun addListener() {
        binding.ivRegisterBack.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, IndexActivity::class.java))
            finish()
        }
    }
}