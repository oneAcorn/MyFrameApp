package com.github.mikephil.charting.acorn.dataset

import com.acorn.basemodule.extendfun.logI
import com.github.mikephil.charting.acorn.extendfun.calculateDistanceOf2Points
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.utils.MPPointD
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.abs

/**
 * 主要实现点击point显示对应点.
 * 因为LineDataSet假定是有序数组,而XFree不是有序数组
 * Created by acorn on 2023/4/10.
 */
class XFreeLineDataSet<T : LineDataProvider>(
    private val mChart: T,
    yVals: MutableList<Entry>?,
    lable: String
) : LineDataSet(yVals, lable) {


    override fun getEntryForXValue(xValue: Float, closestToY: Float, rounding: Rounding): Entry? {
        logI("XFreeLineDataSet $xValue,$closestToY,$rounding")
        if (xValue.isNaN()) return null
        val index = getEntryIndex(xValue, closestToY, rounding)
        if (index > -1)
            return mEntries[index]
        return null
    }

    /**
     * 返回最接近(xValue,closestToY)的点
     *
     * @param xValue
     * @param closestToY 点击位置最接近的y值
     * @param rounding 当点击的位置在两个点之间时,决定选左边右边还是最靠近的
     * @return
     */
    override fun getEntryIndex(xValue: Float, closestToY: Float, rounding: Rounding): Int {
//        logI("getEntryIndex:$xValue,$closestToY,$mEntries")
        if (mEntries == null || mEntries.isEmpty())
            return -1
        var index: Int = -1
        var minDistance = Float.NaN
//        var closestX: Float = Float.NaN
        val touchPoint = mChart.getTransformer(axisDependency).getPixelForValues(xValue, closestToY)
        val touchPixelX = touchPoint.x
        val touchPixelY = touchPoint.y
        MPPointD.recycleInstance(touchPoint)
        try { //处理ConcurrentModificationException性能消耗太大,此方法只是UI显示,所以不做处理(不想try catch可以用CopyOnWriteArrayList)
            for ((i, e) in mEntries.withIndex()) {
                val distance =
                    if (closestToY.isNaN()) {
                        abs(e.x - xValue)
                    } else {
                        val entryPoint =
                            mChart.getTransformer(axisDependency).getPixelForValues(e.x, e.y)
                        val entryPixelX = entryPoint.x
                        val entryPixelY = entryPoint.y
                        MPPointD.recycleInstance(entryPoint)
                        calculateDistanceOf2Points(
                            entryPixelX,
                            entryPixelY,
                            touchPixelX,
                            touchPixelY
                        )
                    }
//                logI("getEntryIndex loop $distance,$minDistance,$e,$clickPts")
                if (minDistance.isNaN() || distance < minDistance) {
                    minDistance = distance
                    index = i
                }
            }
        } catch (e: ConcurrentModificationException) {
            index = -1
            e.printStackTrace()
        }
        return index
    }

    /**
     * 返回x轴等于xValue的点的Entries
     *
     * @param xValue
     * @return
     */
    override fun getEntriesForXValue(xValue: Float): MutableList<Entry> {
        val targetEntries = mutableListOf<Entry>()
        if (mEntries == null || mEntries.isEmpty())
            return targetEntries
        try { //处理ConcurrentModificationException性能消耗太大,此方法只是UI显示,所以不做处理
            for (e in mEntries) {
                if (e.x == xValue) {
                    targetEntries.add(e)
                }
            }
        } catch (e: ConcurrentModificationException) {
            e.printStackTrace()
        }
        return targetEntries
    }
}