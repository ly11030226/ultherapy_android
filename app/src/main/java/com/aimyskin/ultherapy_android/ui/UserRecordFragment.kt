package com.aimyskin.ultherapy_android.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aimyskin.ultherapy_android.DEFAULT_GUEST_BIRTHDAY
import com.aimyskin.ultherapy_android.DEFAULT_GUEST_EMAIL
import com.aimyskin.ultherapy_android.DEFAULT_GUEST_GENDER
import com.aimyskin.ultherapy_android.DEFAULT_GUEST_ID
import com.aimyskin.ultherapy_android.DEFAULT_GUEST_NAME
import com.aimyskin.ultherapy_android.DEFAULT_GUEST_TELEPHONE
import com.aimyskin.ultherapy_android.R
import com.aimyskin.ultherapy_android.adapter.RecordListAdapter
import com.aimyskin.ultherapy_android.base.BaseFragment
import com.aimyskin.ultherapy_android.databinding.FragmentUserRecordBinding
import com.aimyskin.ultherapy_android.inter.ClickRefreshCallback
import com.aimyskin.ultherapy_android.inter.ReminderDialogClickApplyCallback
import com.aimyskin.ultherapy_android.inter.ShowRecordListClickCallback
import com.aimyskin.ultherapy_android.pojo.GuestRecord
import com.aimyskin.ultherapy_android.pojo.User
import com.aimyskin.ultherapy_android.pojo.Record
import com.aimyskin.ultherapy_android.util.GlobalVariable
import com.aimyskin.ultherapy_android.view.MaterialDialogFactory
import com.aimyskin.ultherapy_android.viewmodel.AddUserViewModel
import com.aimyskin.ultherapy_android.viewmodel.DeleteGuestRecordViewModel
import com.aimyskin.ultherapy_android.viewmodel.DeleteRecordViewModel
import com.aimyskin.ultherapy_android.viewmodel.GetGuestRecordListViewModel
import com.aimyskin.ultherapy_android.viewmodel.GetRecordListViewModel
import com.blankj.utilcode.util.LogUtils
import es.dmoral.toasty.Toasty

class UserRecordFragment : BaseFragment(), ShowRecordListClickCallback, ClickRefreshCallback {
    private var _binding: FragmentUserRecordBinding? = null
    private val getRecordListViewModel: GetRecordListViewModel by viewModels()
    private val getGuestRecordListViewModel: GetGuestRecordListViewModel by viewModels()
    private val deleteRecordViewModel: DeleteRecordViewModel by viewModels()
    private val deleteGuestRecordViewModel: DeleteGuestRecordViewModel by viewModels()
    private var adapter: RecordListAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val dataList = mutableListOf<Record>()
    private val guestList = mutableListOf<GuestRecord>()

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserRecordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.tvRecordName.text = GlobalVariable.currentUser?.name ?: DEFAULT_GUEST_NAME
        binding.tvRecordGender.text = GlobalVariable.currentUser?.gender ?: "xxx"
        binding.tvRecordBirthday.text = GlobalVariable.currentUser?.birth ?: "xxxx/xx/xx"
        binding.tvRecordTelephone.text = GlobalVariable.currentUser?.telephone ?: "xxxxxxxxxxxxx"
        binding.tvRecordEmail.text = GlobalVariable.currentUser?.email ?: "xxxxxxxxxx"
        linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        adapter = RecordListAdapter(this, binding.tvRecordName.text.toString(), binding.tvRecordTelephone.text.toString())
        binding.rvRecord.layoutManager = linearLayoutManager
        binding.rvRecord.adapter = adapter
    }

    override fun addListener() {
    }

    override fun createObserver() {
        getRecordListViewModel.run {
            activity?.let {
                getRecordListLiveData.observe(it, Observer { uiState ->
                    if (uiState.isSuccess) {
                        dataList.clear()
                        uiState.recordList?.let { it1 -> dataList.addAll(it1) }
                        adapter?.submitList(dataList)
                    } else {
                        Toasty.error(it, uiState.message).show()
                    }
                })
            }
        }

        getGuestRecordListViewModel.run {
            activity?.let {
                getGuestRecordListLiveData.observe(it, Observer { uiState ->
                    if (uiState.isSuccess) {
                        val mGuestList = uiState.recordList
                        mGuestList?.let { list ->
                            //保存GuestRecord数据
                            guestList.clear()
                            guestList.addAll(list)
                            //需要从GuestRecord转换成Record
                            val newList = mutableListOf<Record>()
                            for (guestRecord in guestList) {
                                val record = Record(
                                    guestRecord.date,
                                    guestRecord.therapist,
                                    guestRecord.machine,
                                    guestRecord.part,
                                    guestRecord.knife,
                                    guestRecord.point,
                                    guestRecord.needPoint,
                                    guestRecord.currentPoint,
                                    DEFAULT_GUEST_ID
                                )
                                newList.add(record)
                            }
                            adapter?.submitList(newList)
                        }
                    } else {
                        Toasty.error(it, uiState.message).show()
                    }
                })
            }
        }

        deleteRecordViewModel.run {
            activity?.let {
                deleteRecordLiveData.observe(it, Observer { uiState ->
                    if (uiState.isSuccess) {
                        Toasty.success(it, uiState.message).show()
                        GlobalVariable.currentUser?.let { u ->
                            getRecordListViewModel.getRecordListFromLocal(u.userId)
                        }
                    } else {
                        Toasty.error(it, uiState.message).show()
                    }
                })
            }
        }

        deleteGuestRecordViewModel.run {
            activity?.let {
                deleteGuestRecordLiveData.observe(it, Observer { uiState ->
                    if (uiState.isSuccess) {
                        Toasty.success(it, uiState.message).show()
                        getGuestRecordListViewModel.getGuestRecordListFromLocal()
                    } else {
                        Toasty.error(it, uiState.message).show()
                    }
                })
            }
        }
    }

    override fun initData() {
        GlobalVariable.currentUser?.let {
            getRecordListViewModel.getRecordListFromLocal(it.userId)
        } ?: run {
            getGuestRecordListViewModel.getGuestRecordListFromLocal()
        }
    }

    override fun clickJump(position: Int) {
        GlobalVariable.currentUser?.let {
            GlobalVariable.startNum = dataList[position].point
            GlobalVariable.needNum = dataList[position].currentPoint
        } ?: {
            GlobalVariable.startNum = guestList[position].point
            GlobalVariable.needNum = guestList[position].needPoint
        }
        activity?.let {
            it.startActivity(Intent(it, AwaitActivity::class.java))
            it.finish()
        }
    }

    override fun clickDelete(position: Int) {
        val reminder = getString(R.string.dialog_delete_record)
        activity?.let {
            MaterialDialogFactory.instance.openReminderDialog(it, reminder, object : ReminderDialogClickApplyCallback {
                override fun clickApply() {
                    GlobalVariable.currentUser?.let {
                        deleteRecordViewModel.deleteRecordFromLocal(dataList[position])
                    } ?: run {
                        deleteGuestRecordViewModel.deleteRecordFromLocal(guestList[position])
                    }
                }
            })
        }
    }

    override fun clickRefresh() {
        //更新详情内容
        binding.tvRecordName.text = GlobalVariable.currentUser?.name ?: DEFAULT_GUEST_NAME
        binding.tvRecordGender.text = GlobalVariable.currentUser?.gender ?: DEFAULT_GUEST_GENDER
        binding.tvRecordBirthday.text = GlobalVariable.currentUser?.birth ?: DEFAULT_GUEST_BIRTHDAY
        binding.tvRecordTelephone.text = GlobalVariable.currentUser?.telephone ?: DEFAULT_GUEST_TELEPHONE
        binding.tvRecordEmail.text = GlobalVariable.currentUser?.email ?: DEFAULT_GUEST_EMAIL
        //重新获取数据
        initData()
    }
}