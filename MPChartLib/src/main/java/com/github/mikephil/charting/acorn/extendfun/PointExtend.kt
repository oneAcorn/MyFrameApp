package com.github.mikephil.charting.acorn.extendfun

import com.github.mikephil.charting.data.Entry
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Created by acorn on 2023/4/10.
 */

fun calculateDistanceOf2Points(pts1: Entry, pts2: Entry): Float {
    val aSideLen = abs(pts2.y - pts1.y)
    val bSideLen = abs(pts2.x - pts1.x)
    return calculateSideLenInRightTriangle(aSideLen, bSideLen)
}

/**
 * 给定直角三角形中两条直角边的边长,计算最长边的长度
 *
 */
private fun calculateSideLenInRightTriangle(a: Float, b: Float): Float {
    return sqrt((a * a + b * b).toDouble()).toFloat()
}