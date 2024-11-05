package com.aimyskin.ultherapy_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aimyskin.nodeparser.repository.impl.UserRepositoryImpl
import com.aimyskin.ultherapy_android.stateCallback.GetUserListUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class GetUserListViewModel : BaseViewModel() {
    val getUserListLiveData: MutableLiveData<GetUserListUiState> = MutableLiveData()
    private val userRepositoryImpl = UserRepositoryImpl()

    /**
     * 从本地查询user列表
     */
    fun getUserListFromLocal(limit: Int, offset: Int) {
        viewModelScope.launch {
            userRepositoryImpl.getUserListFromLocal(limit, offset).flowOn(Dispatchers.IO)
                .catch { e ->
                    // 处理异常
                    e.printStackTrace()
                    getUserListLiveData.postValue(GetUserListUiState(false, "Get user list error", null))
                }.collect { userList ->
                    getUserListLiveData.postValue(GetUserListUiState(true, "Get user list success", userList))
                }
        }
    }
}



