package com.acorn.basemodule.utils.download

import android.content.Context
import com.acorn.basemodule.R

/**
 * Created by acorn on 2020/6/19.
 */
class ApkDownloadUtil(val context: Context) {
    private val downloadUtil = DownloadUtil(context,
            DownloadUtil.Builder().setDownloadTitle("App名称")
                    .setDownloadDescription("下载中")
                    .setDownloadApkName("abc.apk")
                    .setDownloadFinishTitle("App名称")
                    .setDownloadFinishDescription("下载完成，点击安装")
                    .setDownloadChannelTitle("下载APP更新")
                    .setDownloadChannelDescription("下载App名称")
                    .setLargeIcon(R.drawable.ic_baseline_cloud_queue_24)
                    .setSmallIcon(R.drawable.ic_baseline_cloud_queue_24)
                    .build()
    )

    fun download(url: String, callback: (downloadID: Long?) -> Unit) {
        downloadUtil.download(url, callback)
    }

}