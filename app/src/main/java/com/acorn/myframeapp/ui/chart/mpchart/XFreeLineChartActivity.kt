package com.acorn.myframeapp.ui.chart.mpchart

import android.graphics.Color
import android.graphics.PointF
import android.view.MotionEvent
import com.acorn.basemodule.base.BaseBindingActivity
import com.acorn.basemodule.extendfun.logI
import com.acorn.basemodule.extendfun.showToast
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.databinding.ActivityXFreeLineChartBinding
import com.acorn.myframeapp.ui.chart.views.MyMarkerView
import com.github.mikephil.charting.acorn.XFreeLineChart
import com.github.mikephil.charting.acorn.dataset.XFreeLineDataSet
import com.github.mikephil.charting.acorn.highlight.XFreeHighlighter
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import okhttp3.internal.Util
import java.util.*
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.ConcurrentModificationException

/**
 * Created by acorn on 2023/4/7.
 */
class XFreeLineChartActivity :
    BaseBindingActivity<BaseNetViewModel, ActivityXFreeLineChartBinding>(),
    OnChartValueSelectedListener {
    private val queue = LinkedList<PointF>().apply {
        for (i in 0..500) {
            val multiplyX = if (i % 2 == 0) 1 else -1
            offer(PointF(multiplyX.toFloat(), i.toFloat()))
        }
    }
    private val queue2 = LinkedList<PointF>().apply {
        for (i in 0..500) {
            val multiplyX = if (i % 2 == 0) 1 else -1
            offer(PointF(i.toFloat(), (i * multiplyX).toFloat()))
        }
    }

    override fun initView() {
        super.initView()
        initLineChart()
    }

    override fun initListener() {
        super.initListener()
        binding.addBugBtn.singleClick {
            val point = queue.poll() ?: return@singleClick
            addEntry(Entry(point.x, point.y))
        }
        binding.addDataBtn.singleClick {
            val point = queue2.poll() ?: return@singleClick
            addEntry(Entry(point.x, point.y))
        }
        binding.resetBtn.singleClick {
//            binding.xFreeLineChart.data.clearValues()
//            binding.xFreeLineChart.invalidate()
            binding.xFreeLineChart.clear()
        }
        binding.startBtn.singleClick {
            if (binding.startBtn.text == "start") {
                startAddData()
                binding.startBtn.text = "stop"
            } else {
                stopAddData()
                binding.startBtn.text = "start"
            }
        }
        binding.startXFreeBtn.singleClick {
            if (binding.startXFreeBtn.text == "start xFree") {
                startXFreeAddData()
                binding.startXFreeBtn.text = "stop"
            } else {
                stopAddData()
                binding.startXFreeBtn.text = "start xFree"
            }
        }
        binding.startInfiniteBtn.singleClick {
            if (binding.startInfiniteBtn.text == "start infinite") {
                startInfiniteAddData(20000, 10)
                binding.startInfiniteBtn.text = "stop"
            } else {
                stopAddData()
                binding.startInfiniteBtn.text = "start infinite"
            }
        }
        binding.testRemoveFirstBtn.singleClick {
            val lineData = binding.xFreeLineChart.data
            val dataSet = lineData?.getDataSetByIndex(0)
            dataSet?.removeFirst()
            lineData.notifyDataChanged()
            // let the chart know it's data has changed
            binding.xFreeLineChart.notifyDataSetChanged()
            binding.xFreeLineChart.invalidate()
        }
        binding.selectAreaBtn.singleClick {
            binding.xFreeLineChart.enterSelectAreaMode { selectedSets ->
                for (set in selectedSets) {
                    logI("selectArea:${set.set.label},${set.entrys}")
                }
            }
        }
    }

    private var scheduledExecutor: ScheduledThreadPoolExecutor? = null
    private var curIndex = 0
    private fun startAddData() {
        scheduledExecutor = ScheduledThreadPoolExecutor(1, Util.threadFactory("append", false))

//        curIndex = 0
        scheduledExecutor?.scheduleAtFixedRate(
            {
                val multiplyX = if (curIndex % 2 == 0) 1 else -1
                addEntry(Entry(curIndex.toFloat(), (curIndex * multiplyX).toFloat()))
//                logI("addEntry($curIndex)")
                curIndex++
            },
            0,
            10,
            TimeUnit.MILLISECONDS
        )
    }

    private fun startXFreeAddData() {
        scheduledExecutor = ScheduledThreadPoolExecutor(1, Util.threadFactory("append", false))

//        curIndex = 0
        scheduledExecutor?.scheduleAtFixedRate(
            {
                val multiplyX = if (curIndex % 2 == 0) 1 else -1
                addEntry(Entry((curIndex * multiplyX).toFloat(), curIndex.toFloat()))
//                logI("addEntry($curIndex)")
                curIndex++
            },
            0,
            10,
            TimeUnit.MILLISECONDS
        )
    }

    /**
     * Start infinite add data
     * 超出一定数量直接从头部删除,保持恒定的最大数量
     */
    private fun startInfiniteAddData(maxAmount: Long, period: Long) {
        scheduledExecutor = ScheduledThreadPoolExecutor(1, Util.threadFactory("append", false))

//        curIndex = 0
        scheduledExecutor?.scheduleAtFixedRate(
            {
                val multiplyX = if (curIndex % 2 == 0) 1 else -1
                addEntry(Entry(curIndex.toFloat(), (curIndex * multiplyX).toFloat()), maxAmount)
//                logI("addEntry($curIndex)")
//                val lineData = binding.xFreeLineChart.data
//                val dataSet = lineData?.getDataSetByIndex(0)
//                if (dataSet != null && dataSet.entryCount >= 20) {
//                    dataSet.removeFirst()
//                    lineData.notifyDataChanged()
//                    // let the chart know it's data has changed
//                    binding.xFreeLineChart.notifyDataSetChanged()
//                    binding.xFreeLineChart.invalidate()
//                }
                curIndex++
            },
            0,
            period,
            TimeUnit.MILLISECONDS
        )
    }

    private fun stopAddData() {
        scheduledExecutor?.shutdownNow()
        scheduledExecutor = null
    }

    private fun initLineChart() {
        binding.xFreeLineChart.run {
            setOnChartValueSelectedListener(this@XFreeLineChartActivity)
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setDrawGridBackground(false)
            // if disabled, scaling can be done on x- and y-axis separately
            setPinchZoom(false)
            setBackgroundColor(Color.LTGRAY)

//            val data = LineData()
//            data.setValueTextColor(Color.WHITE)
//            // add empty data
//            this.data = data

            //点击point显示的MarkerView
            val markerView = MyMarkerView(this@XFreeLineChartActivity, R.layout.custom_marker_view)
            markerView.chartView = this
            marker = markerView

            // get the legend (only possible after setting data)
            val l: Legend = legend
            // modify the legend ...
            l.form = Legend.LegendForm.LINE
            l.textColor = Color.BLUE
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.VERTICAL
            l.setDrawInside(false)

            xAxis.textColor = Color.WHITE
            xAxis.setDrawGridLines(false)
            xAxis.setAvoidFirstLastClipping(true)
            xAxis.setLabelCount(12, false)
            xAxis.isEnabled = true
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            axisLeft.textColor = Color.WHITE
//            axisLeft.axisMaximum = 100f
//            axisLeft.axisMinimum = 0f
            axisLeft.setDrawGridLines(true)
//            axisLeft.setCenterAxisLabels(true)

            axisRight.isEnabled = false

            highlighter = XFreeHighlighter(this)

            onChartGestureListener = object : OnChartGestureListener {
                override fun onChartGestureStart(
                    me: MotionEvent?,
                    lastPerformedGesture: ChartTouchListener.ChartGesture?
                ) {
                }

                override fun onChartGestureEnd(
                    me: MotionEvent?,
                    lastPerformedGesture: ChartTouchListener.ChartGesture?
                ) {
//                    logI("onChartGestureEnd")
                    setHighlightAndMarkerEnable(true)
                }

                override fun onChartLongPressed(me: MotionEvent?) {
                }

                override fun onChartDoubleTapped(me: MotionEvent?) {
                }

                override fun onChartSingleTapped(me: MotionEvent?) {
                }

                override fun onChartFling(
                    me1: MotionEvent?,
                    me2: MotionEvent?,
                    velocityX: Float,
                    velocityY: Float
                ) {
                }

                override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
//                    logI("onChartScale")
                    setHighlightAndMarkerEnable(false)
                }

                override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
//                    logI("onChartTranslate")
                    setHighlightAndMarkerEnable(false)
                }

                override fun onChartTranslateEnd() {
//                    logI("onChartTranslateEnd")
                    setHighlightAndMarkerEnable(true)
                }
            }

//            isLogEnabled = true
        }
    }

    /**
     * Set highlight enable
     * For performance optimization
     * @param isEnable
     */
    private fun setHighlightAndMarkerEnable(isEnable: Boolean) {
        binding.xFreeLineChart.setDrawMarkers(isEnable)
        val lineData = binding.xFreeLineChart.data ?: return
        try {
            for (set in lineData.dataSets) {
                set.isHighlightEnabled = isEnable
            }
        } catch (e: ConcurrentModificationException) {
            e.printStackTrace()
        }
    }

    private fun addEntry(entry: Entry, limitMaxAmount: Long = -1) {
        var lineData = binding.xFreeLineChart.data
        if (lineData == null) {
            val data = LineData()
            data.setValueTextColor(Color.WHITE)
            // add empty data
            binding.xFreeLineChart.data = data
            lineData = data
        }
        //一个dataSet代表一条线
        var dataSet = lineData.getDataSetByIndex(0)
        if (dataSet == null) {
            dataSet = createSet()
            lineData.addDataSet(dataSet)
        }
        //向第一条线添加数据
        lineData.addEntry(entry, 0)
//        logI("entryCount:${dataSet.entryCount}")
        if (limitMaxAmount > 0 && dataSet.entryCount >= limitMaxAmount) {
            dataSet.removeFirst()
        }
        lineData.notifyDataChanged()
//        logI("after notify entryCount:${dataSet.entryCount}")

        // let the chart know it's data has changed
        binding.xFreeLineChart.notifyDataSetChanged()

        // limit the number of visible entries
//        lineChart.setVisibleXRangeMaximum(120f)
        // chart.setVisibleYRange(30, AxisDependency.LEFT);

        // move to the latest entry(this automatically refreshes the chart (calls invalidate()))
        binding.xFreeLineChart.moveViewToX(lineData.entryCount.toFloat())
//        lineChart.invalidate()
    }

    private fun createSet(): XFreeLineDataSet<XFreeLineChart> {
        val set = XFreeLineDataSet(binding.xFreeLineChart, null, "Test Data")
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.color = ColorTemplate.getHoloBlue()
        set.setCircleColor(Color.WHITE)
        //100个点及以下才显示
        set.mPointVisibleThreshold = 100
//        set.setDrawCircles(false)
        set.lineWidth = 2f
        set.circleRadius = 4f
        set.fillAlpha = 65
        set.fillColor = ColorTemplate.getHoloBlue()
        set.highLightColor = Color.rgb(244, 117, 117)
        set.valueTextColor = Color.WHITE
        set.valueTextSize = 9f
        set.setDrawValues(false)
        return set
    }

    override fun getViewModel(): BaseNetViewModel? = null

    override fun onValueSelected(e: Entry, h: Highlight?) {
        showToast("valueSelected:${e.x},${e.y}")
    }

    override fun onNothingSelected() {
        showToast("nothing selected")
    }
}