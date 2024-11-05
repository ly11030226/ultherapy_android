package com.aimyskin.ultherapy_android.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * 记录表实体类
 */
@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["user_id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("user_id")]
)
data class Record(
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
    @ColumnInfo(name = "user_id")
    var userId:Long
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "record_id")
    var recordId: Long = 0
}
