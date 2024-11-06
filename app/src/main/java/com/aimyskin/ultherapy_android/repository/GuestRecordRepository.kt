package com.aimyskin.ultherapy_android.repository

import com.aimyskin.ultherapy_android.pojo.GuestRecord
import com.aimyskin.ultherapy_android.pojo.UserWithRecordList
import kotlinx.coroutines.flow.Flow

interface GuestRecordRepository {
    suspend fun addGuestRecord(record: GuestRecord)
    suspend fun deleteGuestRecord(record: GuestRecord)
    suspend fun getGuestRecordListFromLocal(): Flow<List<GuestRecord>>
}