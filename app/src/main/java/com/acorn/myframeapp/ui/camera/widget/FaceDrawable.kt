package com.acorn.myframeapp.ui.camera.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.acorn.basemodule.extendfun.dp
import com.google.mlkit.vision.face.Face

/**
 * Created by acorn on 2023/2/1.
 */
class FaceDrawable(private val graphic: IGraphic) {
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 2f.dp
        color = Color.GREEN
    }

    fun draw(face: Face?, canvas: Canvas?) {
        face ?: return
        canvas ?: return
        // Draws a circle at the position of the detected face, with the face's track id below.
        val x: Float = graphic.translateX(face.boundingBox.centerX().toFloat())
        val y: Float = graphic.translateY(face.boundingBox.centerY().toFloat())

        // Calculate positions.
        val left: Float = x - graphic.scale(face.boundingBox.width() / 2.0f)
        val top: Float = y - graphic.scale(face.boundingBox.height() / 2.0f)
        val right: Float = x + graphic.scale(face.boundingBox.width() / 2.0f)
        val bottom: Float = y + graphic.scale(face.boundingBox.height() / 2.0f)
        canvas.drawRect(left, top, right, bottom, paint)
    }
}