package com.acorn.myframeapp.ui.recyclerview.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.base.BaseViewHolder
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.myframeapp.R

/**
 * Created by acorn on 2022/5/24.
 */
class GridRecyclerAdapter(context: Context, datas: List<String>? = null) :
    BaseRecyclerAdapter<String>(context, datas) {

    companion object {
        const val ITEM_TYPE_1 = 1
        const val ITEM_TYPE_2 = 2
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_1) {
            BaseViewHolder(mInflater.inflate(R.layout.item_grid_type1, parent, false))
        } else {
            BaseViewHolder(mInflater.inflate(R.layout.item_grid_type2, parent, false))
        }
    }

    override fun bindData(holder: RecyclerView.ViewHolder, position: Int, item: String) {
        holder.itemView.findViewById<TextView>(R.id.tv).text = getItem(position)
    }

    override fun getDefItemViewType(position: Int): Int {
        return if (position % 6 == 0) {
            ITEM_TYPE_2
        } else {
            ITEM_TYPE_1
        }
    }
}