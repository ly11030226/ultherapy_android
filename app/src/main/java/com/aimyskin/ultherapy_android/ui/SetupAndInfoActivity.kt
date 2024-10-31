package com.aimyskin.ultherapy_android.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aimyskin.ultherapy_android.KEY_FROM_WHERE_TO_SETUP
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.WHERE_FROM_AWAIT
import com.aimyskin.ultherapy_android.WHERE_FROM_MAIN
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivitySetupAndInfoBinding

class SetupAndInfoActivity : BaseActivity() {
    private lateinit var binding: ActivitySetupAndInfoBinding
    private var isShowSetupFragment = true
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupAndInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            val navHostFragment = supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment
            ) as NavHostFragment
            navController = navHostFragment.navController
            addListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addListener() {
        binding.llSettingBack.setOnClickListener {
            intent?.let {
                if (it.hasExtra(KEY_FROM_WHERE_TO_SETUP)) {
                    when (it.getStringExtra(KEY_FROM_WHERE_TO_SETUP)) {
                        WHERE_FROM_AWAIT -> {
                            startActivity(
                                Intent(
                                    this@SetupAndInfoActivity,
                                    AwaitActivity::class.java
                                )
                            )
                            finish()
                        }

                        WHERE_FROM_MAIN -> {
                            startActivity(
                                Intent(
                                    this@SetupAndInfoActivity,
                                    MainActivity::class.java
                                )
                            )
                            finish()
                        }
                    }
                }
            }
        }
        binding.tvSettingSetup.setOnClickListener {
            if (!isShowSetupFragment) {
                binding.ivSettingLine.setBackgroundResource(R.drawable.icon_setup_light)
                isShowSetupFragment = true
                navController.navigate(R.id.action_infoFragment_to_setupFragment)
            }
        }
        binding.tvSettingInfo.setOnClickListener {
            if (isShowSetupFragment) {
                binding.ivSettingLine.setBackgroundResource(R.drawable.icon_info_light)
                isShowSetupFragment = false
                navController.navigate(R.id.action_setupFragment_to_infoFragment)
            }
        }
    }
}