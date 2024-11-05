package com.aimyskin.ultherapy_android.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityUserManagerBinding
import com.aimyskin.ultherapy_android.inter.ChoiceUserCallback
import com.aimyskin.ultherapy_android.pojo.User
import com.aimyskin.ultherapy_android.util.GlobalVariable

class UserManagerActivity : BaseActivity(), ChoiceUserCallback {
    private lateinit var binding: ActivityUserManagerBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initView()
            addListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
        initNav()
        binding.llUserManagerRemake.setBackgroundResource(R.drawable.shape_user_manager_uncheck)
        binding.ivUserManagerRemake.setBackgroundResource(R.drawable.icon_list_uncheck)
        binding.llUserManagerRecord.setBackgroundResource(R.drawable.shape_user_manager_uncheck)
        binding.ivUserManagerRecord.setBackgroundResource(R.drawable.icon_record_uncheck)
    }

    private fun initNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_user_manager
        ) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun addListener() {
        //点击用户记录
        binding.llUserManagerRecord.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.UserGridFragment -> {
                    binding.llUserManagerRemake.setBackgroundResource(R.drawable.shape_user_manager_uncheck)
                    binding.ivUserManagerRemake.setBackgroundResource(R.drawable.icon_list_uncheck)
                    binding.llUserManagerRecord.setBackgroundResource(R.drawable.shape_user_manager_check)
                    binding.ivUserManagerRecord.setBackgroundResource(R.drawable.icon_record_check)
                    navController.navigate(R.id.action_gridFragment_to_recordFragment)
                }

                R.id.UserListFragment -> {
                    binding.llUserManagerRemake.setBackgroundResource(R.drawable.shape_user_manager_uncheck)
                    binding.ivUserManagerRemake.setBackgroundResource(R.drawable.icon_list_uncheck)
                    binding.llUserManagerRecord.setBackgroundResource(R.drawable.shape_user_manager_check)
                    binding.ivUserManagerRecord.setBackgroundResource(R.drawable.icon_record_check)
                    navController.navigate(R.id.action_listFragment_to_recordFragment)
                }

                R.id.UserRecordFragment -> {}
            }
        }
        //点击Remake
        binding.llUserManagerRemake.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.UserGridFragment -> {
                    binding.llUserManagerRemake.setBackgroundResource(R.drawable.shape_user_manager_check)
                    binding.ivUserManagerRemake.setBackgroundResource(R.drawable.icon_list_check)
                    binding.llUserManagerRecord.setBackgroundResource(R.drawable.shape_user_manager_uncheck)
                    binding.ivUserManagerRecord.setBackgroundResource(R.drawable.icon_record_uncheck)
                    navController.navigate(R.id.action_gridFragment_to_listFragment)
                }

                R.id.UserListFragment -> {
                    binding.llUserManagerRemake.setBackgroundResource(R.drawable.shape_user_manager_uncheck)
                    binding.ivUserManagerRemake.setBackgroundResource(R.drawable.icon_list_uncheck)
                    binding.llUserManagerRecord.setBackgroundResource(R.drawable.shape_user_manager_uncheck)
                    binding.ivUserManagerRecord.setBackgroundResource(R.drawable.icon_record_uncheck)
                    navController.navigate(R.id.action_listFragment_to_gridFragment)
                }

                R.id.UserRecordFragment -> {
                    binding.llUserManagerRemake.setBackgroundResource(R.drawable.shape_user_manager_uncheck)
                    binding.ivUserManagerRemake.setBackgroundResource(R.drawable.icon_list_uncheck)
                    binding.llUserManagerRecord.setBackgroundResource(R.drawable.shape_user_manager_uncheck)
                    binding.ivUserManagerRecord.setBackgroundResource(R.drawable.icon_record_uncheck)
                    navController.navigate(R.id.action_recordFragment_to_gridFragment)
                }
            }
        }
        //点击Refresh
        binding.llUserManagerRefresh.setOnClickListener {

        }
        binding.ivUserManagerBack.setOnClickListener {
            startActivity(Intent(this@UserManagerActivity,MainActivity::class.java))
            finish()
        }
    }

    override fun choiceUser(user: User) {
        when (navController.currentDestination?.id) {
            R.id.UserGridFragment -> {
                //保存选中的用户
                GlobalVariable.currentUser = user
                binding.llUserManagerRemake.setBackgroundResource(R.drawable.shape_user_manager_uncheck)
                binding.ivUserManagerRemake.setBackgroundResource(R.drawable.icon_list_uncheck)
                binding.llUserManagerRecord.setBackgroundResource(R.drawable.shape_user_manager_check)
                binding.ivUserManagerRecord.setBackgroundResource(R.drawable.icon_record_check)
                navController.navigate(R.id.action_gridFragment_to_recordFragment)
            }

            R.id.UserListFragment -> {
                //保存选中的用户
                GlobalVariable.currentUser = user
                binding.llUserManagerRemake.setBackgroundResource(R.drawable.shape_user_manager_uncheck)
                binding.ivUserManagerRemake.setBackgroundResource(R.drawable.icon_list_uncheck)
                binding.llUserManagerRecord.setBackgroundResource(R.drawable.shape_user_manager_check)
                binding.ivUserManagerRecord.setBackgroundResource(R.drawable.icon_record_check)
                navController.navigate(R.id.action_listFragment_to_recordFragment)
            }

            R.id.UserRecordFragment -> {}
        }
    }
}