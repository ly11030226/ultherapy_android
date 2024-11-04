package com.aimyskin.ultherapy_android.stateCallback

import java.io.Serializable

data class DeleteUserUiState(
    val isSuccess: Boolean,
    val errorMessage: String
) : Serializable
