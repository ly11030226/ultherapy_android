package com.aimyskin.ultherapy_android.stateCallback

import java.io.Serializable

data class AddUserUiState(
    val isSuccess: Boolean,
    val errorMessage: String
) : Serializable

