package com.aimyskin.ultherapy_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aimyskin.ultherapy_android.repository.impl.GuestRecordRepositoryImpl
import com.aimyskin.ultherapy_android.repository.impl.RecordRepositoryImpl
import com.aimyskin.ultherapy_android.stateCallback.GetGuestRecordListUiState
import com.aimyskin.ultherapy_android.stateCallback.GetRecordListUiState
import com.aimyskin.ultherapy_android.stateCallback.GetUserListUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class GetGuestRecordListViewModel : BaseViewModel() {
    val getGuestRecordListLiveData: MutableLiveData<GetGuestRecordListUiState> = MutableLiveData()
    private val recordRepository = GuestRecordRepositoryImpl()

    fun getGuestRecordListFromLocal() {
        viewModelScope.launch {
            recordRepository.getGuestRecordListFromLocal().flowOn(Dispatchers.IO)
                .catch { e ->
                    // 处理异常
                    e.printStackTrace()
                    getGuestRecordListLiveData.postValue(GetGuestRecordListUiState(false, "Get guest record list error", null))
                }.collect { recordList ->
                    getGuestRecordListLiveData.postValue(GetGuestRecordListUiState(true, "Get guest record list success", recordList))
                }
        }
    }
}



