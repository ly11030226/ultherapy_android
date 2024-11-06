package com.aimyskin.ultherapy_android.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.aimyskin.ultherapy_android.pojo.Record
import com.aimyskin.ultherapy_android.pojo.User
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDataDao {
    @Transaction
    @Query("SELECT * FROM record  WHERE user_id=:userId ORDER BY user_id DESC")
    fun getRecordList(userId: Long): Flow<List<Record>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecord(record: Record): Long

    @Delete
    fun deleteRecord(vararg record: Record)

    @Query("DELETE FROM RECORD WHERE user_id=:userId")
    fun deleteRecordsByUserId(userId:Long)
}