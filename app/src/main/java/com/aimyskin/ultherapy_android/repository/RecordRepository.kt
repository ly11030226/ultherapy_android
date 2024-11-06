package com.aimyskin.ultherapy_android.repository

import com.aimyskin.ultherapy_android.pojo.Record
import com.aimyskin.ultherapy_android.pojo.UserWithRecordList
import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    suspend fun addRecord(record: Record)
    suspend fun deleteRecord(record: Record)
    suspend fun getRecordListFromLocalByUserId(userId: Long): Flow<List<Record>>
}