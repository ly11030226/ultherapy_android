package com.aimyskin.nodeparser.repository

import com.aimyskin.ultherapy_android.pojo.User
import com.aimyskin.ultherapy_android.pojo.UserWithRecordList
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun getUserListFromLocal(limit: Int, offset: Int): Flow<List<User>>
    suspend fun searchUserListFromLocalByTelephone(telephone: String): Flow<List<User>>
}