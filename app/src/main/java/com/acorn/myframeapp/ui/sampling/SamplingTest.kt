package com.acorn.myframeapp.ui.sampling

import android.annotation.SuppressLint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * 根据传感器情况采集数据
 * 传感器保有自己的采样频率,app使用另一个统一的采样频率采集数据.
 * 比如1个实验中,使用1个20hz和1个10hz的传感器,然后实验的采样频率设置为100hz.
 * 注:传感器在高采样频率时,或一次发送多组数据.
 * 比如100hz时每40毫秒发送4组数据.(意味着实际采样频率是25hz,通过软件转换为100Hz)
 * 那么第一组数据代表0ms时的数据,第二组数据代表10ms的,第三组20ms的,第四组30ms的
 * Created by acorn on 2023/2/24.
 */
fun main() {
//    test(50, 2000)
    test2(20, 3000)
//    println("Res:${50 % 200}")
    Thread.sleep(10 * 1000)
}

private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
private var curDuration = AtomicLong(0)
private val mCurIndex = AtomicInteger(1)

/**
 * 假设能获取到数据产生时间的情况(ble可以,Usb不可以获取时间)
 */
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
            val value = findNearestValue(elapsedTime, emitter1.mSampling, emitData)
            log("$elapsedTime,$value")
            emitter.onNext(FinalData(1, 1))
            curDuration.getAndAdd(interval)
            Thread.sleep(interval)
        }
        emitter.onComplete()
    }.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe({

    }, {
        it.printStackTrace()
    })
}

/**
 * 假设不能获取到数据产生时间(usb获取不到)
 * 但是特定采样频率发送数据组数是已知的.
 * 比如100hz时,所有传感器都按一次4组数据形式发送.
 */
private fun test2(sampling: Int, duration: Long) {
    mCurIndex.set(1)
    val interval = (1000 / sampling).toLong()
    val beginTime = System.currentTimeMillis()
    val emitter1 = Emitter(10, 2, 2000, beginTime)
    val emitter2 = Emitter(20, 4, 2000, beginTime)
    val emitters = listOf(emitter2)
    GlobalScope.launch {
        for (emitter in emitters) {
            emitter.startEmit()
        }
//        delay(100)
        while (curDuration.get() < duration) {
            for ((emitterIndex, emitter) in emitters.withIndex()) {
                val value = emitter.getDataBySampling(sampling)
                log("data{$emitterIndex}:$value")
            }
            curDuration.getAndAdd(interval)
            delay(interval)
        }
    }
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