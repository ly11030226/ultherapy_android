package com.aimyskin.ultherapy_android

import com.chibatching.kotpref.KotprefModel

object Profile : KotprefModel() {
    var username: String by stringPref()
    var password: String by stringPref()

    //是否开启了自动感应
    var isAutoRecognition: Boolean by booleanPref(false)

    //是否开启了摘机动画
    var isHaveAnimation: Boolean by booleanPref(true)

    //记录每个刀头打点的数
    var knife15: Int by intPref(0)
    var knife20: Int by intPref(0)
    var knife30: Int by intPref(0)
    var knife45: Int by intPref(0)
    var knife60: Int by intPref(0)
    var knife90: Int by intPref(0)
    var knife130: Int by intPref(0)
    var circle15: Int by intPref(0)
    var circle30: Int by intPref(0)
    var circle45: Int by intPref(0)

    //保存修改后的能量系数
    var energy: Int by intPref(100)
}