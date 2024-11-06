package com.aimyskin.ultherapy_android.stateCallback

import com.aimyskin.ultherapy_android.pojo.Record
import java.io.Serializable

/**
 * 获取用户列表
 */
data class GetRecordListUiState(
    val isSuccess: Boolean,
    val message: String,
    var recordList: List<Record>?
) : Serializable
