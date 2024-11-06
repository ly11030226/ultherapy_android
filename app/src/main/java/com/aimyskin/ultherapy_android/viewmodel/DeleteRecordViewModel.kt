package com.aimyskin.ultherapy_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aimyskin.ultherapy_android.pojo.Record
import com.aimyskin.ultherapy_android.repository.impl.RecordRepositoryImpl
import com.aimyskin.ultherapy_android.stateCallback.DeleteRecordUiState
import com.aimyskin.ultherapy_android.stateCallback.DeleteUserUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeleteRecordViewModel : BaseViewModel() {
    val deleteRecordLiveData: MutableLiveData<DeleteRecordUiState> = MutableLiveData()
    private val recordRepository = RecordRepositoryImpl()
    fun deleteRecordFromLocal(record: Record) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    recordRepository.deleteRecord(record)
                    deleteRecordLiveData.postValue(DeleteRecordUiState(true, "Delete record success"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                deleteRecordLiveData.postValue(DeleteRecordUiState(false, "Delete record error"))
            }
        }
    }
}