package com.aimyskin.ultherapy_android.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aimyskin.ultherapy_android.DATABASE_NAME
import com.aimyskin.ultherapy_android.MyApplication
import com.aimyskin.ultherapy_android.database.GuestRecordDataDao
import com.aimyskin.ultherapy_android.database.RecordDataDao
import com.aimyskin.ultherapy_android.pojo.GuestRecord
import com.aimyskin.ultherapy_android.pojo.Record
import com.aimyskin.ultherapy_android.pojo.User

@Database(
    entities = [
        User::class,
        Record::class,
        GuestRecord::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDataDao(): UserDataDao
    abstract fun recordDataDao(): RecordDataDao
    abstract fun guestRecordDataDao(): GuestRecordDataDao

    companion object {
        val instance = Room.databaseBuilder(MyApplication.INSTANCE, AppDatabase::class.java, DATABASE_NAME).build()
    }
}
