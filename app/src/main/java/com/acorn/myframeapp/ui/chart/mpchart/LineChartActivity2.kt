package com.acorn.myframeapp.ui.chart.mpchart

import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import com.acorn.basemodule.extendfun.logI
import com.acorn.basemodule.extendfun.showToast
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_line_chart2.*
import okhttp3.internal.Util
import java.util.*
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by acorn on 2022/7/8.
 */
class LineChartActivity2 : BaseNoViewModelActivity(), OnChartValueSelectedListener {
    private val queue = LinkedList<PointF>().apply {
        offer(PointF(5f, 1f))
        offer(PointF(4f, 6f))
        offer(PointF(3f, 4f))
        offer(PointF(2f, 3f))
        offer(PointF(1f, 2f))
        offer(PointF(-1f, 6f))
    }
    private val queue2 = LinkedList<PointF>().apply {
        offer(PointF(1f, 1f))
        offer(PointF(2f, 2f))
        offer(PointF(3f, 3f))
        offer(PointF(4f, 4f))
        offer(PointF(5f, 3f))
        offer(PointF(6f, 2f))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart2)
    }

    override fun initView() {
        super.initView()
        lineChart.run {
            setOnChartValueSelectedListener(this@LineChartActivity2)
            //右下角的描述
//            description.isEnabled = true
//            description.text = "I'm description"

            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setDrawGridBackground(false)
            // if disabled, scaling can be done on x- and y-axis separately
            setPinchZoom(false)
            setBackgroundColor(Color.LTGRAY)

            val data = LineData()
            data.setValueTextColor(Color.WHITE)
            // add empty data
            this.data = data

            // get the legend (only possible after setting data)
            val l: Legend = legend

            // modify the legend ...
            l.form = LegendForm.LINE
            l.textColor = Color.WHITE
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
        }
        addBugBtn.singleClick {
//            val point = queue.poll() ?: return@singleClick
//            addEntry(Entry(point.x, point.y))
            val multiplyX = if (mCurIndex % 2 == 0) 1 else -1
            val entry=Entry(mCurIndex.toFloat(), (mCurIndex * multiplyX).toFloat())
            addEntry(entry)
            logI("addEntry($mCurIndex):$entry")
            mCurIndex++
        }
        addDataBtn.singleClick {
            val point = queue2.poll() ?: return@singleClick
            addEntry(Entry(point.x, point.y))
        }
        resetBtn.singleClick {
            lineChart.data.clearValues()
            lineChart.invalidate()
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
    }

    private var scheduledExecutor: ScheduledThreadPoolExecutor? = null
    private var mCurIndex = 0
    private fun startAddData() {
        scheduledExecutor = ScheduledThreadPoolExecutor(1, Util.threadFactory("append", false))

//        curIndex = 0
        scheduledExecutor?.scheduleAtFixedRate(
            {
                val multiplyX = if (mCurIndex % 2 == 0) 1 else -1
                addEntry(Entry(mCurIndex.toFloat(), (mCurIndex * multiplyX).toFloat()))
//                logI("addEntry($curIndex)")
                mCurIndex++
            },
            0,
            10,
            TimeUnit.MILLISECONDS
        )
    }

    private fun stopAddData() {
        scheduledExecutor?.shutdownNow()
        scheduledExecutor = null
    }

    private fun addEntry(entry: Entry) {
        //上面创建过了,直接获取.
        val lineData = lineChart.data
        //一个dataSet代表一条线
        var dataSet = lineData.getDataSetByIndex(0)
        if (dataSet == null) {
            dataSet = createSet()
            lineData.addDataSet(dataSet)
        }
        //向第一条线添加数据
        lineData.addEntry(entry, 0)
        lineData.notifyDataChanged()
        // let the chart know it's data has changed
        lineChart.notifyDataSetChanged()

        // limit the number of visible entries
//        lineChart.setVisibleXRangeMaximum(120f)
        // chart.setVisibleYRange(30, AxisDependency.LEFT);

        // move to the latest entry(this automatically refreshes the chart (calls invalidate()))
        lineChart.moveViewToX(lineData.entryCount.toFloat())
//        lineChart.invalidate()
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "Test Data")
        set.axisDependency = AxisDependency.LEFT
        set.color = ColorTemplate.getHoloBlue()
        set.setCircleColor(Color.WHITE)
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

    override fun onValueSelected(e: Entry, h: Highlight?) {
        showToast("valueSelected:${e.x},${e.y}")
    }

    override fun onNothingSelected() {
        showToast("nothing selected")
    }
}