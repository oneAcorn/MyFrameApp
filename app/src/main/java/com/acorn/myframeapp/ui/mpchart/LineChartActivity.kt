package com.acorn.myframeapp.ui.mpchart

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.acorn.basemodule.extendfun.showToast
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.github.mikephil.charting.components.Description
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
import kotlinx.android.synthetic.main.activity_line_chart.*

/**
 * Created by acorn on 2022/7/8.
 */
class LineChartActivity : BaseNoViewModelActivity(), OnChartValueSelectedListener {
    private var counts = 0
    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            addEntry(Entry((counts * intervalMill).toFloat(), (-1000..1000).random().toFloat()))
            counts++
            sendEmptyMessageDelayed(0, intervalMill.toLong())
        }
    }
    private var isStarted = false
    private val intervalMill = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart)
    }

    override fun initView() {
        super.initView()
        lineChart.run {
            setOnChartValueSelectedListener(this@LineChartActivity)
            //右下角的描述
//            description.isEnabled = true
//            description.text = "I'm description"
            val bottomDescription = Description()
            bottomDescription.text = "x轴"
            val leftDescription = Description()
            leftDescription.text = "y轴"
            setBottomAxisDescription(bottomDescription)
            setLeftAxisDescription(leftDescription)

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
        startBtn.singleClick {
            if (isStarted) { //停止
                startBtn.text = "Start"
                handler.removeCallbacksAndMessages(null)
            } else {
                startBtn.text = "Stop"
                startRecord()
            }
            isStarted = !isStarted
        }
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

        // limit the number of visible entries
//        lineChart.setVisibleXRangeMaximum(120f)
        // chart.setVisibleYRange(30, AxisDependency.LEFT);

        // move to the latest entry(this automatically refreshes the chart (calls invalidate()))
        lineChart.moveViewToX(lineData.entryCount.toFloat())
//        lineChart.invalidate()
    }

    private fun startRecord() {
        handler.sendEmptyMessage(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
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