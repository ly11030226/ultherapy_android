package com.aimyskin.ultherapy_android.stateCallback

import com.aimyskin.ultherapy_android.pojo.User
import java.io.Serializable

/**
 * 通过手机号搜索用户列表
 */
data class SearchUserUiState(
    val isSuccess: Boolean,
    val errorMessage: String,
    var userList: List<User>?
) : Serializable
