package com.acorn.myframeapp.ui.sampling

import android.annotation.SuppressLint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.min

/**
 * Created by acorn on 2023/2/24.
 */
class Emitter(
    val mSampling: Int,
    val groupCount: Int,
    private val duration: Long,
    private val beginTime: Long
) {
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    private var curDuration = AtomicLong(0)
    private var curIndex = AtomicInteger(1)
    var emitData: EmitData? = null
    private val mInterval = (1000 / mSampling * groupCount).toLong()

    @SuppressLint("CheckResult")
    fun startEmit() {
        getSamplingEmitter(groupCount, duration)
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
        groupCount: Int,
        duration: Long
    ): Observable<List<Int>> {
        return Observable.create<List<Int>> { emitter ->
            while (curDuration.get() < duration) {
                val list = mutableListOf<Int>()
                for (i in 0 until groupCount) {
                    list.add(curIndex.getAndAdd(1))
                }
                getDataTimes.set(0)
                emitter.onNext(list)
                curDuration.getAndAdd(mInterval)
                Thread.sleep(mInterval)
            }
            emitter.onComplete()
        }
    }

    /**
     * 取值次数
     */
    private val getDataTimes = AtomicLong(0)

    fun getDataBySampling(consumerSampling: Int): Int? {
//        val curData = emitData?.data ?: return null
//        if (curData.isEmpty()) return null
        if (groupCount <= 1) {
            return emitData?.data?.get(0)
        }
        //第几次取值
        val times = getDataTimes.getAndAdd(1).toInt()
//        val consumerInterval = 1000 / consumerSampling
        return if (consumerSampling >= mSampling) {
            //生产者慢于消费者则通过提供重复数据满足消费者
            val dataIndex = min(times / (consumerSampling / mSampling), (emitData?.data?.size?:1) - 1)
            log("dataIndex:$dataIndex,times:$times,$consumerSampling,$mSampling")
            emitData?.data?.get(dataIndex)
        } else {
            //生产者快于消费者,则提供多组数据中的最后一组数据
            emitData?.data?.last()
        }
    }

    private fun log(str: String) {
        println("emitter-->${simpleDateFormat.format(Date())}:$str")
    }
}

data class EmitData(val elapsedTime: Long, val data: List<Int>)