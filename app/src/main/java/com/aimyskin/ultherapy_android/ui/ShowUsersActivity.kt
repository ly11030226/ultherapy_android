package com.aimyskin.ultherapy_android.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.aimyskin.miscmodule.utils.ClickSoundPoolUtils
import com.aimyskin.ultherapy_android.DATA_NUMBER_PER_PAGE
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.adapter.UserListAdapter
import com.aimyskin.ultherapy_android.base.BaseActivity
import com.aimyskin.ultherapy_android.databinding.ActivityShowUsersBinding
import com.aimyskin.ultherapy_android.inter.ReminderDialogClickApplyCallback
import com.aimyskin.ultherapy_android.inter.ShowUserListClickCallback
import com.aimyskin.ultherapy_android.pojo.DataBean
import com.aimyskin.ultherapy_android.pojo.SingleOrRepeat
import com.aimyskin.ultherapy_android.pojo.User
import com.aimyskin.ultherapy_android.util.GlobalVariable
import com.aimyskin.ultherapy_android.util.getLengthValue
import com.aimyskin.ultherapy_android.util.getPitchValue
import com.aimyskin.ultherapy_android.view.MaterialDialogFactory
import com.aimyskin.ultherapy_android.viewmodel.AddUserViewModel
import com.aimyskin.ultherapy_android.viewmodel.DeleteUserViewModel
import com.aimyskin.ultherapy_android.viewmodel.GetUserListViewModel
import com.aimyskin.ultherapy_android.viewmodel.SearchUserViewModel
import com.blankj.utilcode.util.LogUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import es.dmoral.toasty.Toasty

/**
 *  用户列表页面
 */
class ShowUsersActivity : BaseActivity(), ShowUserListClickCallback {
    private lateinit var binding: ActivityShowUsersBinding
    private val getUserListViewModel: GetUserListViewModel by viewModels()
    private val deleteUserViewModel: DeleteUserViewModel by viewModels()
    private val searchUserViewModel: SearchUserViewModel by viewModels()
    private val userListAdapter: UserListAdapter = UserListAdapter(this)
    private val dataList = mutableListOf<User>()
    private var isFirstPage = true
    private var dataOffset = 0

    private val startRegisterActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //此处是跳转的result回调方法
            if (it.resultCode == Activity.RESULT_OK) {
                //注册成功刷新数据
                isFirstPage = true
                dataOffset = 0
                getUserListViewModel.getUserListFromLocal(DATA_NUMBER_PER_PAGE, dataOffset)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initRefreshLayout()
            addListener()
            initObserver()
            getUserListViewModel.getUserListFromLocal(DATA_NUMBER_PER_PAGE, dataOffset)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initObserver() {
        getUserListViewModel.run {
            getUserListLiveData.observe(this@ShowUsersActivity, Observer {
                if (it.isSuccess) {
                    LogUtils.d("list ... ${it.userList}")
                    it.userList?.let { it1 ->
                        if (isFirstPage) {
                            dataList.clear()
                        }
                        dataList.addAll(it1)
                        userListAdapter.submitList(dataList)
                    }
                } else {
                    Toasty.error(this@ShowUsersActivity, it.message).show()
                }
            })
        }

        deleteUserViewModel.run {
            deleteUserLiveData.observe(this@ShowUsersActivity, Observer {
                if (it.isSuccess) {
                    Toasty.success(this@ShowUsersActivity, it.message).show()
                    isFirstPage = true
                    dataOffset = 0
                    getUserListViewModel.getUserListFromLocal(DATA_NUMBER_PER_PAGE, dataOffset)
                } else {
                    Toasty.error(this@ShowUsersActivity, it.message).show()
                }
            })
        }

        searchUserViewModel.run {
            searchUserLiveData.observe(this@ShowUsersActivity, Observer {
                if (it.isSuccess) {
                    it.userList?.let { list ->
                        LogUtils.d("search result ... $list")
                        dataList.clear()
                        dataList.addAll(list)
                        userListAdapter.submitList(dataList)
                    }
                } else {
                    Toasty.error(this@ShowUsersActivity, it.message).show()
                }
            })
        }
    }

    private fun initRefreshLayout() {
        binding.srlUsers.setRefreshHeader(ClassicsHeader(this))
        binding.srlUsers.setRefreshFooter(ClassicsFooter(this))
        binding.srlUsers.setOnRefreshListener(OnRefreshListener { refreshlayout ->
            isFirstPage = true
            dataOffset = 0
            getUserListViewModel.getUserListFromLocal(DATA_NUMBER_PER_PAGE, dataOffset)
            //传入false表示刷新失败
            refreshlayout.finishRefresh(2000 /*,false*/)
        })
        binding.srlUsers.setOnLoadMoreListener(OnLoadMoreListener { refreshlayout ->
            isFirstPage = false
            //这里传的参数offset带表偏移量，跟currentPage差1
            getUserListViewModel.getUserListFromLocal(DATA_NUMBER_PER_PAGE, ++dataOffset)
            //传入false表示加载失败
            refreshlayout.finishLoadMore(2000 /*,false*/)
        })

        val linearLayoutManager = LinearLayoutManager(this@ShowUsersActivity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvShowUsers.layoutManager = linearLayoutManager
        binding.rvShowUsers.adapter = userListAdapter
    }

    private fun addListener() {
        //显示搜索按钮
        binding.rlShowUsersSearch.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            // REFACTOR: 这里用的自定义主题show_users_theme，不能更改背景颜色，后面会研究
            val dialog = MaterialDialog(this).show {
                customView(R.layout.dialog_search_user)
            }
            val customView = dialog.getCustomView()
            val btnApply = customView.findViewById<Button>(R.id.btn_dialog_search_apply)
            val btnCancel = customView.findViewById<Button>(R.id.btn_dialog_search_cancel)
            val etPhone = customView.findViewById<EditText>(R.id.et_dialog_search_phone)
            btnApply.setOnClickListener {
                ClickSoundPoolUtils.play(it.context, R.raw.click)
                dialog.dismiss()
                //%用于执行模糊查询
                val phone = "%" + etPhone.text.toString() + "%"
                isFirstPage = true
                dataOffset = 0
                if (phone.isNotEmpty()) {
                    searchUserViewModel.searchUserByTelephone(phone)
                } else {
                    getUserListViewModel.getUserListFromLocal(DATA_NUMBER_PER_PAGE, dataOffset)
                }
            }
            btnCancel.setOnClickListener {
                ClickSoundPoolUtils.play(it.context, R.raw.click)
                dialog.dismiss()
            }
            dialog.show()
        }
        //添加新用户
        binding.llShowUsersIncrease.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            startRegisterActivity.launch(Intent(this@ShowUsersActivity, RegisterActivity::class.java))
        }
        //返回
        binding.ivShowUsersBack.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            startActivity(Intent(this@ShowUsersActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun clickSave(position: Int) {
        GlobalVariable.currentUser = dataList[position]
        startActivity(Intent(this@ShowUsersActivity, AwaitActivity::class.java))
        finish()
    }

    override fun clickDelete(position: Int) {
        LogUtils.d("clickDelete position ... $position")
        val reminder = String.format(getString(R.string.dialog_delete_user_reminder), dataList[position].name)
        MaterialDialogFactory.instance.openReminderDialog(this@ShowUsersActivity, reminder, object : ReminderDialogClickApplyCallback {
            override fun clickApply() {
                deleteUserViewModel.deleteUserFromLocal(dataList[position])
            }
        })
    }
}