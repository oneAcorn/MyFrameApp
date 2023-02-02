package com.acorn.myframeapp.ui.camera.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.acorn.basemodule.extendfun.dp
import com.acorn.basemodule.extendfun.getColorCompat
import com.acorn.basemodule.extendfun.logI
import com.acorn.myframeapp.R
import com.google.mlkit.vision.face.Face

/**
 * Created by acorn on 2023/1/12.
 */
class MLFaceDetectOverlay : View, IGraphic {
    private var mRect = Rect()
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 2f.dp
    }
    private val lock = Any()
    private var imageWidth = 0
    private var imageHeight = 0

    //是否需要更新Transformation
    private var needUpdateTransformation = true

    //摄像头翻转(前置摄像头时为true)
    private var isImageFlipped = false

    // The factor of overlay View size to image size. Anything in the image coordinates need to be
    // scaled by this amount to fit with the area of overlay View.
    private var scaleFactor = 1.0f

    // The number of horizontal pixels needed to be cropped on each side to fit the image with the
    // area of overlay View after scaling.
    private var postScaleWidthOffset = 0f

    // The number of vertical pixels needed to be cropped on each side to fit the image with the
    // area of overlay View after scaling.
    private var postScaleHeightOffset = 0f

    // Matrix for transforming from image coordinates to overlay view coordinates.
    private val transformationMatrix = Matrix()

    private val faceDrawable = FaceDrawable(this)
    private var face: Face? = null

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
        addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            needUpdateTransformation = true
        }
    }

    /**
     * Sets the source information of the image being processed by detectors, including size and
     * whether it is flipped, which informs how to transform image coordinates later.
     *
     * @param imageWidth the width of the image sent to ML Kit detectors
     * @param imageHeight the height of the image sent to ML Kit detectors
     * @param isFlipped whether the image is flipped. Should set it to true when the image is from the
     *     front camera.
     */
    fun setImageSourceInfo(imageWidth: Int, imageHeight: Int, isFlipped: Boolean) {
        assert(imageWidth > 0 && imageHeight > 0)
        synchronized(lock) {
            this.imageWidth = imageWidth
            this.imageHeight = imageHeight
            this.isImageFlipped = isFlipped
            this.needUpdateTransformation = true
        }
        postInvalidate()
    }

    private fun updateTransformationIfNeeded() {
        if (!needUpdateTransformation || imageWidth <= 0 || imageHeight <= 0) {
            return
        }
        val viewAspectRatio = width.toFloat() / height
        val imageAspectRatio = imageWidth.toFloat() / imageHeight
        postScaleWidthOffset = 0f
        postScaleHeightOffset = 0f
        if (viewAspectRatio > imageAspectRatio) {
            // The image needs to be vertically cropped to be displayed in this view.
            scaleFactor = width.toFloat() / imageWidth
            postScaleHeightOffset = (width.toFloat() / imageAspectRatio - height) / 2
        } else {
            // The image needs to be horizontally cropped to be displayed in this view.
            scaleFactor = height.toFloat() / imageHeight
            postScaleWidthOffset = (height.toFloat() * imageAspectRatio - width) / 2
        }
        logI("updateTransform:width:$width,height:$height.imageWidth:$imageWidth,imageHeight:$imageHeight \n" +
                "viewRatio:$viewAspectRatio,imageRatio:$imageAspectRatio \n" +
                "scaleFactor:$scaleFactor,widthOffset:$postScaleWidthOffset,heightOffset:$postScaleHeightOffset")
        transformationMatrix.reset()
        transformationMatrix.setScale(scaleFactor, scaleFactor)
        transformationMatrix.postTranslate(-postScaleWidthOffset, -postScaleHeightOffset)
        if (isImageFlipped) {
            transformationMatrix.postScale(-1f, 1f, width / 2f, height / 2f)
        }
        needUpdateTransformation = false
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        canvas?.drawRect(mRect, paint)
        synchronized(lock) {
            updateTransformationIfNeeded()
            //draw
            faceDrawable.draw(face, canvas)
        }
    }

    fun updateFace(face: Face) {
        this.face = face
        postInvalidate()
    }

    fun setRect(rect: Rect) {
        this.mRect = rect
        invalidate()
    }

    override fun scale(imagePixel: Float): Float {
        return imagePixel * scaleFactor
    }

    override fun translateX(x: Float): Float {
        return if (isImageFlipped) {
            width - (scale(x) - postScaleWidthOffset)
        } else {
            scale(x) - postScaleWidthOffset
        }
    }

    override fun translateY(y: Float): Float {
        return scale(y) - postScaleHeightOffset;
    }
}