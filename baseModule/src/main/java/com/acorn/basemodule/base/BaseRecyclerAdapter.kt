package com.acorn.basemodule.base

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.R

/**
 *
 * 参考:QMUI BaseRecyclerAdapter,https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * Created by acorn on 2022/5/23.
 */
abstract class BaseRecyclerAdapter<T>(
    protected val context: Context,
    list: List<T>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mData: MutableList<T> = mutableListOf()
    protected val mInflater: LayoutInflater
    private var mClickListener: OnItemClickListener? = null
    private var mLongClickListener: OnItemLongClickListener? = null

    private lateinit var mHeaderLayout: LinearLayout
    private lateinit var mFooterLayout: LinearLayout
    private lateinit var mEmptyLayout: FrameLayout

    /**
     * 当显示空布局时，是否显示 Header
     */
    var headerWithEmptyEnable = false

    /** 当显示空布局时，是否显示 Foot */
    var footerWithEmptyEnable = false

    /** 是否使用空布局 */
    var isUseEmpty = true


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
                val emptyLayoutVp: ViewParent? = mEmptyLayout.parent
                if (emptyLayoutVp is ViewGroup) {
                    emptyLayoutVp.removeView(mEmptyLayout)
                }
                BaseViewHolder(mEmptyLayout)
            }
            ITEM_TYPE_HEADER -> {
                val headerLayoutVp: ViewParent? = mHeaderLayout.parent
                if (headerLayoutVp is ViewGroup) {
                    headerLayoutVp.removeView(mHeaderLayout)
                }
                BaseViewHolder(mHeaderLayout)
            }
            else -> {
                val holder = onCreateDefViewHolder(parent, viewType)
                mClickListener?.let { itemClickListener ->
                    holder.itemView.setOnClickListener {
                        val adjPos = holder.layoutPosition - headerLayoutCount
                        itemClickListener.onItemClick(
                            it,
                            adjPos,
                            getItemViewType(holder.layoutPosition)
                        )
                    }
                }
                mLongClickListener?.let { itemClickListener ->
                    holder.itemView.setOnClickListener {
                        val adjPos = holder.layoutPosition - headerLayoutCount
                        itemClickListener.onItemLongClick(
                            it,
                            adjPos,
                            getItemViewType(holder.layoutPosition)
                        )
                    }
                }
                holder
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ITEM_TYPE_EMPTY, ITEM_TYPE_HEADER, ITEM_TYPE_FOOTER -> return
            else -> {
                val adjPos = position - headerLayoutCount
                bindData(holder, adjPos, mData[adjPos])
            }
        }
    }

    /**
     * Don't override this method. If need, please override [getDefItemCount]
     * 不要重写此方法，如果有需要，请重写[getDefItemCount]
     * @return Int
     */
    override fun getItemCount(): Int {
        return if (hasEmptyView()) {
            var count = 1
            if (headerWithEmptyEnable && hasHeaderLayout()) {
                count++
            }
            if (footerWithEmptyEnable && hasFooterLayout()) {
                count++
            }
            count
        } else {
            headerLayoutCount + getDefItemCount() + footerLayoutCount
        }
    }

    abstract fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    abstract fun bindData(holder: RecyclerView.ViewHolder, position: Int, item: T)

    /**
     * Don't override this method. If need, please override [getDefItemViewType]
     * 不要重写此方法，如果有需要，请重写[getDefItemViewType]
     *
     * @param position Int
     * @return Int
     */
    override fun getItemViewType(position: Int): Int {
        if (hasEmptyView()) {
            val header = headerWithEmptyEnable && hasHeaderLayout()
            return when (position) {
                0 -> if (header) {
                    ITEM_TYPE_HEADER
                } else {
                    ITEM_TYPE_EMPTY
                }
                1 -> if (header) {
                    ITEM_TYPE_EMPTY
                } else {
                    ITEM_TYPE_FOOTER
                }
                2 -> ITEM_TYPE_FOOTER
                else -> ITEM_TYPE_EMPTY
            }
        }
        val hasHeader = hasHeaderLayout()
        if (hasHeader && position == 0) {
            return ITEM_TYPE_HEADER
        } else {
            val adjPosition = if (hasHeader) {
                position - 1
            } else {
                position
            }
            val dataSize = mData.size
            return if (adjPosition < dataSize) {
                getDefItemViewType(adjPosition)
            } else {
                ITEM_TYPE_FOOTER
                //TODO 确定要不要LOAD_MORE_VIEW,不要就删掉下面的
//                adjPosition -= dataSize
//                val numFooters = if (hasFooterLayout()) {
//                    1
//                } else {
//                    0
//                }
//                if (adjPosition < numFooters) {
//                    ITEM_TYPE_FOOTER
//                } else {
//                    LOAD_MORE_VIEW
//                }
            }
        }

//        if (mData.size == 0) {
//            return ITEM_TYPE_EMPTY
//        }
//        return super.getItemViewType(position)
    }

    /**
     * Override this method and return your ViewType.
     * 重写此方法，返回你的ViewType。
     */
    protected open fun getDefItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    /**
     * Override this method and return your data size.
     * 重写此方法，返回你的数据数量。
     */
    protected open fun getDefItemCount(): Int {
        return mData.size
    }

    fun getItem(position: Int): T = mData[position]

    //region 数据操作
    fun setData(list: List<T>?) {
        mData.clear()
        list?.let { mData.addAll(it) }
        notifyDataSetChanged()
    }

    fun clearData() {
        setData(null)
    }

    fun add(position: Int, item: T) {
        if (position >= mData.size) {
            return
        }
        mData.add(position, item)
        notifyItemInserted(position)
        Log.i("FDSA", "fdsa")
    }

    fun prepend(list: List<T>) {
        mData.addAll(0, list)
        notifyDataSetChanged()
    }

    fun append(list: List<T>) {
        mData.addAll(list)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        if (position >= mData.size)
            return
        mData.removeAt(position)
        notifyItemRemoved(position)
    }
    //endregion


    //region Header,Footer,Empty View.
    @JvmOverloads
    fun addHeaderView(view: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL): Int {
        if (!this::mHeaderLayout.isInitialized) {
            mHeaderLayout = LinearLayout(view.context)
            mHeaderLayout.orientation = orientation
            mHeaderLayout.layoutParams = if (orientation == LinearLayout.VERTICAL) {
                RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            } else {
                RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }

        val childCount = mHeaderLayout.childCount
        var mIndex = index
        if (index < 0 || index > childCount) {
            mIndex = childCount
        }
        mHeaderLayout.addView(view, mIndex)
        if (mHeaderLayout.childCount == 1) {
            val position = headerViewPosition
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return mIndex
    }

    @JvmOverloads
    fun setHeaderView(view: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {
        return if (!this::mHeaderLayout.isInitialized || mHeaderLayout.childCount <= index) {
            addHeaderView(view, index, orientation)
        } else {
            mHeaderLayout.removeViewAt(index)
            mHeaderLayout.addView(view, index)
            index
        }
    }

    fun removeHeaderView(header: View) {
        if (!hasHeaderLayout()) return

        mHeaderLayout.removeView(header)
        if (mHeaderLayout.childCount == 0) {
            val position = headerViewPosition
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    fun removeHeaderView(index: Int) {
        if (!hasHeaderLayout()) return
        if (index < 0 || index >= mHeaderLayout.childCount) return

        mHeaderLayout.removeViewAt(index)
        if (mHeaderLayout.childCount == 0) {
            val position = headerViewPosition
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    fun removeAllHeaderView() {
        if (!hasHeaderLayout()) return

        mHeaderLayout.removeAllViews()
        val position = headerViewPosition
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    val headerViewPosition: Int
        get() {
            if (hasEmptyView()) {
                if (headerWithEmptyEnable) {
                    return 0
                }
            } else {
                return 0
            }
            return -1
        }

    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    val headerLayoutCount: Int
        get() {
            return if (hasHeaderLayout()) {
                1
            } else {
                0
            }
        }

    /**
     * 获取头布局
     */
    val headerLayout: LinearLayout?
        get() {
            return if (this::mHeaderLayout.isInitialized) {
                mHeaderLayout
            } else {
                null
            }
        }

    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    val footerLayoutCount: Int
        get() {
            return if (hasFooterLayout()) {
                1
            } else {
                0
            }
        }

    /**
     * 是否有 HeaderLayout
     * @return Boolean
     */
    fun hasHeaderLayout(): Boolean {
        if (this::mHeaderLayout.isInitialized && mHeaderLayout.childCount > 0) {
            return true
        }
        return false
    }

    fun hasFooterLayout(): Boolean {
        if (this::mFooterLayout.isInitialized && mFooterLayout.childCount > 0) {
            return true
        }
        return false
    }

    fun setEmptyView(emptyView: View) {
        val oldItemCount = itemCount
        var insert = false
        if (!this::mEmptyLayout.isInitialized) {
            mEmptyLayout = FrameLayout(emptyView.context)

            mEmptyLayout.layoutParams = emptyView.layoutParams?.let {
                return@let ViewGroup.LayoutParams(it.width, it.height)
            } ?: ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            insert = true
        } else {
            emptyView.layoutParams?.let {
                val lp = mEmptyLayout.layoutParams
                lp.width = it.width
                lp.height = it.height
                mEmptyLayout.layoutParams = lp
            }
        }

        mEmptyLayout.removeAllViews()
        mEmptyLayout.addView(emptyView)
        isUseEmpty = true
        if (insert && hasEmptyView()) {
            var position = 0
            if (headerWithEmptyEnable && hasHeaderLayout()) {
                position++
            }
            if (itemCount > oldItemCount) {
                notifyItemInserted(position)
            } else {
                notifyDataSetChanged()
            }
        }
    }

    fun removeEmptyView() {
        if (this::mEmptyLayout.isInitialized) {
            mEmptyLayout.removeAllViews()
        }
    }

    fun hasEmptyView(): Boolean {
        if (!this::mEmptyLayout.isInitialized || mEmptyLayout.childCount == 0) {
            return false
        }
        if (!isUseEmpty) {
            return false
        }
        return mData.isEmpty()
    }

    val emptyLayout: FrameLayout?
        get() {
            return if (this::mEmptyLayout.isInitialized) {
                mEmptyLayout
            } else {
                null
            }
        }

    //endregion

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