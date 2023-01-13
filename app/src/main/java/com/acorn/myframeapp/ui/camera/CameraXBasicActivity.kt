package com.acorn.myframeapp.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.core.graphics.toRect
import androidx.core.util.Consumer
import com.acorn.basemodule.base.BaseBindingActivity
import com.acorn.basemodule.extendfun.logE
import com.acorn.basemodule.extendfun.logI
import com.acorn.basemodule.extendfun.requestPermission
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.databinding.ActivityCameraxBasicBinding
import com.acorn.myframeapp.ui.camera.face.FaceImageAnalyzer
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * Created by acorn on 2023/1/11.
 */
class CameraXBasicActivity : BaseBindingActivity<BaseNetViewModel, ActivityCameraxBasicBinding>() {
    private lateinit var cameraController: LifecycleCameraController//声明一个CameraController
    private lateinit var cameraExecutor: ExecutorService //相机需要执行任务的线程池 后续会用到
    private var lensFacing = 0
    private var recording: Recording? = null
    private val faceAnalyzer = FaceImageAnalyzer { face, percentRectF ->
        binding.previewView.bitmap?.let {
            binding.leftIv.setImageBitmap(it)
            val bitmapWidth = it.width.toFloat()
            val bitmapHeight = it.height.toFloat()
            val left = percentRectF.left * bitmapWidth
            val top = percentRectF.top * bitmapHeight
            val right = percentRectF.right * bitmapWidth
            val bottom = percentRectF.bottom * bitmapHeight
            val realRect = RectF(left, top, right, bottom).toRect()
            binding.previewRectView.setRect(realRect)
//            Bitmap.createBitmap(
//                it,
//                left.toInt(),
//                top.toInt(),
//                realRect.width(),
//                realRect.height()
//            )?.let { bmp ->
//                binding.rightIv.setImageBitmap(bmp)
//            }
        }

    }

    override fun initView() {
        super.initView()
        requestPermission(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            allPermGrantedCallback = {
                initController()
            },
            anyPermDeniedCallback = {

            })
    }

    private fun initController() {
        cameraExecutor = Executors.newSingleThreadExecutor()//因为只能同时执行一个任务
        cameraController = LifecycleCameraController(this)
        cameraController.bindToLifecycle(this)
        cameraController.imageCaptureFlashMode = ImageCapture.FLASH_MODE_AUTO
        cameraController.cameraSelector =
            CameraSelector.Builder().requireLensFacing(lensFacing).build()
        //人脸检测
        cameraController.setImageAnalysisAnalyzer(cameraExecutor, faceAnalyzer)
        binding.previewView.controller = cameraController
    }

    override fun initListener() {
        super.initListener()
        binding.switchBtn.singleClick {
            switchCamera()
        }
        binding.takePicBtn.singleClick {
            takePicture()
        }
        binding.recordBtn.singleClick {
            toggleRecord()
        }
    }

    private fun switchCamera() {
        lensFacing = 1 - lensFacing
        cameraController.cameraSelector =
            CameraSelector.Builder().requireLensFacing(lensFacing).build()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun takePicture() {
        val photoFile = File(cacheDir, "abc.jpg")
        val metadata = ImageCapture.Metadata().apply {
            //水平翻转
            isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
        }
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .setMetadata(metadata)
            .build()
        cameraController.setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.IMAGE_ANALYSIS)
        cameraController.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
//                    logI("Photo capture failed: ${exc.message}", exc)
                    logE(exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    logI("Photo capture succeeded: $savedUri")
                }
            })
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun toggleRecord() {
        if (cameraController.isRecording) {
            stopRecord()
        } else {
            startRecord()
        }
    }

    @SuppressLint("UnsafeOptInUsageError", "MissingPermission")
    private fun startRecord() {
        if (cameraController.isRecording) {
            showTip("正在录")
            return
        }

        binding.recordBtn.text = "stop recording"

        val videoFile = File(cacheDir, "abcd.mp4")
        val outputOptions = FileOutputOptions.Builder(videoFile).build()
        cameraController.setEnabledUseCases(CameraController.VIDEO_CAPTURE)
        recording = cameraController.startRecording(
            outputOptions,
            AudioConfig.create(true),
            cameraExecutor
        ) { t -> logI("camera record event:$t") }
    }

    private fun stopRecord() {
        binding.recordBtn.text = "start record"
        recording?.stop()
        recording = null
    }

    override fun getViewModel(): BaseNetViewModel? = null
}