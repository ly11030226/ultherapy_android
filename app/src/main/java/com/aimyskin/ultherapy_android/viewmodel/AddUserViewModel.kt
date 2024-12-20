package com.aimyskin.ultherapy_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aimyskin.ultherapy_android.pojo.User
import com.aimyskin.ultherapy_android.repository.impl.UserRepositoryImpl
import com.aimyskin.ultherapy_android.stateCallback.AddUserUiState
import com.aimyskin.ultherapy_android.stateCallback.DeleteUserUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddUserViewModel : BaseViewModel() {
    val addUserLiveData: MutableLiveData<AddUserUiState> = MutableLiveData()
    private val userRepository = UserRepositoryImpl()
    fun addUserToLocal(user: User) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    userRepository.addUser(user)
                    addUserLiveData.postValue(AddUserUiState(true, "Add user success"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                addUserLiveData.postValue(AddUserUiState(false, "Add user error"))
            }
        }
    }
}