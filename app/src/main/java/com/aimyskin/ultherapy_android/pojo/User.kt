package com.aimyskin.ultherapy_android.pojo

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable
import java.util.Date

/**
 * 用户表实体类
 */
@Entity
data class User(
    var name: String,
    var gender: String,
    @ColumnInfo(name = "register_date")
    var rDate: String,
    var birth: String,
    var telephone: String,
    var email: String,
    var therapist: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var userId: Long = 0
}

data class UserWithRecordList(
    @Embedded
    var user: User,
    @Relation(parentColumn = "user_id", entityColumn = "user_id")
    var recordList: List<Record>
) : Serializable

enum class Gender(val gender: String) {
    FEMALE("Female"),
    MALE("Male")
}
