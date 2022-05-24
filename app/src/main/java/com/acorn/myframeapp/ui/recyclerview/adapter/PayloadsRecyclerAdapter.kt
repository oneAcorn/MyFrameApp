package com.acorn.myframeapp.ui.recyclerview.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.base.BaseViewHolder
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.basemodule.extendfun.showToast
import com.acorn.myframeapp.R
import kotlinx.android.synthetic.main.item_payloads.view.*

/**
 * Created by acorn on 2022/5/24.
 */
class PayloadsRecyclerAdapter(
    context: Context,
    datas: List<PayloadsRecyclerAdapter.PayloadsData>? = null
) :
    BaseRecyclerAdapter<PayloadsRecyclerAdapter.PayloadsData, RecyclerView.ViewHolder>(
        context,
        datas
    ) {

    companion object {
        const val PAYLOAD_TV1 = 0
        const val PAYLOAD_TV2 = 1
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PayloadsViewHolder(mInflater.inflate(R.layout.item_payloads, parent, false))
    }

    override fun bindData(
        holder: RecyclerView.ViewHolder,
        position: Int,
        item: PayloadsRecyclerAdapter.PayloadsData
    ) {
        showToast("position$position bindData")
        //假设这里有一堆view需要更新
        if (holder is PayloadsViewHolder) {
            holder.contentTv.text = "使用Payloads可以在ViewHolder有很多View的情况下,只刷新指定的某个View${item.str0}"
            holder.updateTv1.text = item.str1
            holder.updateTv2.text = item.str2
        }
    }

    override fun bindData(
        holder: RecyclerView.ViewHolder,
        position: Int,
        item: PayloadsRecyclerAdapter.PayloadsData,
        payloads: List<Any>
    ) {
        super.bindData(holder, position, item, payloads)
        showToast("position$position bindDataPayloads")
        if (holder is PayloadsViewHolder) {
            for (payload in payloads) {
                when (payload as Int) {
                    PAYLOAD_TV1 -> {
                        holder.updateTv1.text = item.str1
                    }
                    PAYLOAD_TV2 -> {
                        holder.updateTv2.text = item.str2
                    }
                    else -> {}
                }
            }
        }
    }

    class PayloadsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentTv: TextView
        val updateTv1: TextView
        val updateTv2: TextView

        init {
            contentTv = itemView.tv
            updateTv1 = itemView.updateTv
            updateTv2 = itemView.updateTv2
        }
    }

    data class PayloadsData(var str0: String, var str1: String, var str2: String)
}