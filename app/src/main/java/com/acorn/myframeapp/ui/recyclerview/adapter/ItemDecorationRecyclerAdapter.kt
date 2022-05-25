package com.acorn.myframeapp.ui.recyclerview.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.base.BaseViewHolder
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.myframeapp.R
import kotlinx.android.synthetic.main.item_item_decoration.view.*

/**
 * Created by acorn on 2022/5/25.
 */
class ItemDecorationRecyclerAdapter(context: Context, datas: List<String>? = null) :
    BaseRecyclerAdapter<String, RecyclerView.ViewHolder>(context, datas) {
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BaseViewHolder(mInflater.inflate(R.layout.item_item_decoration, parent, false))
    }

    override fun bindData(holder: RecyclerView.ViewHolder, position: Int, item: String) {
        holder.itemView.tv.text = item
    }

}