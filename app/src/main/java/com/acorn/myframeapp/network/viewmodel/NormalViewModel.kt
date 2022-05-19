package com.acorn.myframeapp.network.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.basemodule.network.BaseObserver
import com.acorn.basemodule.network.MyException
import com.acorn.myframeapp.bean.BaseResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.random.Random

/**
 * Created by acorn on 2022/5/19.
 */
class NormalViewModel : BaseNetViewModel() {
    private val netLiveData: MutableLiveData<BaseResponse<Int>> by lazy { MutableLiveData() }

    fun requestNet() {
        netObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                disposable.add(it)
            }
            .subscribe(object :
                BaseObserver<BaseResponse<Int>>(getUI(), ERROR_MODEL.LAYOUT) {
                override fun success(t: BaseResponse<Int>) {
                    super.success(t)
                    netLiveData.value = t
                }
            })
    }

    private fun netObservable(): Observable<BaseResponse<Int>> {
        return Observable.create<BaseResponse<Int>> {
            Thread.sleep(1000)
            val randomInt = Random.nextInt(100)
            val response: BaseResponse<Int>
            if (randomInt > 80) {
                response = BaseResponse<Int>().apply {
                    code = 0
                    data = randomInt
                }
                it.onNext(response)
                it.onComplete()
            } else if (randomInt > 30) {
                response = BaseResponse<Int>().apply {
                    code = 1
                    msg = "服务器异常......."
                }
                it.onNext(response)
                it.onComplete()
            }
//            else if (randomInt > 30) {
//                response = BaseResponse<Int>().apply {
//                    code = -2
//                    msg = "Token过期,重新登录"
//                }
//                it.onNext(response)
//                it.onComplete()
//            }
            else {
                it.onError(MyException("404啦"))
            }
        }
    }

    fun getNetLiveData(): LiveData<BaseResponse<Int>> = netLiveData
}