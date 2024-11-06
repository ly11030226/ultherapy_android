package com.aimyskin.ultherapy_android.repository.impl

import com.aimyskin.ultherapy_android.database.AppDatabase
import com.aimyskin.ultherapy_android.pojo.User
import com.aimyskin.ultherapy_android.pojo.UserWithRecordList
import com.aimyskin.ultherapy_android.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Locale

class UserRepositoryImpl : UserRepository {
    override suspend fun addUser(user: User) {
        AppDatabase.instance.userDataDao().insertUser(user)
    }

    override suspend fun deleteUser(user: User) {
        AppDatabase.instance.userDataDao().deleteUser(user)
    }

    override suspend fun getUserListFromLocal(limit:Int,offset:Int): Flow<List<User>> {
        return AppDatabase.instance.userDataDao().getUserList(limit,offset)
    }

    override suspend fun searchUserListFromLocalByTelephone(telephone: String): Flow<List<User>> {
        return AppDatabase.instance.userDataDao().searchUserListFromLocalByTelephone(telephone)
    }


}