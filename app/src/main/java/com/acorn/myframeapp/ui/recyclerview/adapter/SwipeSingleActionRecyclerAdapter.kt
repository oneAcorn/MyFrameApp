package com.acorn.myframeapp.ui.recyclerview.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.basemodule.extendfun.dp
import com.acorn.basemodule.extendfun.getColorCompat
import com.acorn.basemodule.extendfun.getDrawableCompat
import com.acorn.basemodule.extendfun.sp
import com.acorn.myframeapp.R
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction
import com.qmuiteam.qmui.recyclerView.QMUISwipeViewHolder
import kotlinx.android.synthetic.main.item_payloads.view.*

/**
 * Created by acorn on 2022/5/25.
 */
class SwipeSingleActionRecyclerAdapter(context: Context, datas: List<String>? = null) :
    BaseRecyclerAdapter<String, RecyclerView.ViewHolder>(context, datas) {
    val mDeleteAction: QMUISwipeAction

    init {
        val actionBuilder = QMUISwipeAction.ActionBuilder().textSize(14.sp)
            .textColor(context.getColorCompat(R.color.white))
            .paddingStartEnd(14.dp)
        mDeleteAction = actionBuilder.text("删除").backgroundColor(Color.RED)
            .icon(context.getDrawableCompat(R.drawable.ic_baseline_delete_white_24))
            .orientation(QMUISwipeAction.ActionBuilder.VERTICAL)
            .reverseDrawOrder(false)
            .build()
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return QMUISwipeViewHolder(
            mInflater.inflate(
                android.R.layout.simple_list_item_1,
                parent,
                false
            )
        ).apply {
            addSwipeAction(mDeleteAction)
        }
    }

    override fun bindData(holder: RecyclerView.ViewHolder, position: Int, item: String) {
        (holder.itemView as? TextView)?.text = getItem(position)
    }


}