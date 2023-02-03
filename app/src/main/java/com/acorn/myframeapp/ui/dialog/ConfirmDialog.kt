package com.acorn.myframeapp.ui.dialog

import android.os.Bundle
import android.view.View
import com.acorn.basemodule.base.BaseDialogFragment
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import kotlinx.android.synthetic.main.dialog_confirm.*

/**
 * Created by acorn on 2022/5/27.
 */
class ConfirmDialog : BaseDialogFragment<BaseNetViewModel>() {
    private var confirmClickCallback: ((dialog: ConfirmDialog) -> Unit)? = null
    private var cancelClickCallback: (() -> Unit)? = null

    companion object {
        fun newInstance(
            title: String?,
            content: String?,
            confirmClickCallback: ((dialog: ConfirmDialog) -> Unit)?,
            cancelClickCallback: (() -> Unit)?
        ): ConfirmDialog {
            val args = Bundle()
            title?.let { args.putString("title", it) }
            content?.let { args.putString("content", it) }
            val fragment = ConfirmDialog()
            fragment.confirmClickCallback = confirmClickCallback
            fragment.cancelClickCallback = cancelClickCallback
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.dialog_confirm
    override fun getViewModel(): BaseNetViewModel? = null

    override fun initView(view: View) {
        super.initView(view)
        val title = arguments?.getString("title")
        val content = arguments?.getString("content")

        titleTv.visibility = if (title.isNullOrEmpty()) View.GONE else View.VISIBLE
        contentTv.visibility = if (content.isNullOrEmpty()) View.GONE else View.VISIBLE
        titleTv.text = title
        contentTv.text = content

        confirmBtn.setOnClickListener {
            confirmClickCallback?.invoke(this)
        }
        cancelBtn.setOnClickListener {
            cancelClickCallback?.invoke()
            dismiss()
        }
    }
}