package com.aimyskin.ultherapy_android.stateCallback

import com.aimyskin.ultherapy_android.pojo.User
import java.io.Serializable

/**
 * 获取用户列表
 */
data class GetUserListUiState(
    val isSuccess: Boolean,
    val errorMessage: String,
    var userList: List<User>?
) : Serializable
