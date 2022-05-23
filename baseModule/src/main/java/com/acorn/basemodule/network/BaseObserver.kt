package com.acorn.basemodule.network

import android.accounts.NetworkErrorException
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.acorn.basemodule.R
import com.acorn.basemodule.extendfun.appContext
import io.reactivex.rxjava3.observers.DisposableObserver
import retrofit2.HttpException
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by acorn on 2019-08-20.
 */
open class BaseObserver<T : IResponse>(
    networkUI: INetworkUI?,
    private val model: ERROR_MODEL = ERROR_MODEL.TOAST,
    private val isShowProgressDialog: Boolean = true
) : DisposableObserver<T>() {
    private val weakUI = if (networkUI == null) null else WeakReference(networkUI)
    private val handler: ProgressHandler by lazy { ProgressHandler(weakUI) }
    private val showProgressDelayMill = 300L

    override fun onComplete() {
        if (isShowProgressDialog) {
//            weakUI?.get()?.dismissProgressDialog()
            handler.sendEmptyMessage(ProgressHandler.DISMISS_DIALOG)
        }
    }

    override fun onStart() {
        super.onStart()
        if (isShowProgressDialog) {
//            weakUI?.get()?.showProgressDialog()
            handler.sendEmptyMessageDelayed(ProgressHandler.SHOW_DIALOG, showProgressDelayMill)
        }
    }

    override fun onNext(t: T) {
        val isNullData = isNullData(t)
        if (t.isTokenExpired()) {
//            appContext.logout(null)
        } else if (t.isSuccess() && !isNullData) {
            weakUI?.get()?.showContentLayout()
            success(t)
        } else if (isNullData) {
            weakUI?.get()?.showNullLayout()
            success(t)
        } else {
            doError(t = t)
        }
    }

    override fun onError(e: Throwable) {
        doError(e = e)
    }

    private fun doError(t: T? = null, e: Throwable? = null) {
        if (isShowProgressDialog) {
//            weakUI?.get()?.dismissProgressDialog()
            handler.sendEmptyMessage(ProgressHandler.DISMISS_DIALOG)
        }
        weakUI?.get()?.let {
            when (model) {
                ERROR_MODEL.LAYOUT -> {
                    it.showErrorLayout()
                }
                ERROR_MODEL.TOAST -> {
                    it.showTip(getFailMessage(t, e))
                }
                ERROR_MODEL.LAYOUT_TOAST -> {
                    it.showErrorLayout()
                    it.showTip(getFailMessage(t, e))
                }
                else -> {}
            }
        }
        error(t, e)
        errorMsg(t?.code() ?: -1, getFailMessage(t, e))
    }

    private fun getFailMessage(t: T? = null, e: Throwable? = null): String {
        var msg = ""
        if (t != null) {
            msg = t.failMessage() ?: ""
        } else if (e != null) {
            msg = when (e) {
                is NetworkErrorException -> {
                    appContext.getString(R.string.network_error_home)
                }
                is SocketTimeoutException -> {
                    appContext.getString(R.string.network_error_home)
                }
                is UnknownHostException -> {
                    appContext.getString(R.string.no_connect_service)
                }
                is MyException -> {
                    e.message ?: appContext.getString(R.string.network_error_home)
                }
                else -> {
                    e.printStackTrace()
                    try {
                        val httpException = e as HttpException
                        when (httpException.code()) {
                            404 -> "ImcoreError:404"
                            401 -> "ImcoreError:401"
                            503 -> "ImcoreError:503"
                            500 -> "ImcoreError:500"
                            else -> "ImcoreError:other"
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        appContext.getString(R.string.server_exception)
                    }
                }
            }
        }
        return msg
    }

    protected open fun isNullData(t: T): Boolean {
        return false
    }

    protected open fun error(t: T?, e: Throwable?) {

    }

    protected open fun errorMsg(code: Int, msg: String) {}

    protected open fun success(t: T) {

    }

    enum class ERROR_MODEL {
        TOAST,
        LAYOUT,
        NONE,
        LAYOUT_TOAST
    }

    class ProgressHandler(private val weakUI: WeakReference<INetworkUI?>?) :
        Handler(Looper.getMainLooper()) {


        companion object {
            const val SHOW_DIALOG = 1
            const val DISMISS_DIALOG = 2
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            msg ?: return
            when (msg.what) {
                SHOW_DIALOG -> {
                    val ui = weakUI?.get()
                    ui?.showProgressDialog()
                }
                DISMISS_DIALOG -> {
                    val ui = weakUI?.get()
                    ui?.dismissProgressDialog()
                    removeCallbacksAndMessages(null)
                }
            }
        }
    }
}