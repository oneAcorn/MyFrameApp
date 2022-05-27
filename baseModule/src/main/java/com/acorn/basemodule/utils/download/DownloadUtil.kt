package com.acorn.basemodule.utils.download

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import com.acorn.basemodule.utils.ApkInstallUtils
import java.io.File


/**
 * Created by acorn on 2020/5/12.
 */
class DownloadUtil(
    val context: Context,
    private val downloadConfig: DownloadConfig
) {
    private lateinit var downloadManager: DownloadManager
    private var downloadPath: String? = null
    private var downloadId: Long? = 0L
    private var receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            checkDownloadStatues()
        }
    }

    fun download(url: String, callback: (downloadID: Long?) -> Unit) {
        DownloadManager.Request(Uri.parse(url)).apply {
            setAllowedOverRoaming(false) //移动网络是否允许漫游
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            setTitle(downloadConfig.downloadTitle ?: downloadConfig.downloadApkName)
            setDescription(downloadConfig.downloadDescription ?: "下载中")
            setVisibleInDownloadsUi(true)

            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                downloadConfig.downloadApkName
            )
            if (file.exists()) {
                file.delete()
            }
            downloadPath = file.absolutePath
            setDestinationUri(Uri.fromFile(file))

            downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadId = downloadManager.enqueue(this)
            callback(downloadId)
        }

        context.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun checkDownloadStatues() {
        downloadId ?: return
        val query = DownloadManager.Query()
        query.setFilterById(downloadId!!)
        val cursor = downloadManager.query(query)
        if (cursor?.moveToFirst() == true) {
            val index = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            if (index < 0)
                return
            when (cursor.getInt(index)) {
                DownloadManager.STATUS_RUNNING -> { //下载中,这个广播只能接收下载完成或失败
//                    //已下载的文件大小
//                    val downSize = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
//                    //总文件大小
//                    val totalSize = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
//                    logI("sfdownload downSize:$downSize,totalSize:$totalSize")
                }
                DownloadManager.STATUS_SUCCESSFUL -> { //下载完成
                    cursor.close()
                    context.unregisterReceiver(receiver)
                    notifyInstall(getNotifyInstallBuilder())
                    if (downloadPath != null){
                        ApkInstallUtils.installApk(File(downloadPath!!))
                    }
                }
                DownloadManager.STATUS_FAILED -> { //下载失败
                    cursor.close()
                    context.unregisterReceiver(receiver)
                }
            }
        }
    }

    /**
     * 下载完成后，通知下载完成，并设置点击安装功能
     */
    private fun notifyInstall(builder: Notification.Builder) {
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (downloadPath != null) {
            val openintent = ApkInstallUtils.getInstallIntent(File(downloadPath!!))
            val contentIntent =
                PendingIntent.getActivity(context, 0, openintent, 0) //当点击消息时就会向系统发送openintent意图
            builder.setContentIntent(contentIntent)
            builder.setAutoCancel(true)
        }
        manager.notify(1010, builder.build())
    }


    private fun getNotifyInstallBuilder(): Notification.Builder {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder: Notification.Builder = Notification.Builder(context.applicationContext)
        builder
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources, downloadConfig.largeIcon
                        ?: 0
                )
            ) //设置下拉菜单中的图标
            .setContentTitle(downloadConfig.downloadFinishTitle ?: "下载完成") //设置下拉菜单的标题
            .setSmallIcon(downloadConfig.smallIcon ?: 0) //设置状态栏的小图标
            .setContentText(downloadConfig.downloadFinishDescription) //设置上下文内容
            .setWhen(System.currentTimeMillis()) //设置通知发生时间
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //android 0以上需要设置channel
            builder.setChannelId(createAndGetchannelId(manager))
        }
        return builder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAndGetchannelId(manager: NotificationManager): String {
        val channelID = "GrtTrainning"
        val channel = NotificationChannel(
            channelID, downloadConfig.downloadChannelTitle
                ?: "下载更新", NotificationManager.IMPORTANCE_HIGH
        )
        channel.enableLights(true) //设置提示灯
        channel.lightColor = Color.GREEN //提示灯颜色
        channel.setShowBadge(true) //显示logo
        channel.description = downloadConfig.downloadChannelDescription ?: "下载中" //设置描述
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC //设置锁屏可见
        manager.createNotificationChannel(channel)
        return channelID
    }

    class Builder {
        private val downloadConfig = DownloadConfig()

        /**
         * 设置下载文件名称
         */
        fun setDownloadApkName(name: String): Builder {
            downloadConfig.downloadApkName = name
            return this
        }

        /**
         * 设置下载管理器中的标题
         */
        fun setDownloadTitle(title: String): Builder {
            downloadConfig.downloadTitle = title
            return this
        }

        /**
         * 设置下载管理器中的描述
         */
        fun setDownloadDescription(description: String): Builder {
            downloadConfig.downloadDescription = description
            return this
        }

        /**
         * 设置下载完成后通知栏的标题
         */
        fun setDownloadFinishTitle(title: String): Builder {
            downloadConfig.downloadFinishTitle = title
            return this
        }

        /**
         * 设置下载完成后通知栏的描述
         */
        fun setDownloadFinishDescription(description: String): Builder {
            downloadConfig.downloadFinishDescription = description
            return this
        }

        /**
         * 设置Channel标题
         */
        fun setDownloadChannelTitle(title: String): Builder {
            downloadConfig.downloadChannelTitle = title
            return this
        }

        /**
         * 设置Channel描述
         */
        fun setDownloadChannelDescription(description: String): Builder {
            downloadConfig.downloadChannelDescription = description
            return this
        }

        /**
         * 设置通知栏大图标
         */
        fun setLargeIcon(@DrawableRes res: Int): Builder {
            downloadConfig.largeIcon = res
            return this
        }

        /**
         * 设置通知栏小图标
         */
        fun setSmallIcon(@DrawableRes res: Int): Builder {
            downloadConfig.smallIcon = res
            return this
        }

        fun build(): DownloadConfig {
            return downloadConfig
        }
    }

    class DownloadConfig {
        var downloadApkName: String = "downloadApk.apk"
        var downloadTitle: String? = null
        var downloadDescription: String? = null
        var downloadFinishTitle: String? = null
        var downloadFinishDescription: String? = null
        var downloadChannelTitle: String? = null
        var downloadChannelDescription: String? = null
        var largeIcon: Int? = null
        var smallIcon: Int? = null
    }
}