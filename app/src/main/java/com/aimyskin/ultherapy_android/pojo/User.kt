package com.aimyskin.ultherapy_android.pojo

data class User(
    val name: String
)

enum class Gender(val gender: String) {
    FEMALE("Female") ,
    MALE("Male")
}
