package com.acorn.basemodule.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.view.ViewCompat
import com.qmuiteam.qmui.widget.popup.QMUIBasePopup
import java.lang.ref.WeakReference

/**
 * 参考:QMUI->QDPopupFragment
 * Created by acorn on 2022/5/30.
 */
abstract class BasePopup(protected val mContext: Context) {
    val DIM_AMOUNT_NOT_EXIST = -1f
    val NOT_SET = -1

    protected lateinit var mWindow: PopupWindow
    protected var mWindowManager: WindowManager? = null
    protected var mAttachedViewRf: WeakReference<View>? = null
    protected var mDimAmount = DIM_AMOUNT_NOT_EXIST
    private val mDismissListener: PopupWindow.OnDismissListener? = null

    private val mOnAttachStateChangeListener: View.OnAttachStateChangeListener =
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}
            override fun onViewDetachedFromWindow(v: View) {
                dismiss()
            }
        }

    @SuppressLint("ClickableViewAccessibility")
    private val mOutsideTouchDismissListener =
        OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                mWindow.dismiss()
                return@OnTouchListener true
            }
            false
        }

    init {
        mWindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mWindow = PopupWindow(mContext).apply {
            setBackgroundDrawable((ColorDrawable(Color.TRANSPARENT)))
            isFocusable = true
            isTouchable = true
            setOnDismissListener {
                removeOldAttachStateChangeListener()
                mAttachedViewRf = null
                onDismiss()
            }
        }
    }

    private fun removeOldAttachStateChangeListener() {
        if (mAttachedViewRf != null) {
            val oldAttachedView = mAttachedViewRf!!.get()
            oldAttachedView?.removeOnAttachStateChangeListener(mOnAttachStateChangeListener)
        }
    }

    open fun getDecorView(): View? {
        var decorView: View? = null
        try {
            decorView = if (mWindow.background == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mWindow.contentView.parent as View
                } else {
                    mWindow.contentView
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mWindow.contentView.parent.parent as View
                } else {
                    mWindow.contentView.parent as View
                }
            }
        } catch (ignore: Exception) {
        }
        return decorView
    }

    protected fun showAtLocation(parent: View, x: Int, y: Int) {
        if (!ViewCompat.isAttachedToWindow(parent)) {
            return
        }
        removeOldAttachStateChangeListener()
        parent.addOnAttachStateChangeListener(mOnAttachStateChangeListener)
        mAttachedViewRf = WeakReference(parent)
        mWindow.showAtLocation(parent, Gravity.NO_GRAVITY, x, y)
        if (mDimAmount != QMUIBasePopup.DIM_AMOUNT_NOT_EXIST) {
            updateDimAmount(mDimAmount)
        }
    }

    fun setDismissIfOutsideTouch(dismissIfOutsideTouch: Boolean) {
        mWindow.isOutsideTouchable = dismissIfOutsideTouch
        if (dismissIfOutsideTouch) {
            mWindow.setTouchInterceptor(mOutsideTouchDismissListener)
        } else {
            mWindow.setTouchInterceptor(null)
        }
    }

    fun isShowing(): Boolean = mWindow.isShowing

    fun dismiss() {
        mWindow.dismiss()
    }

    private fun updateDimAmount(dimAmount: Float) {
        val decorView = getDecorView()
        if (decorView != null) {
            val p = decorView.layoutParams as WindowManager.LayoutParams
            p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            p.dimAmount = dimAmount
            modifyWindowLayoutParams(p)
            mWindowManager?.updateViewLayout(decorView, p)
        }
    }

    protected open fun modifyWindowLayoutParams(lp: WindowManager.LayoutParams) {}

    protected open fun onDismiss() {}
}