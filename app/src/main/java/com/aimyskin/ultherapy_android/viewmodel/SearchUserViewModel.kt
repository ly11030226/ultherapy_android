package com.aimyskin.nodeparser.viewModel

import android.os.TokenWatcher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aimyskin.nodeparser.repository.impl.UserRepositoryImpl
import com.aimyskin.ultherapy_android.stateCallback.SearchUserUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 搜索用户 ViewModel
 */
class SearchUserViewModel : BaseViewModel() {
    val identifierSearchLiveData: MutableLiveData<SearchUserUiState> = MutableLiveData()
    private val userRepositoryImpl = UserRepositoryImpl()
    fun searchUserByTelephone(key: String) {
        viewModelScope.launch {
            userRepositoryImpl.searchUserListFromLocalByTelephone(key).flowOn(Dispatchers.IO)
                .catch { e ->
                    // 处理异常
                    e.printStackTrace()
                    identifierSearchLiveData.value = SearchUserUiState(false,"Search user error",null)
                }.collect { userList ->
                    identifierSearchLiveData.value = SearchUserUiState(true,"Search user error",userList)
                }
        }
    }
}