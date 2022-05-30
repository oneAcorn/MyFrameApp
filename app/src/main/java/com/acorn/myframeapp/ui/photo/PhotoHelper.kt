package com.acorn.myframeapp.ui.photo

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.provider.MediaStore
import android.webkit.ValueCallback
import android.webkit.WebView
import com.acorn.myframeapp.R
import com.luck.picture.lib.animators.AnimationType
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import java.io.File

/**
 * 商铺选址
 * Created by acorn on 2022/4/12.
 */
class PhotoHelper {
    private val REQUESTCODE_TAKEPHOTO_BY_CAMERA = 2001;
    private val REQUESTCODE_TAKEPHOTO_BY_GALLERY = 2002;
    private val REQUESTCODE_TAKEVIDEO_BY_CAMERA = 2003;
    private val REQUESTCODE_TAKEVIDEO_BY_GALLERY = 2004;

    var uploadFile: ValueCallback<Array<Uri>>? = null

    //android5.0以下使用的
    var uploadFileOld: ValueCallback<Uri>? = null

    fun takePhotoByCamera(context: Activity) {
    }

    fun takePhotoByGallery(context: Activity) {
    }

    fun takeVideoByCamera(context: Activity) {
    }

    fun takeVideoByGallery(context: Activity) {
    }

    fun onActivityResult(
        resultCode: Int,
        requestCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUESTCODE_TAKEPHOTO_BY_GALLERY || requestCode == REQUESTCODE_TAKEVIDEO_BY_GALLERY ||
            requestCode == REQUESTCODE_TAKEVIDEO_BY_CAMERA
        ) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //解决onShowFileChooser无法多次触发问题
                //无论是否取消选择文件,都要调用onReceiveValue(),
                uploadFile?.onReceiveValue(null)
                uploadFile = null
                uploadFileOld?.onReceiveValue(null)
                uploadFileOld = null
            } else if (resultCode == Activity.RESULT_OK) {
                val result = data?.data ?: return
                uploadFile?.onReceiveValue(arrayOf(result))
                uploadFile = null
                uploadFileOld?.onReceiveValue(result)
                uploadFileOld = null
            }
        }
    }

}