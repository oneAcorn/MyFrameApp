package com.acorn.basemodule.extendfun

import java.util.*
import kotlin.math.min

/**
 * Created by acorn on 2022/7/26.
 */

/**
 * 转换为 小时(HH):分钟(mm):秒(ss).毫秒的第100位(S)
 */
fun Long.toHHmmssS(): String {
    val totalSecond = this / 1000
    //毫秒的第一位,比如933毫秒=9
    val firstS = (this - (totalSecond * 1000))/100
    val second = totalSecond % 60
    val minute = (totalSecond / 60) % 60
    val hour = totalSecond / 3600
    return String.format("%d:%02d:%02d.%d", hour, minute, second, firstS)
}

