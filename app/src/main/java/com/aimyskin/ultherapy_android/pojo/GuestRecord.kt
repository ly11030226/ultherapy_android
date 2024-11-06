package com.aimyskin.ultherapy_android.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * 用來保存游客身份进行的打点记录
 */
@Entity
data class GuestRecord(
    var date: String,
    var therapist: String? = null,
    var machine: String? = null,
    var part: String? = null,
    var knife: String,
    var point: Int,
    @ColumnInfo(name = "need_point")
    var needPoint: Int,
    @ColumnInfo(name = "current_point")
    var currentPoint: Int,
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}