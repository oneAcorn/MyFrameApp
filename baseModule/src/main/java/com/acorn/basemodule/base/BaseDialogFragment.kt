package com.acorn.basemodule.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialogFragment
import com.acorn.basemodule.R

/**
 * Created by acorn on 2022/5/27.
 */
abstract class BaseDialogFragment : AppCompatDialogFragment() {
    private var onDialogDismiss: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CommonDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(canceledOnTouchOutside())
        return View.inflate(context, getLayoutId(), null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initData()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogDismiss?.invoke()
    }

    protected open fun canceledOnTouchOutside(): Boolean = true

    protected open fun initView(view: View){}

    protected open fun initData(){}

    protected abstract fun getLayoutId():Int
}