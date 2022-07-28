package com.acorn.myframeapp.ui.recyclerview.largedata2

import androidx.paging.PagingDataAdapter
import com.acorn.basemodule.extendfun.logI
import com.wcy.databasemodule.biz.ExperimentRecordBiz
import com.wcy.databasemodule.entities.ExperimentRecord
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by acorn on 2022/7/28.
 */
object DataGenerator {
    var isGenerating = false
    private var disposable: Disposable? = null

    fun generateData(adapter:PagingDataAdapter<*,*>) {
        isGenerating = true
        disposable = Observable.interval(0, 100, TimeUnit.MILLISECONDS)
            .map {
                ExperimentRecordBiz.instance.insert(ExperimentRecord().apply {
                    valueStr = "data:$it"
                })
            }
            .observeOn(Schedulers.computation())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                logI("retry..")
//adapter.retry()
            }
    }

    fun cancel() {
        isGenerating = false
        disposable?.dispose()
    }
}