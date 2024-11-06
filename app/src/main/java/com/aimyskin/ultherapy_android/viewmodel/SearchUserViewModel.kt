package com.aimyskin.ultherapy_android.viewmodel
import android.os.TokenWatcher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aimyskin.ultherapy_android.repository.impl.UserRepositoryImpl
import com.aimyskin.ultherapy_android.stateCallback.SearchUserUiState
import com.aimyskin.ultherapy_android.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 搜索用户 ViewModel
 */
class SearchUserViewModel : BaseViewModel() {
    val searchUserLiveData: MutableLiveData<SearchUserUiState> = MutableLiveData()
    private val userRepositoryImpl = UserRepositoryImpl()
    fun searchUserByTelephone(key: String) {
        viewModelScope.launch {
            userRepositoryImpl.searchUserListFromLocalByTelephone(key).flowOn(Dispatchers.IO)
                .catch { e ->
                    // 处理异常
                    e.printStackTrace()
                    searchUserLiveData.postValue(SearchUserUiState(false, "Search user error", null))
                }.collect { userList ->
                    searchUserLiveData.postValue(SearchUserUiState(true, "Search user error", userList))
                }
        }
    }
}