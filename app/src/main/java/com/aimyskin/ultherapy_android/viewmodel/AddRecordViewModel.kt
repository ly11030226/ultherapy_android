package com.aimyskin.ultherapy_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aimyskin.ultherapy_android.pojo.Record
import com.aimyskin.ultherapy_android.repository.RecordRepository
import com.aimyskin.ultherapy_android.repository.impl.RecordRepositoryImpl
import com.aimyskin.ultherapy_android.stateCallback.AddRecordUiState
import com.aimyskin.ultherapy_android.stateCallback.AddUserUiState
import com.aimyskin.ultherapy_android.stateCallback.DeleteUserUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddRecordViewModel : BaseViewModel() {
    val addRecordLiveData: MutableLiveData<AddRecordUiState> = MutableLiveData()
    private val recordRepository = RecordRepositoryImpl()
    fun addRecordToLocal(record: Record) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    recordRepository.addRecord(record)
                    addRecordLiveData.postValue(AddRecordUiState(true, "Add record success"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                addRecordLiveData.postValue(AddRecordUiState(false, "Add record error"))
            }
        }
    }
}