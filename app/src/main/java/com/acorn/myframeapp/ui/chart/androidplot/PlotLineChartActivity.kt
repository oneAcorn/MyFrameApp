package com.acorn.myframeapp.ui.chart.androidplot

import android.annotation.SuppressLint
import android.graphics.DashPathEffect
import android.os.Bundle
import com.acorn.basemodule.extendfun.logI
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.basemodule.network.commonRequest
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.acorn.myframeapp.ui.chart.androidplot.custom.MyXYSeries
import com.acorn.myframeapp.ui.chart.androidplot.custom.PlotSeriesType
import com.acorn.myframeapp.ui.chart.androidplot.custom.SeriesClickHelper
import com.androidplot.acorn.IBoundaryChangeListener
import com.androidplot.util.PixelUtils
import com.androidplot.xy.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_plot_line_chart.*
import okhttp3.internal.Util
import java.text.DecimalFormat
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.random.Random

/**
 * Created by acorn on 2022/11/8.
 */
class PlotLineChartActivity : BaseNoViewModelActivity(), IBoundaryChangeListener {
    //缩放
    private lateinit var panZoom: PanZoom
    private val disposable = CompositeDisposable()

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
        drawFxBtn.singleClick {
            addFormulaLine()
        }
        startBtn.singleClick {
            if (startBtn.text == "start") {
                startAddData()
                startBtn.text = "stop"
            } else {
                stopAddData()
                startBtn.text = "start"
            }
        }
        clearBtn.singleClick {
            plot.clear()
            plot.redraw()
        }
    }

    private var scheduledExecutor: ScheduledThreadPoolExecutor? = null

    private var isAddData = false

    @SuppressLint("CheckResult")
    private fun startAddData() {
//        Observable.create<Boolean> { emitter ->
//            while (true) {
//                Thread.sleep(10)
//                emitter.onNext(true)
//            }
//        }
//            .commonRequest(disposable)
//            .subscribe {
//                appendData(Random.nextFloat() * 20f, Random.nextFloat() * 50f)
//                plot.redraw()
//            }

//        isAddData = true
//        while (isAddData) {
//            Thread.sleep(1000)
//            appendData(Random.nextFloat() * 20f, Random.nextFloat() * 50f)
//            plot.redraw()
//        }

        scheduledExecutor = ScheduledThreadPoolExecutor(1, Util.threadFactory("append", false))

        scheduledExecutor?.scheduleAtFixedRate(
            {
                appendData(Random.nextFloat() * 2000f, Random.nextFloat() * 5000f)
                plot.redraw()
            },
            0,
            10,
            TimeUnit.MILLISECONDS
        )
    }

    private fun stopAddData() {
        scheduledExecutor?.shutdownNow()
        scheduledExecutor = null
//        isAddData = false
//        if (!disposable.isDisposed) {
//            disposable.dispose()
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAddData()
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
//        showTip("append:$x,$y")
        logI("append:$x,$y")
        val seriesList = plot.registry.seriesList
        val series = if (seriesList == null || seriesList.isEmpty()) {
//            val dynamicSeries = DynamicSeries()
            val dynamicSeries = MyXYSeries("test1")
//            val formatter1 = LineAndPointFormatter(
//                Color.rgb(0, 200, 0), null, null, null
//            )
            val formatter1 = LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels)
            formatter1.setPointLabeler(null)
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
        logI("count:${series.size()}")
    }

    //region Formula
    private fun addFormulaLine() {
        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        val series1Format = LineAndPointFormatter(this, R.xml.line_point_formatter_formula)
        plot.addSeries(generateSeries(-5.00, 5.00, 100.00), series1Format)
    }

    private fun generateSeries(minX: Double, maxX: Double, resolution: Double): XYSeries {
        val range = maxX - minX
        val step = range / resolution
        val xVals: MutableList<Number> = ArrayList()
        val yVals: MutableList<Number> = ArrayList()
        var x = minX
        while (x <= maxX) {
            xVals.add(x)
            yVals.add(fx(x))
            x += step
        }
        return MyXYSeries(xVals, yVals, "f(x) = (x^2) - 13").apply {
            seriesType = PlotSeriesType.Formula
        }
    }

    private fun fx(x: Double): Double {
        return abs(x * x) - 13
    }

    override fun onBoundaryChanged(isDomain: Boolean, lower: Number?, upper: Number?) {
        //TODO 改用FromulaSeries
//        lower ?: return
//        upper ?: return
//        val series1Format = LineAndPointFormatter(this, R.xml.line_point_formatter_formula)
//        for (series in plot.registry.seriesList) {
//            if (series is MyXYSeries && series.seriesType == PlotSeriesType.Formula) {
//                plot.removeSeries(series)
//                plot.addSeries(generateSeries(lower.toDouble(),upper.toDouble(),100.00),series1Format)
//            }
//        }
    }
    //endregion

    private fun initPlot() {
        //设置x轴数值的DecimalFormat
        // only display whole numbers in domain labels
        plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = DecimalFormat("#.#")

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
        panZoom.setBoundaryChangeListener(this)
        SeriesClickHelper(this).attachPlot(plot, panZoom)
    }
}