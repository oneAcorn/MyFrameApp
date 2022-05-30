package com.acorn.basemodule.base

import android.content.Context
import android.view.*
import android.widget.FrameLayout
import com.qmuiteam.qmui.widget.IBlankTouchDetector
import com.qmuiteam.qmui.widget.popup.QMUIBasePopup

/**
 * Created by acorn on 2022/5/30.
 */
abstract class FullScreenPopup(private val context: Context) : BasePopup(context) {
    var onBlankClickCallback: ((FullScreenPopup) -> Unit)? = null
    var mAnimStyle = QMUIBasePopup.NOT_SET

    init {
        mWindow.width = ViewGroup.LayoutParams.MATCH_PARENT
        mWindow.height = ViewGroup.LayoutParams.MATCH_PARENT
        mWindow.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        mDimAmount = 0.6f
    }

    fun show(parent: View) {
        if (isShowing())
            return
        val rootView = RootView(context)
        LayoutInflater.from(context).inflate(getLayoutId(), rootView)
        initView(rootView)
        mWindow.contentView = rootView
        if (mAnimStyle != NOT_SET) {
            mWindow.animationStyle = mAnimStyle
        }
        showAtLocation(parent, 0, 0)
    }

    protected abstract fun getLayoutId(): Int

    protected open fun initView(view: View) {}

    override fun modifyWindowLayoutParams(lp: WindowManager.LayoutParams) {
        //java代码 lp.flags |= FLAG_LAYOUT_IN_SCREEN | FLAG_LAYOUT_INSET_DECOR;
        lp.flags =
            lp.flags or (WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR)
        super.modifyWindowLayoutParams(lp)
    }

    inner class RootView(context: Context) : FrameLayout(context) {
        private var mShouldInvokeBlackClickWhenTouchUp = false

        /**
         * 因为是全屏的,所以setDismissIfOutsideTouch是无效的,需要自己实现触摸事件.
         */
        override fun onTouchEvent(event: MotionEvent?): Boolean {
            val action = event!!.actionMasked
            if (onBlankClickCallback == null) {
                return true
            }
            if (action == MotionEvent.ACTION_DOWN) {
                mShouldInvokeBlackClickWhenTouchUp = isTouchInBlack(event)
            } else if (action == MotionEvent.ACTION_MOVE) {
                mShouldInvokeBlackClickWhenTouchUp =
                    mShouldInvokeBlackClickWhenTouchUp && isTouchInBlack(event)
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                mShouldInvokeBlackClickWhenTouchUp =
                    mShouldInvokeBlackClickWhenTouchUp && isTouchInBlack(event)
                if (mShouldInvokeBlackClickWhenTouchUp) {
                    onBlankClickCallback?.invoke(this@FullScreenPopup)
                }
            }
            return true
        }

        private fun isTouchInBlack(event: MotionEvent): Boolean {
            val childView = findChildViewUnder(event.x, event.y)
            var isBlank = childView == null
            if (!isBlank && childView is IBlankTouchDetector) {
                val e = MotionEvent.obtain(event)
                val offsetX = scrollX - childView.left
                val offsetY = scrollY - childView.top
                e.offsetLocation(offsetX.toFloat(), offsetY.toFloat())
                isBlank = (childView as IBlankTouchDetector).isTouchInBlank(e)
                e.recycle()
            }
            return isBlank
        }


        private fun findChildViewUnder(x: Float, y: Float): View? {
            val count = childCount
            for (i in count - 1 downTo 0) {
                val child = getChildAt(i)
                val translationX = child.translationX
                val translationY = child.translationY
                if (x >= child.left + translationX && x <= child.right + translationX && y >= child.top + translationY && y <= child.bottom + translationY) {
                    return child
                }
            }
            return null
        }
    }
}