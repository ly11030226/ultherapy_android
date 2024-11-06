package com.aimyskin.ultherapy_android.repository.impl

import com.aimyskin.ultherapy_android.database.AppDatabase
import com.aimyskin.ultherapy_android.pojo.GuestRecord
import com.aimyskin.ultherapy_android.pojo.User
import com.aimyskin.ultherapy_android.pojo.UserWithRecordList
import com.aimyskin.ultherapy_android.repository.GuestRecordRepository
import com.aimyskin.ultherapy_android.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.Locale

class GuestRecordRepositoryImpl : GuestRecordRepository {
    override suspend fun addGuestRecord(record: GuestRecord) {
        AppDatabase.instance.guestRecordDataDao().insertGuestRecord(record)

    }

    override suspend fun deleteGuestRecord(record: GuestRecord) {
        AppDatabase.instance.guestRecordDataDao().deleteGuestRecord(record)
    }

    override suspend fun getGuestRecordListFromLocal(): Flow<List<GuestRecord>> {
        return AppDatabase.instance.guestRecordDataDao().getGuestRecordList()

    }

}