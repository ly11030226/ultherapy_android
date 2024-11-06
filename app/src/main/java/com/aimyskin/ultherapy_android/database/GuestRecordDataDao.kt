package com.aimyskin.ultherapy_android.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.aimyskin.ultherapy_android.pojo.GuestRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface GuestRecordDataDao {
    @Transaction
    @Query("SELECT * FROM guestrecord  ORDER BY id DESC")
    fun getGuestRecordList(): Flow<List<GuestRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGuestRecord(record: GuestRecord): Long

    @Delete
    fun deleteGuestRecord(vararg record: GuestRecord)
}