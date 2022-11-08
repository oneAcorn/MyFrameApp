package com.acorn.myframeapp.ui.chart.mpchart

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.acorn.basemodule.extendfun.dp
import com.acorn.basemodule.network.MyException
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description

/**
 * Created by acorn on 2022/7/8.
 */
class MyLineChart : LineChart {
    private var bottomAxisDescription: Description? = null
    private var leftAxisDescription: Description? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    override fun drawDescription(c: Canvas) {
        bottomAxisDescription?.let {
            val positionX = width - mViewPortHandler.offsetRight() - mDescription.xOffset
            val positionY = height - mViewPortHandler.offsetBottom() - mDescription.yOffset
            it.setPosition(positionX, positionY)
            drawDescription(it, c)
        }
        leftAxisDescription?.let {
            val positionX = mViewPortHandler.offsetLeft() + mDescription.xOffset + 10.dp
            val positionY = mViewPortHandler.offsetTop() + mDescription.yOffset
            it.setPosition(positionX, positionY)
            drawDescription(it, c)
        }
    }

    private fun drawDescription(desc: Description, c: Canvas) {
        if (!desc.isEnabled)
            return

        val position = desc.position

        mDescPaint.typeface = desc.typeface
        mDescPaint.textSize = desc.textSize
        mDescPaint.color = desc.textColor
        mDescPaint.textAlign = desc.textAlign

        c.drawText(desc.text, position.x, position.y, mDescPaint)
    }

    fun setBottomAxisDescription(desc: Description?) {
        this.bottomAxisDescription = desc
    }

    fun setLeftAxisDescription(desc: Description?) {
        this.leftAxisDescription = desc
    }

    override fun setDescription(desc: Description?) {
        throw MyException("pls use xxxAxisDescription")
    }

    override fun getDescription(): Description {
        throw MyException("pls use xxxAxisDescription")
    }
}