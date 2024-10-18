package com.aimyskin.ultherapy_android
import com.chibatching.kotpref.KotprefModel

object Profile : KotprefModel() {
    var username: String by stringPref()
    var password: String by stringPref()
}