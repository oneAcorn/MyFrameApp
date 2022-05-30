package com.acorn.myframeapp.ui.webview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.acorn.basemodule.base.BaseFragment
import com.acorn.basemodule.extendfun.showToast
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.sdk.*
import kotlinx.android.synthetic.main.fragment_x5_webview.*

/**
 * 参考我的项目sqapp->HTMLActivity,imcore_android->CommonX5WebFragment
 * Created by acorn on 2022/5/29.
 */
class X5WebviewFragment : BaseFragment<BaseNetViewModel>() {
    private var webView: WebView? = null
    private var url: String? = null
    private var webviewCallback: IWebviewCallback? = null

    //固定标题,不根据网页标题变化
    private var isFixedTitle: Boolean = false
    private var title: String? = null

    //对网页选择文件的处理,具体参考我的项目sqapp->PhotoHelper
    var uploadFile: android.webkit.ValueCallback<Array<Uri>>? = null

    //android5.0以下使用的
    var uploadFileOld: android.webkit.ValueCallback<Uri>? = null

    companion object {
        fun newInstance(
            url: String?,
            title: String? = null,
            isFixedTitle: Boolean = false,
            webviewCallback: IWebviewCallback? = null
        ): X5WebviewFragment {
            val args = Bundle()
            args.putString("url", url)
            args.putString("title", title)
            args.putBoolean("isFixedTitle", isFixedTitle)
            val fragment = X5WebviewFragment()
            fragment.arguments = args
            fragment.webviewCallback = webviewCallback
            return fragment
        }
    }

    override fun initView() {
        super.initView()
        initArguments()

        webView = WebView(context)
        val lp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        webContainer.addView(webView, lp)
        progressBar.progress = 0
        progressBar.max = 100
        initWebview()
    }

    override fun initData() {
        super.initData()
        webView?.loadUrl(url)
    }

    private fun initArguments() {
        arguments?.run {
            url = getString("url")
            title = getString("title", null)
            isFixedTitle = getBoolean("isFixedTitle")
        }
        webviewCallback?.onTitleChange(title)
    }

    private fun initWebview() {
        webView?.settings?.run {
            javaScriptEnabled = true
            //可访问文件
            allowFileAccess = true
            setAllowFileAccessFromFileURLs(true)
            //是否支持缩放
            builtInZoomControls = false
            //是否允许js弹出窗口
            javaScriptCanOpenWindowsAutomatically = true
            //设置自适应屏幕，两者合用将图片调整到适合webview的大小
            useWideViewPort = true
            //缩放至屏幕大小
            loadWithOverviewMode = true
            //webview的缓存
            cacheMode = WebSettings.LOAD_NO_CACHE
            //自动加载图片
            loadsImagesAutomatically = true
            //编码格式
            defaultTextEncodingName = "utf-8"
            //是否启用数据库
            databaseEnabled = true
            //设置定位的数据库路径
            val dir = activity?.applicationContext?.getDir("database", Context.MODE_PRIVATE)?.path
            setGeolocationDatabasePath(dir)
            //启用地理定位
            setGeolocationEnabled(true)
            //开启DomStorage缓存
            domStorageEnabled = true

            // 将Android里面定义的类对象AndroidJs暴露给javascript
            webView?.addJavascriptInterface(
                MyAndroidJs(
                    context,
                    object :
                        MyAndroidJs.CallBack {
                        override fun anyAndroidFunc() {
                            showToast("Javascript调用Android原生")
                        }

                        override fun anyAndroidFunc2() {
                            showToast("Javascript调用Android原生")
                        }

                    }), "AndroidJs"
            )
        }
        webView?.run {
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, progress: Int) {
                    super.onProgressChanged(view, progress)
                    if (progress == 100) { //加载完成
                        progressBar.visibility = View.GONE
                    } else {
                        progressBar.visibility = View.VISIBLE
                        progressBar.progress = progress
                    }
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    if (!isFixedTitle) {
                        webviewCallback?.onTitleChange(title)
                    }
                }

                /**
                 * 选择文件(如:视频,图片)时的回调(大于等于api 21时)
                 */
                override fun onShowFileChooser(
                    webview: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    uploadFile = filePathCallback
                    var isVideoSelect = false
                    for (type in fileChooserParams!!.acceptTypes) {
                        if (type.contains("video/")) { //视频
                            isVideoSelect = true
                            break
                        }
                    }
                    val isCamera = fileChooserParams.isCaptureEnabled
                    chooseFile(isVideoSelect, isCamera)

                    return super.onShowFileChooser(webview, filePathCallback, fileChooserParams)
                }

                /**
                 * 选择文件(如:视频,图片)时的回调(小于api 21时)
                 */
                override fun openFileChooser(
                    uploadMsg: ValueCallback<Uri>?,
                    acceptType: String?,
                    capture: String?
                ) {
                    super.openFileChooser(uploadMsg, acceptType, capture)
                    uploadFileOld = uploadMsg
                    val isVideo = acceptType!!.contains("video/")
                    val isCamera = capture == "camera"
                    chooseFile(isVideo, isCamera)
                }
            }

            webViewClient = object : WebViewClient() {
                override fun onReceivedSslError(
                    p0: WebView?,
                    sslErrHandler: SslErrorHandler?,
                    p2: SslError?
                ) {
//                    super.onReceivedSslError(p0, sslErrHandler, p2)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        webView?.settings?.mixedContentMode =
                            android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    }
                    sslErrHandler?.proceed()   //解决加载https报错问题
                }

                override fun onPageFinished(p0: WebView?, p1: String?) {
                    super.onPageFinished(p0, p1)

                }

                override fun shouldOverrideUrlLoading(p0: WebView?, p1: String?): Boolean {
                    return super.shouldOverrideUrlLoading(p0, p1)
                }
            }
        }
    }

    /**
     * H5选择图片或视频时会回调的方法.
     */
    private fun chooseFile(isVideo: Boolean, isByCamera: Boolean) {
        webviewCallback?.onChooseFile(isVideo, isByCamera)
    }

    fun onBackPress() {
        if (webView?.canGoBack() == true) {
            val url = webView?.url
            if (isRootUrl(url)) {
                activity?.finish()
            } else {
                webView?.goBack()
            }
        } else {
            activity?.finish()
        }
    }


    private fun isRootUrl(url: String?): Boolean {
        //随便写的示例,如果是规定的根布局,就直接关闭页面
        return "root" == url?.substring(url.lastIndexOf('/') + 1) ||
                "http://jandan.net/" == url ||
                webviewCallback?.isRootUrl(url) == true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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

    override fun onDestroyView() {
        webView?.let {
            it.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            it.clearHistory()
            it.clearCache(true)
            (it.parent as? ViewGroup)?.removeView(webView)
            it.destroy()
        }
        super.onDestroyView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_x5_webview

    override fun getViewModel(): BaseNetViewModel? = null

    interface IWebviewCallback {
        fun onTitleChange(title: String?)

        fun onChooseFile(isVideo: Boolean, isByCamera: Boolean)

        fun isRootUrl(url: String?): Boolean
    }
}