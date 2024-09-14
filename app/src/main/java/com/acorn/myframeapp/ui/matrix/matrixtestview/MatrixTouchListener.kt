package com.acorn.myframeapp.ui.matrix.matrixtestview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View

/**
 * Created by acorn on 2024/3/6.
 */
class MatrixTouchListener(
    private val context: Context,
    private val touchMatrix: Matrix,
    private val dragTriggerDistance: Float
) : SimpleOnGestureListener(),
    View.OnTouchListener {
    private var mVelocityTracker: VelocityTracker? = null
    private var mTouchMode = TouchGesture.NONE
    private val mGestureDetector = GestureDetector(context,this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker?.addMovement(event)
        if (event.actionMasked == MotionEvent.ACTION_CANCEL) {
            if (mVelocityTracker != null) {
                mVelocityTracker?.recycle()
                mVelocityTracker = null
            }
        }

        if (mTouchMode == TouchGesture.NONE) {
            mGestureDetector.onTouchEvent(event)
        }

        // perform the transformation, update the chart
//        mMatrix = mChart.getViewPortHandler().refresh(mMatrix, mChart, true)

        return true // indicate event was handled
    }
}

enum class TouchGesture {
    NONE, DRAG, FLING
}