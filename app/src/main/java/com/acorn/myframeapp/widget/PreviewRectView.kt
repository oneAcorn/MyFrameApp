package com.acorn.myframeapp.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import com.acorn.basemodule.extendfun.dp
import com.acorn.basemodule.extendfun.getColorCompat
import com.acorn.myframeapp.R

/**
 * Created by acorn on 2023/1/12.
 */
class PreviewRectView : View {
    private var mRect = Rect()
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 2f.dp
    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        paint.color = context.getColorCompat(R.color.teal_200)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(mRect, paint)
    }

    fun setRect(rect: Rect) {
        this.mRect = rect
        invalidate()
    }
}