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
class SwipeActionRecyclerAdapter(context: Context, datas: List<String>? = null) :
    BaseRecyclerAdapter<String, RecyclerView.ViewHolder>(context, datas) {
    val mDeleteAction: QMUISwipeAction
    val mWriteReviewAction: QMUISwipeAction
    val mIconAction: QMUISwipeAction

    init {
        val actionBuilder = QMUISwipeAction.ActionBuilder().textSize(14.sp)
            .textColor(context.getColorCompat(R.color.white))
            .paddingStartEnd(14.dp)
        mDeleteAction = actionBuilder.text("删除").backgroundColor(Color.RED)
            .icon(context.getDrawableCompat(R.drawable.ic_baseline_delete_white_24))
            .orientation(QMUISwipeAction.ActionBuilder.VERTICAL)
            .reverseDrawOrder(false)
            .build()
        mWriteReviewAction = actionBuilder.text("写评论").backgroundColor(Color.BLUE).build()
        mIconAction = actionBuilder.backgroundColor(Color.GREEN)
            .text(null) //防止text被上面的"写评论"覆盖
            .icon(context.getDrawableCompat(R.drawable.ic_baseline_architecture_24))
            .orientation(QMUISwipeAction.ActionBuilder.VERTICAL)
            .build()
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SwipeActionViewHolder(
            mInflater.inflate(
                R.layout.item_payloads,
                parent,
                false
            )
        ).apply {
            addSwipeAction(mDeleteAction)
            addSwipeAction(mWriteReviewAction)
            addSwipeAction(mIconAction)
        }
    }

    override fun bindData(holder: RecyclerView.ViewHolder, position: Int, item: String) {
        if (holder is SwipeActionViewHolder) {
            holder.tv1.text = item + "11111111"
            holder.tv2.text = item + "2222222222"
        }
    }

    class SwipeActionViewHolder(itemView: View) : QMUISwipeViewHolder(itemView) {
        val tv1: TextView
        val tv2: TextView

        init {
            tv1 = itemView.updateTv
            tv2 = itemView.updateTv2
        }
    }
}