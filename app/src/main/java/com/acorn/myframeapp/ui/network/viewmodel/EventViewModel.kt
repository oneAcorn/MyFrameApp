package com.acorn.myframeapp.ui.network.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.basemodule.network.BaseObserver
import com.acorn.basemodule.network.MyException
import com.acorn.myframeapp.bean.BaseResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * Created by acorn on 2022/5/19.
 */
class EventViewModel : BaseNetViewModel() {
    private val intervalLiveData: MutableLiveData<Long> by lazy { MutableLiveData() }

    fun intervalEvent() {
        Observable.interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                disposable.add(it)
            }
            .subscribe {
                intervalLiveData.value = it
            }
    }

    fun getIntervalLiveData(): LiveData<Long> = intervalLiveData
}