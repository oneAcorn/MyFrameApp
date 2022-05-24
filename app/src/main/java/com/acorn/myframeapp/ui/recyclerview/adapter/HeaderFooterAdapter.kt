package com.acorn.myframeapp.ui.recyclerview.adapter

import android.R
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.basemodule.base.BaseViewHolder

/**
 * Created by acorn on 2022/5/23.
 */
class HeaderFooterAdapter(context: Context, datas: List<String>? = null) :
    BaseRecyclerAdapter<String,RecyclerView.ViewHolder>(context, datas) {
    override fun onCreateDefViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return BaseViewHolder(mInflater.inflate(R.layout.simple_list_item_1, parent, false))
    }

    override fun bindData(holder: RecyclerView.ViewHolder, position: Int, item: String) {
        (holder.itemView as? TextView)?.text = getItem(position)
    }
}