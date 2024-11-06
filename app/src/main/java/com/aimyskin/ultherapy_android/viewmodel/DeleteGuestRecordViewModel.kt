package com.aimyskin.ultherapy_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aimyskin.ultherapy_android.pojo.GuestRecord
import com.aimyskin.ultherapy_android.pojo.Record
import com.aimyskin.ultherapy_android.repository.impl.GuestRecordRepositoryImpl
import com.aimyskin.ultherapy_android.repository.impl.RecordRepositoryImpl
import com.aimyskin.ultherapy_android.stateCallback.DeleteRecordUiState
import com.aimyskin.ultherapy_android.stateCallback.DeleteUserUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeleteGuestRecordViewModel : BaseViewModel() {
    val deleteGuestRecordLiveData: MutableLiveData<DeleteRecordUiState> = MutableLiveData()
    private val recordRepository = GuestRecordRepositoryImpl()
    fun deleteRecordFromLocal(record: GuestRecord) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    recordRepository.deleteGuestRecord(record)
                    deleteGuestRecordLiveData.postValue(DeleteRecordUiState(true, "Delete guest record success"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                deleteGuestRecordLiveData.postValue(DeleteRecordUiState(false, "Delete guest record error"))
            }
        }
    }
}