package com.acorn.myframeapp.ui.chart.androidplot.series

import android.graphics.PointF
import com.androidplot.xy.XYSeries

/**
 * Created by acorn on 2022/11/8.
 */
class DynamicSeries(private val mTitle: String? = null, var list: MutableList<PointF>? = null) :
    XYSeries {
    override fun getTitle(): String = mTitle ?: ""

    override fun size(): Int {
        return list?.size ?: 0
    }

    override fun getX(index: Int): Number {
        return list?.takeIf { index < it.size }?.get(index)?.x ?: 0f
    }

    override fun getY(index: Int): Number {
        return list?.takeIf { index < it.size }?.get(index)?.y ?: 0f
    }
}