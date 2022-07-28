package com.acorn.myframeapp.ui.recyclerview.largedata2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wcy.databasemodule.entities.ExperimentRecord

/**
 * Created by acorn on 2022/7/27.
 */
class DynamicLargeDataAdapter :
    PagingDataAdapter<ExperimentRecord, DynamicLargeDataAdapter.LargeDataViewHolder>(LargeDataDiffCallback()) {

    override fun onBindViewHolder(holder: LargeDataViewHolder, position: Int) {
        val bean = getItem(position)
        holder.tv.text = bean?.valueStr
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LargeDataViewHolder {
        return LargeDataViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
        )
    }

    inner class LargeDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView

        init {
            tv = itemView as TextView
        }
    }
}

private class LargeDataDiffCallback : DiffUtil.ItemCallback<ExperimentRecord>() {
    override fun areItemsTheSame(oldItem: ExperimentRecord, newItem: ExperimentRecord): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ExperimentRecord, newItem: ExperimentRecord): Boolean {
        return oldItem == newItem
    }

}