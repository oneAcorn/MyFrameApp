package com.acorn.myframeapp.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.acorn.basemodule.extendfun.logI
import com.acorn.basemodule.extendfun.screenWidth
import com.acorn.basemodule.utils.download.DownloadProgressWatcher
import com.acorn.myframeapp.R
import kotlinx.android.synthetic.main.dialog_update.*


/**
 * 升级弹框
 * Created by acorn on 2020/5/9.
 */
class UpdateDialog : AppCompatDialogFragment(), View.OnClickListener {
    private lateinit var titleTv: TextView
    private lateinit var contentTv: TextView
    private lateinit var downloadStateTv: TextView
    private lateinit var cancelBtn: Button
    private lateinit var okBtn: Button
    private lateinit var line: View
    private var clickListener: OnUpdateDialogClickListener? = null

    private var downloadWatcher: DownloadProgressWatcher? = null

    companion object {
        /**
         * @param title 标题
         * @param content 内容
         * @param isShowCancel 是否显示取消（不显示代表强制更新）
         */
        fun newInstance(title: String? = null, content: String? = null, isShowCancel: Boolean,
                        onClickListener: OnUpdateDialogClickListener): UpdateDialog {
            val args = Bundle()
            if (title != null) {
                args.putString("title", title)
            }
            if (content != null) {
                args.putString("content", content)
            }
            args.putBoolean("isShowCancel", isShowCancel)
            val fragment = UpdateDialog()
            fragment.arguments = args
            fragment.clickListener = onClickListener
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
        dialog?.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) { //禁止物理返回键
                return@setOnKeyListener true
            }
            false
        }
        val view = View.inflate(context, R.layout.dialog_update, null)
        initView(view)
        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        downloadWatcher?.stop()
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(msg: EventMessage) {
//        if (msg.eventType == EventMessage.EventType.DOWNLOADING_APK) {
//            showDownloading(msg.msg ?: "")
//        }
//    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val lp = it.attributes
            lp.width = (screenWidth.toFloat() * 0.8f).toInt()
            it.attributes = lp
        }
    }

    private fun initView(view: View) {
        titleTv = view.findViewById(R.id.titleTv)
        contentTv = view.findViewById(R.id.contentTv)
        downloadStateTv = view.findViewById(R.id.downloadStateTv)
        cancelBtn = view.findViewById(R.id.cancelTv)
        okBtn = view.findViewById(R.id.okBtn)
        line = view.findViewById(R.id.line)

        val title: String? = arguments?.getString("title", null)
        val content: String? = arguments?.getString("content", null)
        title?.let {
            titleTv.text = it
        }
        content?.let {
            contentTv.text = it
        }

        if (arguments?.getBoolean("isShowCancel") == true) {
            cancelBtn.visibility = View.VISIBLE
        } else {
            cancelBtn.visibility = View.GONE
        }

        cancelBtn.setOnClickListener(this)
        okBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v === cancelBtn) {
            clickListener?.onCancelClick(this)
        } else if (v === okBtn) {
            clickListener?.onOkClick(this)
        }
    }

    fun updateDownloadingState(downloadId: Long?) {
        downloadStateTv.text = "下载中.."
        downloadStateTv.visibility = View.VISIBLE
//        okBtn.text = "下载中.."
        okBtn.setTextColor(requireContext().resources.getColor(com.acorn.basemodule.R.color.gray))
        okBtn.isEnabled = false
        updateProgress(downloadId)
    }

    private fun updateProgress(downloadId: Long?) {
        downloadId ?: return
        downloadPb.visibility = View.VISIBLE
        downloadWatcher = DownloadProgressWatcher(requireContext(), downloadId)
        downloadWatcher?.watchProgress(
                { downSize, totalSize ->
                    downloadPb.max = totalSize
                    downloadPb.progress = downSize
                    logI("downSize:$downSize,totalSize:$totalSize")
                    //totalSize在小米刚启用下载时上有可能为-1
                    if (totalSize > 0 && downSize >= totalSize) {
                        downloadStateTv.text = "下载完成"
                    }
                },
                {
                    downloadStateTv.text = it
                })
    }


    interface OnUpdateDialogClickListener {
        fun onOkClick(dialog: UpdateDialog)

        fun onCancelClick(dialog: UpdateDialog)
    }
}