package com.aimyskin.ultherapy_android.stateCallback

import com.aimyskin.ultherapy_android.pojo.GuestRecord
import com.aimyskin.ultherapy_android.pojo.Record
import java.io.Serializable

/**
 * 获取用户列表
 */
data class GetGuestRecordListUiState(
    val isSuccess: Boolean,
    val message: String,
    var recordList: List<GuestRecord>?
) : Serializable
