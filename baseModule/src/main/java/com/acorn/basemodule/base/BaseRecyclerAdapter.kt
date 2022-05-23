package com.acorn.basemodule.base

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.R

/**
 *
 * Created by acorn on 2022/5/23.
 */
abstract class BaseRecyclerAdapter<D>(
    protected val context: Context,
    list: List<D>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mData: MutableList<D> = mutableListOf()
    protected val mInflater: LayoutInflater
    protected var mClickListener: OnItemClickListener? = null
    protected var mLongClickListener: OnItemLongClickListener? = null

    init {
        list?.let { mData.addAll(it) }
        mInflater = LayoutInflater.from(context)
    }

    companion object {
        private const val ITEM_TYPE_EMPTY = 9000
        private const val ITEM_TYPE_HEADER = 9001
        private const val ITEM_TYPE_FOOTER = 9002
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_EMPTY -> {
                BaseViewHolder(
                    context,
                    mInflater.inflate(R.layout.layout_empty_data, parent, false)
                )
            }
            else -> {
                val holder = onCreateChildViewHolder(parent, viewType)
                mClickListener?.let { itemClickListener ->
                    holder.itemView.setOnClickListener {
                        itemClickListener.onItemClick(
                            it,
                            holder.layoutPosition,
                            getItemViewType(holder.layoutPosition)
                        )
                    }
                }
                mLongClickListener?.let { itemClickListener ->
                    holder.itemView.setOnClickListener {
                        itemClickListener.onItemLongClick(
                            it,
                            holder.layoutPosition,
                            getItemViewType(holder.layoutPosition)
                        )
                    }
                }
                holder
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) != ITEM_TYPE_EMPTY)
            bindData(holder, position, mData[position])
    }

    override fun getItemCount(): Int = if (mData.size == 0) 1 else mData.size

    abstract fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    abstract fun bindData(holder: RecyclerView.ViewHolder, position: Int, item: D)

    override fun getItemViewType(position: Int): Int {
        if (mData.size == 0) {
            return ITEM_TYPE_EMPTY
        }
        return super.getItemViewType(position)
    }

    fun getItem(position: Int): D = mData[position]

    fun setData(list: List<D>?) {
        mData.clear()
        list?.let { mData.addAll(it) }
        notifyDataSetChanged()
    }

    fun add(position: Int, item: D) {
        if (position >= mData.size) {
            return
        }
        mData.add(position, item)
        notifyItemInserted(position)
        Log.i("FDSA", "fdsa")
    }

    fun prepend(list: List<D>) {
        mData.addAll(0, list)
        notifyDataSetChanged()
    }

    fun append(list: List<D>) {
        mData.addAll(list)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        if (position >= mData.size)
            return
        mData.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.mClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(itemLongClickListener: OnItemLongClickListener) {
        this.mLongClickListener = itemLongClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(itemView: View, position: Int, itemViewType: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(itemView: View, position: Int, itemViewType: Int)
    }

}