package com.aimyskin.nodeparser.database

import androidx.room.TypeConverter
import java.sql.Date
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * 处理时间格式的数据与String转换的类
 * 利于数据持久化
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}