package com.acorn.myframeapp.ui.matrix.matrixtestview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

class ZoomableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs), ScaleGestureDetector.OnScaleGestureListener {

    private val matrix = Matrix()
    private var scale = 1f
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var bitmap: Bitmap? = null

    init {
        scaleGestureDetector = ScaleGestureDetector(context, this)
    }

    fun setImage(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap?.let {
            canvas.save()
            canvas.concat(matrix)
            canvas.drawBitmap(it, 0f, 0f, null)
            canvas.restore()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return true
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        val scaleFactor = detector.scaleFactor
        scale *= scaleFactor
        scale = Math.max(0.1f, Math.min(scale, 10.0f))

        matrix.setScale(scale, scale, width / 2f, height / 2f)
        invalidate()
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean = true

    override fun onScaleEnd(detector: ScaleGestureDetector) {}

}
