package com.acorn.myframeapp.recyclerview.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.base.BaseRecyclerAdapter
import com.acorn.basemodule.base.BaseViewHolder

/**
 * Created by acorn on 2022/5/23.
 */
class ConventionalRecyclerAdapter(context: Context, datas: List<String>? = null) :
    BaseRecyclerAdapter<String>(context, datas) {
    override fun onCreateChildViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return BaseViewHolder(
            context,
            mInflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        )
    }

    override fun bindData(holder: RecyclerView.ViewHolder, position: Int, item: String) {
        (holder.itemView as? TextView)?.text = getItem(position)
    }

}