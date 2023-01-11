package com.acorn.myframeapp.ui.camera.face

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.acorn.basemodule.extendfun.logE
import com.acorn.basemodule.extendfun.logI
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

/**
 * Created by acorn on 2023/1/11.
 */
class FaceImageAnalyzer : ImageAnalysis.Analyzer {
    private val highAccuracyOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()
    private val detector = FaceDetection.getClient(highAccuracyOpts)


    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        logI("face analyze")
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            detector.process(image)
                .addOnSuccessListener {
                    logI("face analyze success:$it")
                    //分析完图片需要关闭
                    //The image needs to be closed by calling close when the analyzing is done.
                    imageProxy.close()
                }
                .addOnFailureListener {
                    logE(it)
                    //The image needs to be closed by calling close when the analyzing is done.
                    imageProxy.close()
                }
        }
    }
}