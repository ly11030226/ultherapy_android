package com.aimyskin.ultherapy_android.repository.impl

import com.aimyskin.ultherapy_android.database.AppDatabase
import com.aimyskin.ultherapy_android.pojo.Record
import com.aimyskin.ultherapy_android.pojo.User
import com.aimyskin.ultherapy_android.pojo.UserWithRecordList
import com.aimyskin.ultherapy_android.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Locale

class RecordRepositoryImpl : RecordRepository {
    override suspend fun addRecord(record: Record) {
        AppDatabase.instance.recordDataDao().insertRecord(record)

    }

    override suspend fun deleteRecord(record: Record) {
        AppDatabase.instance.recordDataDao().deleteRecord(record)
    }

    override suspend fun getRecordListFromLocalByUserId(userId: Long): Flow<List<Record>> {
        return AppDatabase.instance.recordDataDao().getRecordList(userId)
    }

}