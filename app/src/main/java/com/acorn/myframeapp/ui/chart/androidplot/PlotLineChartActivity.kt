package com.acorn.myframeapp.ui.chart.androidplot

import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Bundle
import com.acorn.basemodule.extendfun.dp
import com.acorn.basemodule.extendfun.logI
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.acorn.myframeapp.ui.chart.androidplot.custom.SeriesClickHelper
import com.androidplot.util.PixelUtils
import com.androidplot.xy.*
import kotlinx.android.synthetic.main.activity_plot_line_chart.*
import java.text.DecimalFormat
import kotlin.random.Random

/**
 * Created by acorn on 2022/11/8.
 */
class PlotLineChartActivity : BaseNoViewModelActivity() {
    //缩放
    private lateinit var panZoom: PanZoom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plot_line_chart)
    }

    override fun initView() {
        super.initView()
        initPlot()
    }

    override fun initListener() {
        super.initListener()
        testBtn.singleClick {
            appendData(Random.nextFloat() * 20f, Random.nextFloat() * 50f)
            plot.redraw()
//            setData()
        }
        resetPositionBtn.singleClick {
            // Setup the boundary mode, boundary values only applicable in FIXED mode.
            plot.setRangeBoundaries(0, 0, BoundaryMode.AUTO)
            plot.setDomainBoundaries(0, 0, BoundaryMode.AUTO)
            plot.redraw()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("pan-zoom-state", panZoom.state)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        panZoom.state = savedInstanceState.getSerializable("pan-zoom-state") as PanZoom.State
        plot.redraw()
    }

    private fun appendData(x: Float, y: Float) {
        showTip("append:$x,$y")
        logI("append:$x,$y")
        val seriesList = plot.registry.seriesList
        val series = if (seriesList == null || seriesList.isEmpty()) {
//            val dynamicSeries = DynamicSeries()
            val dynamicSeries = SimpleXYSeries("test1")
//            val formatter1 = LineAndPointFormatter(
//                Color.rgb(0, 200, 0), null, null, null
//            )
            val formatter1 = LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels)
            plot.addSeries(dynamicSeries, formatter1)
            dynamicSeries
        } else {
            seriesList[0] as SimpleXYSeries
        }
//        if (series.list?.isNotEmpty() != true) {
//            series.list = mutableListOf()
//        }
//        series.list?.add(PointF(x, y))
        series.addLast(x, y)
    }


    private fun initPlot() {
        //设置x轴数值的DecimalFormat
        // only display whole numbers in domain labels
        plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = DecimalFormat("0")

        //设置线的样式
//        val formatter1 = LineAndPointFormatter(
//            Color.rgb(0, 200, 0), null, null, null
//        )
//        formatter1.linePaint.strokeJoin = Paint.Join.ROUND
//        formatter1.linePaint.strokeWidth = 2f.dp

        //x轴固定5个值
        // thin out domain tick labels so they dont overlap each other:
        plot.domainStepMode = StepMode.SUBDIVIDE
        plot.domainStepValue = 5.0

        //y轴固定10个值
        plot.rangeStepMode = StepMode.SUBDIVIDE
        plot.rangeStepValue = 10.0

        //左侧y轴DecimalFormat
        plot.graph.getLineLabelStyle(
            XYGraphWidget.Edge.LEFT
        ).format = DecimalFormat("###.#")

        // uncomment this line to freeze the range boundaries:
//        plot.setRangeBoundaries(-100, 100, BoundaryMode.FIXED)

        // create a dash effect for domain and range grid lines:
        val dashFx =
            DashPathEffect(floatArrayOf(PixelUtils.dpToPix(3f), PixelUtils.dpToPix(3f)), 0f)
        plot.graph.domainGridLinePaint.pathEffect = dashFx
        plot.graph.rangeGridLinePaint.pathEffect = dashFx

        panZoom = PanZoom.attach(
            plot,
            PanZoom.Pan.BOTH,
            PanZoom.Zoom.STRETCH_BOTH,
            PanZoom.ZoomLimit.MIN_TICKS
        )
        SeriesClickHelper(this).attachPlot(plot, panZoom)
    }
}