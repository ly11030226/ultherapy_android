package com.aimyskin.ultherapy_android.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.Orientation
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.aimyskin.miscmodule.utils.ClickSoundPoolUtils
import com.aimyskin.ultherapy_android.DATA_NUMBER_PER_PAGE
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.USER_MANAGER_GRID_COLUMN
import com.aimyskin.ultherapy_android.adapter.UserManagerGridAdapter
import com.aimyskin.ultherapy_android.adapter.UserManagerListAdapter
import com.aimyskin.ultherapy_android.base.BaseFragment
import com.aimyskin.ultherapy_android.databinding.FragmentUserListBinding
import com.aimyskin.ultherapy_android.pojo.User
import com.aimyskin.ultherapy_android.viewmodel.GetUserListViewModel
import com.aimyskin.ultherapy_android.viewmodel.SearchUserViewModel
import com.blankj.utilcode.util.LogUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import es.dmoral.toasty.Toasty


class UserListFragment : BaseFragment() {
    private val getUserListViewModel: GetUserListViewModel by viewModels()
    private val searchUserViewModel: SearchUserViewModel by viewModels()
    private val adapter: UserManagerListAdapter = UserManagerListAdapter()
    private val dataList = mutableListOf<User>()
    private var isFirstPage = true
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.srlListUsers.setRefreshHeader(ClassicsHeader(activity))
        binding.srlListUsers.setRefreshFooter(ClassicsFooter(activity))
        binding.srlListUsers.setOnRefreshListener(OnRefreshListener { refreshlayout ->
            //传入false表示刷新失败
            refreshlayout.finishRefresh(2000 /*,false*/)
        })
        binding.srlListUsers.setOnLoadMoreListener(OnLoadMoreListener { refreshlayout ->
            //传入false表示加载失败
            refreshlayout.finishLoadMore(2000 /*,false*/)
        })

        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.rvShowUsersList.layoutManager = linearLayoutManager
        binding.rvShowUsersList.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            val data = adapter.getItem(position)
            data?.let { (activity as UserManagerActivity).choiceUser(it) }
        }
    }

    override fun addListener() {
        binding.rlListSearch.setOnClickListener {
            ClickSoundPoolUtils.play(it.context, R.raw.click)
            activity?.let { activity ->
                val dialog = MaterialDialog(activity).show {
                    activity.setTheme(R.style.show_users_theme)
                    customView(R.layout.dialog_search_user)
                }
                val customView = dialog.getCustomView()
                val btnApply = customView.findViewById<Button>(R.id.btn_dialog_search_apply)
                val btnCancel = customView.findViewById<Button>(R.id.btn_dialog_search_cancel)
                val etPhone = customView.findViewById<EditText>(R.id.et_dialog_search_phone)
                btnApply.setOnClickListener { v ->
                    ClickSoundPoolUtils.play(v.context, R.raw.click)
                    dialog.dismiss()
                    //%用于执行模糊查询
                    val phone = "%" + etPhone.text.toString() + "%"
                    isFirstPage = true
                    if (phone.isNotEmpty()) {
                        searchUserViewModel.searchUserByTelephone(phone)
                    } else {
                        getUserListViewModel.getUserListFromLocal(DATA_NUMBER_PER_PAGE, 0)
                    }
                }
                btnCancel.setOnClickListener { v ->
                    ClickSoundPoolUtils.play(v.context, R.raw.click)
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    }

    override fun createObserver() {
        getUserListViewModel.run {
            activity?.let {
                getUserListLiveData.observe(it, Observer { uiState ->
                    if (uiState.isSuccess) {
                        LogUtils.d("list ... ${uiState.userList}")
                        uiState.userList?.let { it1 ->
                            if (isFirstPage) {
                                dataList.clear()
                            }
                            dataList.addAll(it1)
                            adapter.submitList(dataList)
                        }
                    } else {
                        Toasty.error(requireActivity(), uiState.message).show()
                    }
                })
            }
        }

        searchUserViewModel.run {
            activity?.let { activity ->
                searchUserLiveData.observe(activity, Observer {
                    if (it.isSuccess) {
                        it.userList?.let { list ->
                            LogUtils.d("search result ... $list")
                            dataList.clear()
                            dataList.addAll(list)
                            adapter.submitList(dataList)
                        }
                    } else {
                        Toasty.error(activity, it.message).show()
                    }
                })
            }
        }
    }

    override fun initData() {
        getUserListViewModel.getUserListFromLocal(DATA_NUMBER_PER_PAGE, 0)
    }
}