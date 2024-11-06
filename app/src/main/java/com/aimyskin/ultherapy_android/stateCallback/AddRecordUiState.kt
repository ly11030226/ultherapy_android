package com.aimyskin.ultherapy_android.stateCallback

import java.io.Serializable

data class AddRecordUiState(
    val isSuccess: Boolean,
    val message: String
) : Serializable

