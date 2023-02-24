package com.acorn.myframeapp.ui.sampling

import android.annotation.SuppressLint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.abs

/**
 * Created by acorn on 2023/2/24.
 */
fun main() {
    test(50, 2000)
    Thread.sleep(10 * 1000)
}

private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
private var curDuration = AtomicLong(0)
private val curIndex = AtomicInteger(0)

@SuppressLint("CheckResult")
private fun test(sampling: Int, duration: Long) {
    val interval = (1000 / sampling).toLong()
    val beginTime = System.currentTimeMillis()
    val emitter1 = Emitter(10, 2, 2000, beginTime)
    emitter1.startEmit()
    Observable.create<FinalData> { emitter ->
        while (curDuration.get() < duration) {
            val emitData = emitter1.emitData ?: continue
            val elapsedTime = System.currentTimeMillis() - beginTime
            val value = findNearestValue(elapsedTime, emitter1.sampling, emitData)
            log("$elapsedTime,$value")
            emitter.onNext(FinalData(1, 1))
            curDuration.getAndAdd(interval)
            Thread.sleep(interval)
        }
        emitter.onComplete()
    }
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe({

        }, {
            it.printStackTrace()
        })
}

private fun findNearestValue(curElapsedTime: Long, sampling: Int, curData: EmitData): Int {
    if (curData.data.size == 1) {
        return curData.data[0]
    }
    val interval = (1000 / sampling).toLong()
    var targetIndex = 0
    var minDifferent: Long? = null
    for (i in 0 until curData.data.size) {
        val emitElapsedTime = curData.elapsedTime + interval * i
        val diff = curElapsedTime - emitElapsedTime
        if (diff < 0) { //超出当前采样的时间了
            break
        }
//        log("near:${diff},$curElapsedTime,$emitElapsedTime,$i,$curData")
        if (minDifferent == null || diff < minDifferent) {
            minDifferent = diff
            targetIndex = i
        }
    }
    return curData.data[targetIndex]
}

private fun log(str: String) {
    println("processor-->${simpleDateFormat.format(Date())}:$str")
}

data class FinalData(val index: Int, val value: Int)