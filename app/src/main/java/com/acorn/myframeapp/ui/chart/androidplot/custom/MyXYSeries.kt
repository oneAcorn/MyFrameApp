package com.acorn.myframeapp.ui.chart.androidplot.custom

import com.androidplot.xy.SimpleXYSeries

/**
 * Created by acorn on 2022/11/9.
 */
class MyXYSeries : SimpleXYSeries {
    var seriesType: PlotSeriesType = PlotSeriesType.Line

    constructor(title: String) : super(title)

    constructor(format: ArrayFormat, title: String, vararg model: Number) : super(
        format,
        title,
        *model
    )

    constructor(model: List<Number>, format: ArrayFormat, title: String) : super(
        model,
        format,
        title
    )

    constructor(xVals: List<Number>, yVals: List<Number>, title: String) : super(
        xVals,
        yVals,
        title
    )
}