package com.acorn.myframeapp.ui.mpchart.fitting

import org.apache.commons.math3.stat.regression.SimpleRegression

/**
 * 线性拟合
 * Created by acorn on 2022/9/2.
 */

fun main() {
    val test = LinearFitTest()

    val arr = arrayOf(
        doubleArrayOf(1.00, 3.00),
        doubleArrayOf(2.00, 1.00),
        doubleArrayOf(3.00, 7.00),
        doubleArrayOf(4.00, 3.00),
        doubleArrayOf(5.00, -3.00),
    )
    test.regression(arr)
    println("-----------------------------------")
    test.regression(test.getPoints())
}

class LinearFitTest {

    // 已知函数 y = 2x + 3
    fun func(x: Double): Double {
        return 2 * x + 3
    }

    // 生成待拟合数据
    fun getPoints(): Array<DoubleArray> {
        val xy = Array(100) { DoubleArray(2) }
        for (x in 0..99) {
            xy[x][0] = x.toDouble() // x
            xy[x][1] = func(x.toDouble()) // y
        }
        return xy
    }

    /**
     * points[0] == x 存放 x 值
     * points[1] == y 存放 y 值
     */
    fun regression(points: Array<DoubleArray>) {
        val regression = SimpleRegression()
        regression.addData(points) // 数据集
        /*
        * RegressionResults 中是拟合的结果
        * 其中重要的几个参数如下：
        *   parameters:
        *      0: b
        *      1: k
        *   globalFitInfo
        *      0: 平方误差之和, SSE
        *      1: 平方和, SST
        *      2: R 平方, RSQ
        *      3: 均方误差, MSE
        *      4: 调整后的 R 平方, adjRSQ
        *
        * */
        val results = regression.regress()
        println("b = " + results.getParameterEstimate(0))
        println("k = " + results.getParameterEstimate(1))
        println("r^2 = " + results.rSquared)
        println("adjusted r^2 = " + results.adjustedRSquared)
    }
}