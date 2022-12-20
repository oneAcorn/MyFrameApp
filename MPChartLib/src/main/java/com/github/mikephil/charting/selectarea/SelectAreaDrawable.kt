package com.github.mikephil.charting.selectarea

import android.graphics.*
import android.graphics.drawable.Drawable
import com.acorn.basemodule.extendfun.dp
import kotlin.math.abs

/**
 * Created by acorn on 2022/9/5.
 */
class SelectAreaDrawable(private val minTouchSlop: Int) : Drawable() {
    var mDrawRect: RectF? = null
        private set
    private var startX = 0f
    private var startY = 0f
    private var h = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#203D7EFF")
        strokeWidth = 2f.dp
    }
    private var isDragMode = false

    override fun draw(canvas: Canvas) {
        val rect = mDrawRect ?: return
        canvas.drawRect(rect, paint)
    }

    fun setStartPoint(x: Float, y: Float) {
        reset()
        startX = x
//        startY = y
        //高度覆盖全屏
        startY = 0f
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        h = bounds.height().toFloat()
    }

    fun setMovedPoint(x: Float, y: Float) {
        if (isDragMode || abs(x - startX) > minTouchSlop) {
            isDragMode = true
            mDrawRect = RectF(startX, startY, x, h)
        }
        invalidateSelf()
    }

    fun reset() {
        isDragMode = false
        mDrawRect = null
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}