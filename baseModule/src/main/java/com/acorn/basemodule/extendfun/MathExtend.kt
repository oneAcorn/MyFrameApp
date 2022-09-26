package com.acorn.basemodule.extendfun

import android.graphics.PointF
import com.acorn.basemodule.utils.TransUtil
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.pow

/**
 * Created by acorn on 2022/7/6.
 */
fun getDecimalFormat(decimalCount: Int): DecimalFormat {
    val sb = StringBuilder("#")
    if (decimalCount > 0) {
        sb.append(".")
        for (i in 0 until decimalCount) {
            sb.append('#')
        }
    }
//    println(sb.toString())
    return DecimalFormat(sb.toString())
}

/**
 * 根据角度angle获取在此角与圆心连线中距离圆心为distanceToCenter远的点
 *
 * @param angle            以圆心为起点垂直向上作边a,与此边a按顺时针方向的夹角
 * @param distanceToCenter 距离圆心的距离
 */
fun getPointByAngle(angle: Float, cx: Float, cy: Float, distanceToCenter: Int): PointF {
    val x1: Double
    val y1: Double
    //角度转成弧度
    val radians: Double = TransUtil.angle2radians(angle)
    x1 = cx + distanceToCenter * Math.cos(radians)
    y1 = cy + distanceToCenter * Math.sin(radians)
    return PointF(x1.toFloat(), y1.toFloat())
}

/**
 * 对一些如points=2时,0.0003212做特殊处理,处理结果为0.00032而不是0.00
 * @param points 保留小数点位数
 */
fun Float.formatCompatSmallValue(points: Int): String{
    return this.toDouble().formatCompatSmallValue(points)
}

/**
 * 对一些如points=2时,0.0003212做特殊处理,处理结果为0.00032而不是0.00
 * @param points 保留小数点位数
 */
fun Double.formatCompatSmallValue(points: Int): String {
    var decimalCount = points
    val minValue = 0.1.pow(points)
    if (abs(this) < minValue) {
        //比如points=2,this=0.000921.则需要保留到0.00092
        var nonzeroPosition = -1
        val str = this.toBigDecimal().toString()
        val charArr = str.toCharArray()
        val length = charArr.size
        val start = str.indexOf('.')
        if (start >= 0) {
            for (i in start + 1 until length) {
                val c = charArr[i]
                if (c != '0') {
                    //保留有值部分+points的位数
                    nonzeroPosition = i - start + points - 1
                    break
                }
            }
        }
        if (nonzeroPosition != -1) {
            decimalCount = nonzeroPosition
        }
    }
    return getDecimalFormat(decimalCount).format(this)
}