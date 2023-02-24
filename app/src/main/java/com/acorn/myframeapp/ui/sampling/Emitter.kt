package com.acorn.myframeapp.ui.sampling

import android.annotation.SuppressLint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by acorn on 2023/2/24.
 */
class Emitter(
    val sampling: Int,
    val groupCount: Int,
    private val duration: Long,
    val beginTime: Long
) {
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    private var curDuration = AtomicLong(0)
    private var curIndex = AtomicInteger(1)
    var emitData: EmitData? = null

    @SuppressLint("CheckResult")
    fun startEmit() {
        getSamplingEmitter(sampling, groupCount, duration)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                val elapsedTime = System.currentTimeMillis() - beginTime
                emitData = EmitData(elapsedTime, it)
                println("emitter-->${simpleDateFormat.format(Date())}:$emitData")
            }, {
                it.printStackTrace()
            })
    }

    /**
     * 按采样频率按组发送数据.
     * 比如10hz,持续时间2秒,应当发送20条数据.
     * 当groupCount为2时,会发送10次数据,每次2条.
     * @param sampling 采样频率
     * @param groupCount 一次传递几组数据
     * @param duration 总持续时间
     */
    private fun getSamplingEmitter(
        sampling: Int,
        groupCount: Int,
        duration: Long
    ): Observable<List<Int>> {
        val interval = (1000 / sampling * groupCount).toLong()
        return Observable.create<List<Int>> { emitter ->
            while (curDuration.get() < duration) {
                val list = mutableListOf<Int>()
                for (i in 0 until groupCount) {
                    list.add(curIndex.getAndAdd(1))
                }
                emitter.onNext(list)
                curDuration.getAndAdd(interval)
                Thread.sleep(interval)
            }
            emitter.onComplete()
        }
    }
}

data class EmitData(val elapsedTime: Long, val data: List<Int>)