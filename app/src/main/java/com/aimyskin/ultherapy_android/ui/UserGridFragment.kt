package com.aimyskin.ultherapy_android.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aimyskin.ultherapy_android.DATA_NUMBER_PER_PAGE
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.USER_MANAGER_GRID_COLUMN
import com.aimyskin.ultherapy_android.adapter.UserManagerGridAdapter
import com.aimyskin.ultherapy_android.base.BaseFragment
import com.aimyskin.ultherapy_android.databinding.FragmentUserGridBinding
import com.aimyskin.ultherapy_android.pojo.User
import com.aimyskin.ultherapy_android.viewmodel.GetUserListViewModel
import com.blankj.utilcode.util.LogUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import es.dmoral.toasty.Toasty

class UserGridFragment : BaseFragment() {
    private val getUserListViewModel: GetUserListViewModel by viewModels()
    private var _binding: FragmentUserGridBinding? = null
    private val adapter: UserManagerGridAdapter = UserManagerGridAdapter()
    private val dataList = mutableListOf<User>()
    private var isFirstPage = true
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserGridBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.srlGridUsers.setRefreshHeader(ClassicsHeader(activity))
        binding.srlGridUsers.setRefreshFooter(ClassicsFooter(activity))
        binding.srlGridUsers.setOnRefreshListener(OnRefreshListener { refreshlayout ->
            //传入false表示刷新失败
            refreshlayout.finishRefresh(2000 /*,false*/)
        })
        binding.srlGridUsers.setOnLoadMoreListener(OnLoadMoreListener { refreshlayout ->
            //传入false表示加载失败
            refreshlayout.finishLoadMore(2000 /*,false*/)
        })

        val gridLayoutManager = GridLayoutManager(activity, USER_MANAGER_GRID_COLUMN)
        binding.rvShowUsers.layoutManager = gridLayoutManager
        binding.rvShowUsers.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            val data = adapter.getItem(position)
            data?.let { (activity as UserManagerActivity).choiceUser(it) }
        }
    }

    override fun addListener() {
        binding.rlGridSearch.setOnClickListener {

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
    }

    override fun initData() {
        getUserListViewModel.getUserListFromLocal(DATA_NUMBER_PER_PAGE, 0)
    }
}