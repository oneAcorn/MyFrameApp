package com.github.mikephil.charting.acorn.extendfun

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.datasets.IDataSet

/**
 * Created by acorn on 2023/4/10.
 */

/**
 * Safe get entry for index
 * 因为要实现随时调用DataSet.removeFirst(),而渲染器可能在调用过removeFirst后,
 * 获取到错误的dataSet.entryCount,导致IndexOutOfBoundsException.
 * 所以此处忽略此错误
 * @param index
 * @return
 */
fun IDataSet<*>.safeGetEntryForIndex(index: Int): Entry? {
    var ret: Entry? = null
    try {
        ret = getEntryForIndex(index)
    } catch (e: java.lang.IndexOutOfBoundsException) {
        e.printStackTrace()
    }
    return ret
}