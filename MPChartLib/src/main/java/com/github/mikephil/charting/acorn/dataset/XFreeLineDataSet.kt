package com.github.mikephil.charting.acorn.dataset

import com.acorn.basemodule.extendfun.logI
import com.github.mikephil.charting.acorn.extendfun.calculateDistanceOf2Points
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlin.math.abs

/**
 * 主要实现点击point显示对应点.
 * 因为LineDataSet假定是有序数组,而XFree不是有序数组
 * Created by acorn on 2023/4/10.
 */
class XFreeLineDataSet(yVals: MutableList<Entry>?, lable: String) : LineDataSet(yVals, lable) {


    override fun getEntryForXValue(xValue: Float, closestToY: Float, rounding: Rounding): Entry? {
//        logI("XFreeLineDataSet $xValue,$closestToY,$rounding")
        if (xValue.isNaN()) return null
        val index = getEntryIndex(xValue, closestToY, rounding)
        if (index > -1)
            return mEntries[index]
        return null
    }

    /**
     * Get entry index
     *
     * @param xValue
     * @param closestToY 点击位置最接近的y值
     * @param rounding 当点击的位置在两个点之间时,决定选左边右边还是最靠近的
     * @return
     */
    override fun getEntryIndex(xValue: Float, closestToY: Float, rounding: Rounding): Int {
        if (mEntries == null || mEntries.isEmpty())
            return -1
        var index: Int = -1
        var minDistance = Float.NaN
//        var closestX: Float = Float.NaN
        val clickPts = Entry(xValue, closestToY)
        for ((i, e) in mEntries.withIndex()) {
            val distance =
                if (closestToY.isNaN()) {
                    abs(e.x - xValue)
                } else {
                    calculateDistanceOf2Points(e, clickPts)
                }
            if (minDistance.isNaN() || distance < minDistance) {
                minDistance = distance
                index = i
            }
        }
        return index
    }

    /**
     * Get entries for x value
     *
     * @param xValue
     * @return
     */
    override fun getEntriesForXValue(xValue: Float): MutableList<Entry> {
        val targetEntries = mutableListOf<Entry>()
        if (mEntries == null || mEntries.isEmpty())
            return targetEntries
        for (e in mEntries) {
            if (e.x == xValue) {
                targetEntries.add(e)
            }
        }
        return targetEntries
    }
}