package com.acorn.myframeapp.ui.chart.androidplot

import android.os.Bundle
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity

/**
 * Created by acorn on 2022/11/8.
 */
class PlotLineChartActivity:BaseNoViewModelActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plot_line_chart)
    }
}