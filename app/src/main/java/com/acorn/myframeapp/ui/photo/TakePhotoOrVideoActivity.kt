package com.acorn.myframeapp.ui.photo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.acorn.basemodule.extendfun.showToast
import com.acorn.basemodule.utils.GlideEngine
import com.acorn.myframeapp.demo.BaseDemoActivity
import com.acorn.myframeapp.demo.BaseNoViewModelDemoActivity
import com.acorn.myframeapp.demo.Demo
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropImageEngine
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File


/**
 * https://github.com/LuckSiege/PictureSelector
 *
 * Created by acorn on 2022/5/27.
 */
class TakePhotoOrVideoActivity : BaseNoViewModelDemoActivity() {
    private val resultCallback = object : OnResultCallbackListener<LocalMedia> {
        override fun onResult(result: ArrayList<LocalMedia>?) {
            result ?: return
            for (res in result) {
                showToast("选择:${res.path}")
            }
        }

        override fun onCancel() {
            showToast("cancel")
        }
    }

    companion object {
        private const val CLICK_TAKE_PHOTO_BY_GALLERY = 0
        private const val CLICK_TAKE_PHOTO_BY_CAMERA = 1
        private const val CLICK_TAKE_VIDEO_BY_GALLERY = 2
        private const val CLICK_TAKE_VIDEO_BY_CAMERA = 3
    }

    override fun getItems(): Array<Demo> = arrayOf(
        Demo("Take photo from gallery", CLICK_TAKE_PHOTO_BY_GALLERY),
        Demo("Take photo from camera", CLICK_TAKE_PHOTO_BY_CAMERA),
        Demo("Take video from gallery", CLICK_TAKE_VIDEO_BY_GALLERY),
        Demo("Take video from camera", CLICK_TAKE_VIDEO_BY_CAMERA)
    )

    /**
     * .setSelectorUIStyle(); 设置相册主题
    .setLanguage(); 设置相册语言
    .setImageEngine(); 设置相册图片加载引擎
    .setCompressEngine(); 设置相册压缩引擎
    .setCropEngine(); 设置相册裁剪引擎
    .setSandboxFileEngine(); 设置相册沙盒目录拷贝引擎
    .setOriginalFileEngine(); 设置相册图片原图处理引擎
    .setExtendLoaderEngine(); 设置相册数据源加载引擎
    .setCameraInterceptListener(); 拦截相机事件，实现自定义相机
    .setEditMediaInterceptListener(); 拦截资源编辑事件，实现自定义编辑
    .setPermissionsInterceptListener(); 拦截相册权限处理事件，实现自定义权限
    .setSelectLimitTipsListener();拦截选择限制事件，可实现自定义提示
    .setSelectFilterListener();拦截不支持的选择项
    .isCameraForegroundService(); 拍照时是否开启一个前台服务
    .setRequestedOrientation(); 设置屏幕旋转方向
    .setSelectedData(); 相册已选数据
    .setRecyclerAnimationMode(); 相册列表动画效果
    .setImageSpanCount(); 相册列表每行显示个数
    .isDisplayCamera(); 是否显示相机入口
    .isPageStrategy(); 是否开启分页模式
    .selectionMode(); 单选或是多选
    .setMaxSelectNum(); 图片最大选择数量
    .setMinSelectNum(); 图片最小选择数量
    .setMaxVideoSelectNum(); 视频最大选择数量
    .setMinVideoSelectNum(); 视频最小选择数量
    .setRecordVideoMaxSecond(); 视频录制最大时长
    .setRecordVideoMinSecond(); 视频录制最小时长
    .setFilterVideoMaxSecond(); 过滤视频最大时长
    .setFilterVideoMinSecond(); 过滤视频最小时长
    .setSelectMaxDurationSecond(); 选择最大时长视频或音频
    .setSelectMinDurationSecond(); 选择最小时长视频或音频
    .setVideoQuality(); 系统相机录制视频质量
    .isQuickCapture(); 使用系统摄像机录制后，是否支持使用系统播放器立即播放视频
    .isPreviewAudio(); 是否支持音频预览
    .isPreviewImage(); 是否支持预览图片
    .isPreviewVideo(); 是否支持预览视频
    .isPreviewFullScreenMode(); 预览点击全屏效果
    .isEmptyResultReturn(); 支持未选择返回
    .isWithSelectVideoImage(); 是否支持视频图片同选
    .isSelectZoomAnim(); 选择缩略图缩放效果
    .isOpenClickSound(); 是否开启点击音效
    .isCameraAroundState(); 是否开启前置摄像头；系统相机 只支持部分机型
    .isCameraRotateImage(); 拍照是否纠正旋转图片
    .isGif(); 是否显示gif文件
    .isWebp(); 是否显示webp文件
    .isBmp(); 是否显示bmp文件
    .isHidePreviewDownload(); 是否隐藏预览下载功能
    .isAutoScalePreviewImage(); 预览图片自动放大充满屏幕
    .setOfAllCameraType(); isWithSelectVideoImage模式下相机优先使用权
    .isMaxSelectEnabledMask(); 达到最大选择数是否开启禁选蒙层
    .isSyncCover(); isPageModel模式下是否强制同步封面，默认false
    .isAutomaticTitleRecyclerTop(); 点击相册标题是否快速回到第一项
    .isFastSlidingSelect(); 快速滑动选择
    .isDirectReturnSingle(); 单选时是否立即返回
    .setCameraImageFormat(); 拍照图片输出格式
    .setCameraImageFormatForQ(); 拍照图片输出格式，Android Q以上
    .setCameraVideoFormat(); 拍照视频输出格式
    .setCameraVideoFormatForQ(); 拍照视频输出格式，Android Q以上
    .setOutputCameraDir(); 使用相机输出路径
    .setOutputAudioDir();使用录音输出路径
    .setOutputCameraImageFileName(); 图片输出文件名
    .setOutputCameraVideoFileName(); 视频输出文件名
    .setOutputAudioFileName(); 录音输出文件名
    .setQuerySandboxDir(); 查询指定目录下的资源
    .isOnlyObtainSandboxDir(); 是否只查询指定目录下的资源
    .setFilterMaxFileSize(); 过滤最大文件
    .setFilterMinFileSize(); 过滤最小文件
    .setSelectMaxFileSize(); 最大可选文件大小
    .setSelectMinFileSize(); 最小可选文件大小
    .setQueryOnlyMimeType(); 查询指定文件类型
    .setSkipCropMimeType(); 跳过不需要裁剪的类型
     */
    override fun onItemClick(data: Demo, idOrPosition: Int) {
        when (idOrPosition) {
            CLICK_TAKE_PHOTO_BY_GALLERY -> {
                takePhotoByGallery()
            }
            CLICK_TAKE_PHOTO_BY_CAMERA -> {
                PictureSelector.create(this)
                    .openCamera(SelectMimeType.ofImage())
                    .forResult(resultCallback)
            }
            CLICK_TAKE_VIDEO_BY_GALLERY -> {
                PictureSelector.create(this)
                    .openGallery(SelectMimeType.ofVideo())
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .setMaxSelectNum(1)
                    .forResult(resultCallback)
            }
            CLICK_TAKE_VIDEO_BY_CAMERA -> {
                PictureSelector.create(this)
                    .openCamera(SelectMimeType.ofVideo())
                    .forResult(resultCallback)
            }
            else -> {}
        }
    }

    private fun takePhotoByGallery() {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofAll())
            .setImageEngine(GlideEngine.createGlideEngine())
            .setMaxSelectNum(3)
            .setMinSelectNum(1)
            .setCompressEngine(CompressFileEngine { context, source, call ->
                Luban.with(this@TakePhotoOrVideoActivity)
                    .load(source)
                    .ignoreBy(100)
                    .setCompressListener(object : OnNewCompressListener {
                        override fun onStart() {

                        }

                        override fun onSuccess(source: String?, compressFile: File?) {
                            showToast("压缩成功")
                            call?.onCallback(source, compressFile?.absolutePath)
                        }

                        override fun onError(source: String?, e: Throwable?) {
                            showToast("压缩失败:${e?.message}")
                            call?.onCallback(source, null)
                        }
                    }).launch()
            })
            .setCropEngine { fragment, srcUri, destinationUri, dataSource, requestCode ->
                // 注意* 如果你实现自己的裁剪库，需要在Activity的.setResult();
                // Intent中需要给MediaStore.EXTRA_OUTPUT，塞入裁剪后的路径；如果有额外数据也可以通过CustomIntentKey.EXTRA_CUSTOM_EXTRA_DATA字段存入；
                val uCrop = UCrop.of(srcUri, destinationUri, dataSource)
                //ImageEngine根本没走?
                uCrop.setImageEngine(object : UCropImageEngine {
                    override fun loadImage(
                        context: Context?,
                        url: String?,
                        imageView: ImageView?
                    ) {
                        context ?: return
                        imageView ?: return
                        Glide.with(context).load(url).into(imageView)
                    }

                    override fun loadImage(
                        context: Context?,
                        url: Uri?,
                        maxWidth: Int,
                        maxHeight: Int,
                        call: UCropImageEngine.OnCallbackListener<Bitmap>?
                    ) {
                        context ?: return
                        Glide.with(context).asBitmap().load(url)
                            .into(object : CustomTarget<Bitmap>(maxWidth, maxHeight) {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    call?.onCall(resource)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                }

                            })
                    }
                })
                //有需要再写
//                        val option = UCrop.Options()
//                        option.isForbidCropGifWebp(false)
//                        uCrop.withOptions(option)
                uCrop.start(this@TakePhotoOrVideoActivity, fragment, requestCode)
            }
            .forResult(resultCallback)
    }

    override fun initView() {
        super.initView()
        showToolbar { it.title = "Photo or Video" }
    }
}