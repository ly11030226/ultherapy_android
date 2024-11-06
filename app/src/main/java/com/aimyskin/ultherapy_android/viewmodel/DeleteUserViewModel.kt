package com.aimyskin.ultherapy_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aimyskin.ultherapy_android.pojo.User
import com.aimyskin.ultherapy_android.repository.impl.RecordRepositoryImpl
import com.aimyskin.ultherapy_android.repository.impl.UserRepositoryImpl
import com.aimyskin.ultherapy_android.stateCallback.DeleteUserUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeleteUserViewModel : BaseViewModel() {
    val deleteUserLiveData: MutableLiveData<DeleteUserUiState> = MutableLiveData()
    private val userRepository = UserRepositoryImpl()
    private val recordRepositoryImpl = RecordRepositoryImpl()

    fun deleteUserFromLocal(user: User) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    userRepository.deleteUser(user)
                    //删除用户的同时 也删除该用户下的记录
                    recordRepositoryImpl.deleteRecordsByUserId(user.userId)
                    deleteUserLiveData.postValue(DeleteUserUiState(true, "Delete user success"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                deleteUserLiveData.postValue(DeleteUserUiState(false, "Delete user error"))
            }
        }
    }
}