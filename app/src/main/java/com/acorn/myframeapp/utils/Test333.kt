package com.acorn.myframeapp.utils

/**
 * Created by acorn on 2024/3/8.
 */

fun main() {
    GetArcCenter(0.0,4.0,4.0,0.0,4.0)
}

fun GetArcCenter(x1: Double, y1: Double, x2: Double, y2: Double, radius: Double) {
    var CenterX = 0.0
    var CenterY = 0.0
    val CenterList: List<String> = ArrayList()
    val c1 = (x2 * x2 - x1 * x1 + y2 * y2 - y1 * y1) / (2 * (x2 - x1))
    val c2 = (y2 - y1) / (x2 - x1) //斜率
    val A = c2 * c2 + 1
    val B = 2 * x1 * c2 - 2 * c1 * c2 - 2 * y1
    val C = x1 * x1 - 2 * x1 * c1 + c1 * c1 + y1 * y1 - radius * radius
    CenterY = (-B + Math.sqrt(B * B - 4 * A * C)) / (2 * A)
    CenterX = c1 - c2 * CenterY
    println("圆心$CenterX,$CenterY")
}