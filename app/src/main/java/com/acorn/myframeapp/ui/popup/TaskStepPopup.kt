package com.acorn.myframeapp.ui.popup

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.popup.CommonBasePopupWindow
import com.acorn.myframeapp.R
import kotlinx.android.synthetic.main.popup_task_step.view.*

/**
 * Created by acorn on 2022/4/18.
 */
class TaskStepPopup(
    context: Activity,
    list: List<String>,
    lifecycle: Lifecycle,
    onItemClickListener: ((index: Int, nodeName: String) -> Unit)? = null
) : CommonBasePopupWindow(context, R.layout.popup_task_step, lifecycle) {
    private var adapter: TaskStepAdapter? = null

    init {
        adapter = TaskStepAdapter(list)
        adapter?.onItemClickListener = { index, nodeName ->
            onItemClickListener?.invoke(index, nodeName)
            dismiss()
        }
        rootView.taskStepRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootView.taskStepRv.adapter = adapter
        rootView.setOnClickListener {
            dismiss()
        }
    }
}