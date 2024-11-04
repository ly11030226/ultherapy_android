package com.aimyskin.ultherapy_android.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityShowUsersBinding
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.SingleOrRepeat
import com.aimyskin.ultherapy_android.util.getLengthValue
import com.aimyskin.ultherapy_android.util.getPitchValue
import com.blankj.utilcode.util.LogUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener


/**
 *  用户列表页面
 */
class ShowUsersActivity : BaseActivity() {
    private lateinit var binding: ActivityShowUsersBinding

    private val startRegisterActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //此处是跳转的result回调方法
            if (it.resultCode == Activity.RESULT_OK) {
                //注册成功刷新数据
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initRefreshLayout()
            addListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initRefreshLayout() {
        binding.srlUsers.setRefreshHeader(ClassicsHeader(this))
        binding.srlUsers.setRefreshFooter(ClassicsFooter(this))
        binding.srlUsers.setOnRefreshListener(OnRefreshListener { refreshlayout ->
            //传入false表示刷新失败
            refreshlayout.finishRefresh(2000 /*,false*/)
        })
        binding.srlUsers.setOnLoadMoreListener(OnLoadMoreListener { refreshlayout ->
            //传入false表示加载失败
            refreshlayout.finishLoadMore(2000 /*,false*/)
        })
    }

    private fun addListener() {
        //显示搜索按钮
        binding.rlShowUsersSearch.setOnClickListener {
            val dialog = MaterialDialog(this).show {
                setTheme(R.style.show_users_theme)
                customView(R.layout.dialog_search_user)
            }
            val customView = dialog.getCustomView()
            val btnApply = customView.findViewById<Button>(R.id.btn_dialog_search_apply)
            val btnCancel = customView.findViewById<Button>(R.id.btn_dialog_search_cancel)
            val etPhone = customView.findViewById<EditText>(R.id.et_dialog_search_phone)
            btnApply.setOnClickListener {
                dialog.dismiss()
                LogUtils.d("phone ... ${etPhone.text.toString()}")
            }
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        //添加新用户
        binding.llShowUsersIncrease.setOnClickListener {
            startRegisterActivity.launch(Intent(this@ShowUsersActivity, RegisterActivity::class.java))
        }
        //返回
        binding.ivShowUsersBack.setOnClickListener {
            startActivity(Intent(this@ShowUsersActivity, MainActivity::class.java))
            finish()
        }
    }
}