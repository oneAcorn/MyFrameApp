package com.acorn.basemodule.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.acorn.basemodule.extendfun.appContext
import java.io.File

/**
 * Created by acorn on 2022/5/27.
 */
object ApkInstallUtils {
    fun installApk(file: File) {
        val intent = getInstallIntent(file)
        appContext.startActivity(intent)
    }

    fun getInstallIntent(file: File):Intent{
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = getVersionUri(appContext, file, intent)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        return intent
    }

    private fun getVersionUri(context: Context, file: File, intent: Intent?): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent?.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent?.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            FileProvider.getUriForFile(context, context.applicationContext.packageName + ".fileprovider", file)
        } else {
            Uri.fromFile(file)
        }
    }
}