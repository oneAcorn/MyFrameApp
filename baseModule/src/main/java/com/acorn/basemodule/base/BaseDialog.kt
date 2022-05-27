package com.acorn.basemodule.base

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import androidx.appcompat.app.AppCompatDialog

/**
 * 参考QMUI->QMUIBaseDialog
 * Created by acorn on 2022/5/26.
 */
abstract class BaseDialog(context: Context, themeId: Int) : AppCompatDialog(context, themeId) {
    var mCancelable = true
    private var canceledOnTouchOutside = true

    //是否设置过canceledOnTouchOutside
    private var canceledOnTouchOutsideSet = false

    init {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun setCancelable(cancelable: Boolean) {
        super.setCancelable(cancelable)
        if (mCancelable != cancelable) {
            this.mCancelable = cancelable
            onSetCancelable(cancelable)
        }
    }

    protected open fun onSetCancelable(cancelable: Boolean) {

    }

    override fun setCanceledOnTouchOutside(cancel: Boolean) {
        super.setCanceledOnTouchOutside(cancel)
        if (cancel && !mCancelable) {
            mCancelable = true
        }
        canceledOnTouchOutside = cancel
        canceledOnTouchOutsideSet = true
    }

    protected open fun shouldWindowCloseOnTouchOutside(): Boolean {
        if (!canceledOnTouchOutside) {
            val typeArray =
                context.obtainStyledAttributes(intArrayOf(android.R.attr.windowCloseOnTouchOutside))
            canceledOnTouchOutside = typeArray.getBoolean(0, true)
            typeArray.recycle()
            canceledOnTouchOutsideSet = true
        }
        return canceledOnTouchOutside
    }

    override fun dismiss() {
        var ctx = context
        if (ctx is ContextWrapper) {
            ctx = ctx.baseContext
        }
        if (ctx is Activity) {
            if (ctx.isDestroyed || ctx.isFinishing) {
                return
            }
            super.dismiss()
        } else {
            try {
                super.dismiss()
            } catch (ignore: Throwable) {
            }
        }
    }
}