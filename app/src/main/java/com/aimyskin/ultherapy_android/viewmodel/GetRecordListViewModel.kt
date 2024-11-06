package com.aimyskin.ultherapy_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aimyskin.ultherapy_android.repository.impl.RecordRepositoryImpl
import com.aimyskin.ultherapy_android.stateCallback.GetRecordListUiState
import com.aimyskin.ultherapy_android.stateCallback.GetUserListUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class GetRecordListViewModel : BaseViewModel() {
    val getRecordListLiveData: MutableLiveData<GetRecordListUiState> = MutableLiveData()
    private val recordRepository = RecordRepositoryImpl()

    /**
     * 从本地查询user列表
     */
    fun getRecordListFromLocal(userId: Long) {
        viewModelScope.launch {
            recordRepository.getRecordListFromLocalByUserId(userId).flowOn(Dispatchers.IO)
                .catch { e ->
                    // 处理异常
                    e.printStackTrace()
                    getRecordListLiveData.postValue(GetRecordListUiState(false, "Get record list error", null))
                }.collect { recordList ->
                    getRecordListLiveData.postValue(GetRecordListUiState(true, "Get record list success", recordList))
                }
        }
    }
}



