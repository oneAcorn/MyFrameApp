package com.acorn.basemodule.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.acorn.basemodule.R
import kotlinx.android.synthetic.main.base_dialog_progress.*

/**
 * Created by acorn on 2019-08-20.
 */
class ProgressDialog : AppCompatDialogFragment() {
    private var msg: String? = null
    private var onDialogDismiss: (() -> Unit)? = null
    var backPressCancelable = true

    companion object {
        fun newInstance(onDialogDismiss: (() -> Unit)? = null): ProgressDialog {
            val args = Bundle()
            val fragment = ProgressDialog()
            fragment.arguments = args
            fragment.onDialogDismiss = onDialogDismiss
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ProgressDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(false)
        return View.inflate(context, R.layout.base_dialog_progress, null)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : AppCompatDialog(context, theme) {
            override fun onBackPressed() {
                if (backPressCancelable)
                    super.onBackPressed()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogDismiss?.invoke()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLoadMsg.text = msg
    }

    fun show(manager: FragmentManager, tag: String?, msg: String?) {
        this.msg = msg
        show(manager, tag)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val attr = it.attributes.apply {
                dimAmount = 0.3f
            }
            it.attributes = attr
        }
    }
}