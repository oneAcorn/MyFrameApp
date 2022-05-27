package com.acorn.basemodule.utils.download

import android.app.DownloadManager
import android.content.Context
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by acorn on 2020/5/15.
 */
class DownloadProgressWatcher(context: Context, downloadId: Long) {
    private val downloadManager: DownloadManager by lazy { context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }
    private val query by lazy { DownloadManager.Query().apply { setFilterById(downloadId) } }
    private var disposable: Disposable? = null

    fun watchProgress(watchCallback: (downSize: Int, totalSize: Int) -> Unit, errListener: (String) -> Unit) {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val cursor = downloadManager.query(query)
                    if (cursor == null) {
                        errListener("下载失败")
                        return@subscribe
                    }
                    if (!cursor.moveToFirst()) {
                        errListener("下载失败")
                        stop()
                        return@subscribe
                    }
                    //已下载的文件大小
                    val downSize = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    //总文件大小
                    val totalSize = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    watchCallback(downSize, totalSize)
                    if (!cursor.isClosed) {
                        cursor.close()
                    }
                    //totalSize在小米刚启用下载时上有可能为-1
                    if (totalSize > 0 && downSize >= totalSize) { //下载完成
                        disposable?.dispose()
                    }
                }
    }

    fun stop() {
        if (disposable?.isDisposed == false)
            disposable?.dispose()
    }
}
