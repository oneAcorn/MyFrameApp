package com.acorn.myframeapp.ui.popup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.extendfun.appContext
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.myframeapp.R

/**
 * Created by acorn on 2022/4/18.
 */
class TaskStepAdapter(private val stepList: List<String>) :
    RecyclerView.Adapter<TaskStepAdapter.ViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    var onItemClickListener: ((index: Int, nodeName: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(getLayoutInflater().inflate(R.layout.item_task_step, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stepList[position])
    }

    override fun getItemCount(): Int {
        return stepList.size
    }

    private fun getLayoutInflater(): LayoutInflater {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(appContext)
        }
        return layoutInflater!!
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tv: TextView = itemView as TextView

        init {
            itemView.singleClick {
                onItemClickListener?.invoke(
                    absoluteAdapterPosition,
                    stepList[absoluteAdapterPosition]
                )
            }
        }

        fun bind(str: String) {
            tv.text = str
        }
    }
}