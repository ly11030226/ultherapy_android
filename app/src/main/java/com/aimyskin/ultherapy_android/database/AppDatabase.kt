package com.aimyskin.nodeparser.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aimyskin.ultherapy_android.DATABASE_NAME
import com.aimyskin.ultherapy_android.MyApplication
import com.aimyskin.ultherapy_android.pojo.Record
import com.aimyskin.ultherapy_android.pojo.User

@Database(
    entities = [
        User::class,
        Record::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDataDao(): UserDataDao

    companion object {
        val instance =
            Room.databaseBuilder(MyApplication.INSTANCE, AppDatabase::class.java, DATABASE_NAME)
                .build()
    }
}
