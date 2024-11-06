package com.aimyskin.ultherapy_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aimyskin.ultherapy_android.pojo.GuestRecord
import com.aimyskin.ultherapy_android.pojo.Record
import com.aimyskin.ultherapy_android.repository.RecordRepository
import com.aimyskin.ultherapy_android.repository.impl.GuestRecordRepositoryImpl
import com.aimyskin.ultherapy_android.repository.impl.RecordRepositoryImpl
import com.aimyskin.ultherapy_android.stateCallback.AddRecordUiState
import com.aimyskin.ultherapy_android.stateCallback.AddUserUiState
import com.aimyskin.ultherapy_android.stateCallback.DeleteUserUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddGuestRecordViewModel : BaseViewModel() {
    val addGuestRecordLiveData: MutableLiveData<AddRecordUiState> = MutableLiveData()
    private val recordRepository = GuestRecordRepositoryImpl()
    fun addRecordToLocal(record: GuestRecord) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    recordRepository.addGuestRecord(record)
                    addGuestRecordLiveData.postValue(AddRecordUiState(true, "Add guest record success"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                addGuestRecordLiveData.postValue(AddRecordUiState(false, "Add guest record error"))
            }
        }
    }
}