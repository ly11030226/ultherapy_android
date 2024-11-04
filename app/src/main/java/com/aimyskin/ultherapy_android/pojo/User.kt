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
    val name: String,
    val gender: String,
    @ColumnInfo(name = "register_date")
    val rDate: String,
    val birth: String,
    val telephone: String,
    val email: String,
    val therapist: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val userId: Long = 0
}

data class UserWithRecordList(
    @Embedded
    val user: User,
    @Relation(parentColumn = "user_id", entityColumn = "user_id")
    val recordList: List<Record>
) : Serializable

enum class Gender(val gender: String) {
    FEMALE("Female"),
    MALE("Male")
}
