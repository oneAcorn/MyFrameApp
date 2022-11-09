package com.acorn.myframeapp.ui.chart.androidplot.custom

/**
 * TODO getX,getY动态计算,不存入内存
 * Created by acorn on 2022/11/9.
 */
class FormulaSeries : MyXYSeries {

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