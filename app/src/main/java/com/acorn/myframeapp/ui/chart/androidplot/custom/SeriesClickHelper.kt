package com.acorn.myframeapp.ui.chart.androidplot.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.acorn.basemodule.extendfun.logI
import com.acorn.basemodule.extendfun.showToast
import com.acorn.basemodule.extendfun.showToastAndLog
import com.androidplot.xy.LineAndPointRenderer
import com.androidplot.xy.PanZoom
import com.androidplot.xy.XYPlot
import kotlinx.android.synthetic.main.activity_plot_line_chart.*
import kotlin.math.abs

/**
 * Created by acorn on 2022/11/9.
 */
class SeriesClickHelper(context: Context) : GestureDetector.OnGestureListener {
    private val gestureDetector = GestureDetectorCompat(context, this)
    private lateinit var plot: XYPlot

    companion object {
        //点击有效区域
        private const val clickAreaThreshold = 40f
    }

    @SuppressLint("ClickableViewAccessibility")
    fun attachPlot(plot: XYPlot, panZoom: PanZoom) {
        this.plot = plot
        panZoom.setDelegate { view, event ->
            return@setDelegate gestureDetector.onTouchEvent(event)
        }
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onSingleTapUp(event: MotionEvent?): Boolean {
        event ?: return false
        val seriesList = plot.registry.seriesList
        val lineRenderer = plot.getRenderer(LineAndPointRenderer::class.java)
        for (series in seriesList) {
            //公式无法点击
            if(series is MyXYSeries && series.seriesType==PlotSeriesType.Formula) continue
            val cachePoints = lineRenderer.getCurrentPointsCache(series) ?: continue
//            showToastAndLog("tap (${event.x},${event.y})")
            var clickPointIndex = -1
            for ((i, pointF) in cachePoints.withIndex()) {
                if (event.isInClickArea(pointF)) {
                    clickPointIndex = i
                    break
                }
            }
            if (clickPointIndex >= 0) {
                showToastAndLog("tap ${series.getX(clickPointIndex)},${series.getY(clickPointIndex)}")
            }
        }
        return true
    }

    private fun MotionEvent.isInClickArea(targetPoint: PointF): Boolean {
        return abs(x - targetPoint.x) < clickAreaThreshold && abs(y - targetPoint.y) < clickAreaThreshold
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }
}