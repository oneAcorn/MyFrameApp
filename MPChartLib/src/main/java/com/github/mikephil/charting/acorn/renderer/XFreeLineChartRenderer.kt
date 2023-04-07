package com.github.mikephil.charting.acorn.renderer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.acorn.basemodule.extendfun.logI
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.renderer.DataRenderer
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ViewPortHandler
import java.lang.ref.WeakReference

/**
 * Created by acorn on 2023/4/7.
 */
class XFreeLineChartRenderer(
    private val mChart: LineDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : DataRenderer(animator, viewPortHandler) {

    /**
     * paint for the inner circle of the value indicators
     */
    private val mCirclePaintInner: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    /**
     * Bitmap object used for drawing the paths (otherwise they are too long if
     * rendered directly on the canvas)
     */
    private var mDrawBitmap: WeakReference<Bitmap>? = null

    /**
     * on this canvas, the paths are rendered, it is initialized with the
     * pathBitmap
     */
    private var mBitmapCanvas: Canvas? = null

    /**
     * the bitmap configuration to be used
     */
    private var mBitmapConfig = Bitmap.Config.ARGB_8888

    private var mLineBuffer = FloatArray(4)

    override fun initBuffers() {
    }

    override fun drawData(c: Canvas?) {
        c ?: return
        val width = mViewPortHandler.chartWidth.toInt()
        val height = mViewPortHandler.chartHeight.toInt()

        var drawBitmap = if (mDrawBitmap == null) null else mDrawBitmap!!.get()

        if (drawBitmap == null || drawBitmap.width != width || drawBitmap.height != height) {
            if (width > 0 && height > 0) {
                drawBitmap = Bitmap.createBitmap(width, height, mBitmapConfig)
                mDrawBitmap = WeakReference(drawBitmap)
                mBitmapCanvas = Canvas(drawBitmap)
            } else return
        }
        drawBitmap ?: return
        drawBitmap.eraseColor(Color.TRANSPARENT)

        for (set in mChart.lineData.dataSets) {
            if (!set.isVisible) continue
            drawDataSet(c, set)
        }
        c.drawBitmap(drawBitmap, 0f, 0f, mRenderPaint)
    }

    private fun drawDataSet(c: Canvas, dataSet: ILineDataSet) {
        val entryCount = dataSet.entryCount
        if (entryCount < 1) return
        mRenderPaint.strokeWidth = dataSet.lineWidth
        mRenderPaint.pathEffect = dataSet.dashPathEffect

        val trans = mChart.getTransformer(dataSet.axisDependency)
        val phaseY = mAnimator.phaseY
        mRenderPaint.style = Paint.Style.STROKE
        val pointsPerEntryPair = 2

        // only one color per dataset
        if (mLineBuffer.size < Math.max(entryCount * pointsPerEntryPair, pointsPerEntryPair) * 2) {
            mLineBuffer =
                FloatArray(Math.max(entryCount * pointsPerEntryPair, pointsPerEntryPair) * 4)
        }
        var j = 0
        for (i in 0 until entryCount) {
            val e1 = dataSet.getEntryForIndex(if (i == 0) 0 else i - 1)
            val e2 = dataSet.getEntryForIndex(i)
            if (e1 == null || e2 == null) continue

            mLineBuffer[j++] = e1.x
            mLineBuffer[j++] = e1.y * phaseY

            mLineBuffer[j++] = e2.x
            mLineBuffer[j++] = e2.y * phaseY
        }
        if (j > 0) { //需要绘制线条
            trans.pointValuesToPixel(mLineBuffer)

            val size = Math.max((entryCount) * pointsPerEntryPair, pointsPerEntryPair) * 2

            mRenderPaint.color = dataSet.color

            c.drawLines(mLineBuffer, 0, size, mRenderPaint)
        }

        mRenderPaint.pathEffect = null
    }

    override fun drawValues(c: Canvas?) {
    }

    override fun drawExtras(c: Canvas?) {
        drawCircles(c)
    }

    /**
     * cache for the circle bitmaps of all datasets
     */
    private val mImageCaches = HashMap<IDataSet<*>, DataSetImageCache>()

    /**
     * buffer for drawing the circles
     */
    private val mCirclesBuffer = FloatArray(2)

    private fun drawCircles(c: Canvas?) {
        c ?: return
        mRenderPaint.style = Paint.Style.FILL

        val phaseY = mAnimator.phaseY

        mCirclesBuffer[0] = 0f
        mCirclesBuffer[1] = 0f
        val dataSets = mChart.lineData.dataSets
        for (dataSet in dataSets) {
            if (!dataSet.isVisible || !dataSet.isDrawCirclesEnabled || dataSet.entryCount == 0) continue
            mCirclePaintInner.color = dataSet.circleHoleColor

            val trans = mChart.getTransformer(dataSet.axisDependency)

            val circleRadius = dataSet.circleRadius
            val circleHoleRadius = dataSet.circleHoleRadius
            val drawCircleHole =
                dataSet.isDrawCircleHoleEnabled && circleHoleRadius < circleRadius && circleHoleRadius > 0f
            val drawTransparentCircleHole =
                drawCircleHole && dataSet.circleHoleColor == ColorTemplate.COLOR_NONE

            var imageCache: DataSetImageCache

            if (mImageCaches.containsKey(dataSet)) {
                imageCache = mImageCaches.get(dataSet)!!
            } else {
                imageCache = DataSetImageCache(mRenderPaint, mCirclePaintInner)
                mImageCaches.put(dataSet, imageCache)
            }

            val changeRequired = imageCache.init(dataSet)

            // only fill the cache with new bitmaps if a change is required
            if (changeRequired) {
                imageCache.fill(dataSet, drawCircleHole, drawTransparentCircleHole)
            }

            val entryCount = dataSet.entryCount
            for (j in 0 until entryCount) {
                val e = dataSet.getEntryForIndex(j) ?: continue
                mCirclesBuffer[0] = e.x
                mCirclesBuffer[1] = e.y * phaseY
                trans.pointValuesToPixel(mCirclesBuffer)
                if (!mViewPortHandler.isInBoundsRight(mCirclesBuffer[0])) {
                    continue
                }

                if (!mViewPortHandler.isInBoundsLeft(mCirclesBuffer[0]) ||
                    !mViewPortHandler.isInBoundsY(mCirclesBuffer[1])
                ) {
                    continue
                }
                val circleBitmap = imageCache.getBitmap(j)

                if (circleBitmap != null) {
                    c.drawBitmap(
                        circleBitmap,
                        mCirclesBuffer[0] - circleRadius,
                        mCirclesBuffer[1] - circleRadius,
                        null
                    )
                }
            }
        }
    }

    override fun drawHighlighted(c: Canvas?, indices: Array<out Highlight>?) {
    }

    fun releaseBitmap() {
        if (mBitmapCanvas != null) {
            mBitmapCanvas?.setBitmap(null)
            mBitmapCanvas = null
        }
        if (mDrawBitmap != null) {
            val drawBitmap = mDrawBitmap?.get()
            drawBitmap?.recycle()
            mDrawBitmap?.clear()
            mDrawBitmap = null
        }
    }
}