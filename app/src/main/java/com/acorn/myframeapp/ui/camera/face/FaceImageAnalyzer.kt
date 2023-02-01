package com.acorn.myframeapp.ui.camera.face

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Handler
import android.os.Looper
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.core.graphics.toRectF
import com.acorn.basemodule.extendfun.logE
import com.acorn.basemodule.extendfun.logI
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceContour.ContourType
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

/**
 * @param interval 检测人脸间隔时间(ms)
 * @param callback 检测到的第一个人脸(复数取第一个)
 * 脸部框对不齐问题还没解决,想解决可参考
 * https://github.com/googlesamples/mlkit/blob/master/android/vision-quickstart/app/src/main/java/com/google/mlkit/vision/demo/GraphicOverlay.java
 * GraphicOverlay中的updateTransformationIfNeeded()方法
 * 和FaceGraphic中的draw()方法
 * Created by acorn on 2023/1/11.
 */
class FaceImageAnalyzer(
    private val interval: Long = 300L,
    private val callback: ((face: Face, percentRectF: RectF) -> Unit)? = null
) :
    ImageAnalysis.Analyzer {
    private val opts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE) //性能模式,高精度模式
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL) //地标(眼睛,嘴巴,眉毛等的位置)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE) //按类别分类
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .build()
    private val detector = FaceDetection.getClient(opts)
    private val handler = Handler(Looper.getMainLooper())


    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
//        logI("face analyze")
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
//            logI("image:${image.byteBuffer}")
            // Pass image to an ML Kit Vision API
            detector.process(image)
                .addOnSuccessListener {
//                    val bitmap = getBitmap(
//                        image.byteBuffer!!,
//                        mediaImage.cropRect.width(),
//                        mediaImage.cropRect.height()
//                    )
//                    logI("face analyze success:$it,${mediaImage.cropRect}")
                    it.takeIf { it.isNotEmpty() }?.get(0)?.let { face ->
                        val leftEyeLandmark = face.getLandmark(FaceLandmark.LEFT_EYE)
                        val rightEyeLandmark = face.getLandmark(FaceLandmark.RIGHT_EYE)
                        val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                        val rightEyeContour = face.getContour(FaceContour.RIGHT_EYE)?.points
                        logI(
                            "face analyze callback:${face.boundingBox},${mediaImage.cropRect},${image.rotationDegrees},\n" +
                                    "leftEye:${leftEyeContour?.size},rightEye:${rightEyeContour?.size},leftEyeLandmark:${leftEyeLandmark},rightEyeLandmark:$rightEyeLandmark"
                        )
                        val faceRect = face.boundingBox.toRectF()
                        val frameRect = mediaImage.cropRect.toRectF()
                        val frameWidth = frameRect.width()
                        val frameHeight = frameRect.height()
                        val leftPercent = faceRect.left / frameWidth
                        val topPercent = faceRect.top / frameHeight
                        val rightPercent = faceRect.right / frameWidth
                        val bottomPercent = faceRect.bottom / frameHeight
                        callback?.invoke(
                            face,
                            RectF(leftPercent, topPercent, rightPercent, bottomPercent)
                        )
                    }
                    //分析完图片需要关闭
                    //The image needs to be closed by calling close when the analyzing is done.
                    imageProxy.closeDelay(interval)
                }
                .addOnFailureListener {
                    logE(it)
                    //The image needs to be closed by calling close when the analyzing is done.
                    imageProxy.closeDelay(interval)
                }
        }
    }

    private fun getBitmap(data: ByteBuffer, width: Int, height: Int): Bitmap? {
        data.rewind()
        val imageInBuffer = ByteArray(data.limit())
        data.get(imageInBuffer, 0, imageInBuffer.size)
        try {
            val image = YuvImage(
                imageInBuffer, ImageFormat.NV21, width, height, null
            )
            val stream = ByteArrayOutputStream()
            image.compressToJpeg(Rect(0, 0, width, height), 80, stream)
            val bmp: Bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size())
            stream.close()
            return bmp
        } catch (e: Exception) {
            logE(e)
        }
        return null
    }

    private fun ImageProxy.closeDelay(ms: Long) {
        if (ms < 30) {
            close()
        } else {
            handler.postDelayed({ close() }, ms)
        }
    }
}