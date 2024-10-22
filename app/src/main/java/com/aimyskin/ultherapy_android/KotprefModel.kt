package com.aimyskin.ultherapy_android

import com.chibatching.kotpref.KotprefModel

object Profile : KotprefModel() {
    var username: String by stringPref()
    var password: String by stringPref()
    //是否开启了自动感应
    var isAutoRecognition: Boolean by booleanPref(false)
    //是否开启了摘机动画
    var isHaveAnimation: Boolean by booleanPref(true)
}