package com.acorn.basemodule.base.recyclerAdapter

import android.animation.Animator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.acorn.basemodule.base.BaseViewHolder
import com.acorn.basemodule.base.recyclerAdapter.animation.*
import com.acorn.basemodule.base.recyclerAdapter.drag.BaseDraggableModule
import com.acorn.basemodule.base.recyclerAdapter.drag.DraggableModule
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

/**
 *
 * 参考:QMUI BaseRecyclerAdapter,https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * Created by acorn on 2022/5/23.
 */
abstract class BaseRecyclerAdapter<T, VH : RecyclerView.ViewHolder>(
    protected val context: Context,
    list: List<T>? = null
) : RecyclerView.Adapter<VH>() {
    var data: MutableList<T> = mutableListOf()
        internal set

    protected val mInflater: LayoutInflater
    private var mClickListener: OnItemClickListener<T>? = null
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

    /**
     * if asFlow is true, footer/header will arrange like normal item view.
     * only works when use [GridLayoutManager],and it will ignore span size.
     */
    var headerViewAsFlow: Boolean = false
    var footerViewAsFlow: Boolean = false

    /**
     * 是否打开动画
     */
    var animationEnable: Boolean = false

    /**
     * 动画是否仅第一次执行
     */
    var isAnimationFirstOnly = true

    /**
     * 上次执行动画的位置
     */
    private var mLastAnimatedPosition = -1

    /**
     * 设置自定义动画
     */
    var adapterAnimation: BaseAnimation? = null
        set(value) {
            animationEnable = true
            field = value
        }

    private var mDraggableModule: BaseDraggableModule? = null

    /**
     * 拖拽模块
     */
    val draggableModule: BaseDraggableModule
        get() {
            checkNotNull(mDraggableModule) { "Please first implements DraggableModule" }
            return mDraggableModule!!
        }

    companion object {
        const val ITEM_TYPE_EMPTY = 9000
        const val ITEM_TYPE_HEADER = 9001
        const val ITEM_TYPE_FOOTER = 9002

        fun isFixedViewType(type: Int): Boolean =
            type == ITEM_TYPE_HEADER || type == ITEM_TYPE_FOOTER || type == ITEM_TYPE_EMPTY
    }

    init {
        checkModule()
        list?.let { data.addAll(it) }
        mInflater = LayoutInflater.from(context)
    }

    /**
     * 检查模块
     */
    private fun checkModule() {
        if (this is DraggableModule) {
            mDraggableModule = this.addDraggableModule(this)
        }
    }

    //region Implement fun
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return when (viewType) {
            ITEM_TYPE_EMPTY -> {
                val emptyLayoutVp: ViewParent? = mEmptyLayout.parent
                if (emptyLayoutVp is ViewGroup) {
                    emptyLayoutVp.removeView(mEmptyLayout)
                }
                createBaseViewHolder(mEmptyLayout)
            }
            ITEM_TYPE_HEADER -> {
                val headerLayoutVp: ViewParent? = mHeaderLayout.parent
                if (headerLayoutVp is ViewGroup) {
                    headerLayoutVp.removeView(mHeaderLayout)
                }
                createBaseViewHolder(mHeaderLayout)
            }
            ITEM_TYPE_FOOTER -> {
                val footerLayoutVp: ViewParent? = mFooterLayout.parent
                if (footerLayoutVp is ViewGroup) {
                    footerLayoutVp.removeView(mFooterLayout)
                }
                createBaseViewHolder(mFooterLayout)
            }
            else -> {
                val holder = onCreateDefViewHolder(parent, viewType)
                mClickListener?.let { itemClickListener ->
                    holder.itemView.setOnClickListener {
                        val adjPos = holder.layoutPosition - headerLayoutCount
                        itemClickListener.onItemClick(
                            it,
                            adjPos,
                            getItemViewType(holder.layoutPosition),
                            getItem(adjPos)
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
                mDraggableModule?.initView(holder)
                holder
            }
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        when (holder.itemViewType) {
            ITEM_TYPE_EMPTY, ITEM_TYPE_HEADER, ITEM_TYPE_FOOTER -> return
            else -> {
                val adjPos = position - headerLayoutCount
                bindData(holder, adjPos, getItem(adjPos))
            }
        }
    }

    override fun onBindViewHolder(
        holder: VH,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }
        when (holder.itemViewType) {
            ITEM_TYPE_EMPTY, ITEM_TYPE_HEADER, ITEM_TYPE_FOOTER -> return
            else -> {
                val adjPos = position - headerLayoutCount
                bindData(holder, adjPos, data[adjPos], payloads)
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

    /**
     * Called when a view created by this holder has been attached to a window.
     * simple to solve item will layout using all
     * [setFullSpan]
     *
     * @param holder
     */
    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        val type = holder.itemViewType
        if (isFixedViewType(type)) {
            setFullSpan(holder)
        } else {
            addAnimation(holder)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        mDraggableModule?.attachToRecyclerView(recyclerView)

        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            val defSpanSizeLookup = manager.spanSizeLookup
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val type = getItemViewType(position)
                    if (type == ITEM_TYPE_HEADER && headerViewAsFlow) {
                        return 1
                    }
                    if (type == ITEM_TYPE_FOOTER && footerViewAsFlow) {
                        return 1
                    }
                    val spanSize =
                        if (isFixedViewType(type)) manager.spanCount else defSpanSizeLookup.getSpanSize(
                            position
                        )
                    return spanSize
                }

            }
        }
    }

    //endregion

    /**
     * 创建 ViewHolder。可以重写
     *
     * @param view View
     * @return VH
     */
    @Suppress("UNCHECKED_CAST")
    protected open fun createBaseViewHolder(view: View): VH {
        var temp: Class<*>? = javaClass
        var z: Class<*>? = null
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp)
            temp = temp.superclass
        }
        // 泛型擦除会导致z为null
        val vh: VH? = if (z == null) {
            BaseViewHolder(view) as VH
        } else {
            createBaseGenericKInstance(z, view)
        }
        return vh ?: BaseViewHolder(view) as VH
    }

    /**
     * try to create Generic VH instance
     *
     * @param z
     * @param view
     * @return
     */
    @Suppress("UNCHECKED_CAST")
    private fun createBaseGenericKInstance(z: Class<*>, view: View): VH? {
        try {
            val constructor: Constructor<*>
            // inner and unstatic class
            return if (z.isMemberClass && !Modifier.isStatic(z.modifiers)) {
                constructor = z.getDeclaredConstructor(javaClass, View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(this, view) as VH
            } else {
                constructor = z.getDeclaredConstructor(View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(view) as VH
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * get generic parameter VH
     *
     * @param z
     * @return
     */
    private fun getInstancedGenericKClass(z: Class<*>): Class<*>? {
        try {
            val type = z.genericSuperclass
            if (type is ParameterizedType) {
                val types = type.actualTypeArguments
                for (temp in types) {
                    if (temp is Class<*>) {
                        if (BaseViewHolder::class.java.isAssignableFrom(temp)) {
                            return temp
                        }
                    } else if (temp is ParameterizedType) {
                        val rawType = temp.rawType
                        if (rawType is Class<*> && BaseViewHolder::class.java.isAssignableFrom(
                                rawType
                            )
                        ) {
                            return rawType
                        }
                    }
                }
            }
        } catch (e: java.lang.reflect.GenericSignatureFormatError) {
            e.printStackTrace()
        } catch (e: TypeNotPresentException) {
            e.printStackTrace()
        } catch (e: java.lang.reflect.MalformedParameterizedTypeException) {
            e.printStackTrace()
        }
        return null
    }

    protected open fun isFixedViewType(type: Int): Boolean {
        return type == ITEM_TYPE_EMPTY || type == ITEM_TYPE_HEADER || type == ITEM_TYPE_FOOTER
    }

    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder True if this item should traverse all spans.
     */
    protected open fun setFullSpan(holder: RecyclerView.ViewHolder) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }
    }

    protected fun getDefItem(position: Int): T {
        val adjPos = position - headerLayoutCount
        return getItem(adjPos)
    }

    abstract fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract fun bindData(holder: RecyclerView.ViewHolder, position: Int, item: T)

    /**
     * 局部刷新
     */
    open fun bindData(
        holder: RecyclerView.ViewHolder,
        position: Int,
        item: T,
        payloads: List<Any>
    ) {
    }

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
            val dataSize = getDefItemCount()
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
    open fun getDefItemCount(): Int {
        return data.size
    }

    open fun getItem(position: Int): T = data[position]

    //region 数据操作

    fun setData(list: List<T>?) {
        data.clear()
        list?.let { data.addAll(it) }
        mLastAnimatedPosition = -1
        notifyDataSetChanged()
    }

    fun clearData() {
        setData(null)
    }

    fun add(position: Int, item: T) {
        if (position > getDefItemCount()) {
            return
        } else if (position == getDefItemCount()) {
            data.add(item)
        } else {
            data.add(position, item)
        }
        notifyItemInserted(position)
    }

    fun prepend(list: List<T>) {
        data.addAll(0, list)
        notifyDataSetChanged()
    }

    fun prependWithoutNotify(list: List<T>) {
        data.addAll(0, list)
    }

    fun append(item: T) {
        data.add(item)
//        notifyItemInserted(itemCount - 1)
        notifyDataSetChanged()
    }

    fun append(list: List<T>) {
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        if (position >= getDefItemCount())
            return
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeItem(item: T) {
        val position = data.indexOf(item)
        if (position < 0) return
        data.remove(item)
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

    val footerLayout: LinearLayout?
        get() {
            return if (this::mFooterLayout.isInitialized) {
                mFooterLayout
            } else {
                null
            }
        }

    @JvmOverloads
    fun addFooterView(view: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL): Int {
        if (!this::mFooterLayout.isInitialized) {
            mFooterLayout = LinearLayout(view.context)
            mFooterLayout.orientation = orientation
            mFooterLayout.layoutParams = if (orientation == LinearLayout.VERTICAL) {
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

        val childCount = mFooterLayout.childCount
        var mIndex = index
        if (index < 0 || index > childCount) {
            mIndex = childCount
        }
        mFooterLayout.addView(view, mIndex)
        if (mFooterLayout.childCount == 1) {
            val position = footerViewPosition
            if (position != -1) {
                notifyItemInserted(position)
            }
        }
        return mIndex
    }

    @JvmOverloads
    fun setFooterView(view: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {
        return if (!this::mFooterLayout.isInitialized || mFooterLayout.childCount <= index) {
            addFooterView(view, index, orientation)
        } else {
            mFooterLayout.removeViewAt(index)
            mFooterLayout.addView(view, index)
            index
        }
    }

    fun removeFooterView(index: Int) {
        if (!hasFooterLayout()) return
        if (index < 0 || index >= mFooterLayout.childCount) return

        mFooterLayout.removeViewAt(index)
        if (mFooterLayout.childCount == 0) {
            val position = footerViewPosition
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    fun removeFooterView(footer: View) {
        if (!hasFooterLayout()) return

        mFooterLayout.removeView(footer)
        if (mFooterLayout.childCount == 0) {
            val position = footerViewPosition
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    fun removeAllFooterView() {
        if (!hasFooterLayout()) return

        mFooterLayout.removeAllViews()
        val position = footerViewPosition
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    val footerViewPosition: Int
        get() {
            if (hasEmptyView()) {
                var position = 1
                if (headerWithEmptyEnable && hasHeaderLayout()) {
                    position++
                }
                if (footerWithEmptyEnable) {
                    return position
                }
            } else {
                return headerLayoutCount + getDefItemCount()
            }
            return -1
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
        return data.isEmpty()
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

    //region Item Animation
    private fun addAnimation(holder: RecyclerView.ViewHolder) {
        if (animationEnable) {
            if (!isAnimationFirstOnly || holder.layoutPosition > mLastAnimatedPosition) {
                val animation: BaseAnimation = adapterAnimation ?: AlphaInAnimation()
                animation.animators(holder.itemView).forEach {
                    startAnim(it, holder.layoutPosition)
                }
                mLastAnimatedPosition = holder.layoutPosition
            }
        }
    }

    /**
     * 开始执行动画方法
     * 可以重写此方法，实行更多行为
     *
     * @param anim
     * @param index
     */
    protected open fun startAnim(anim: Animator, index: Int) {
        anim.start()
    }

    /**
     * 内置默认动画类型
     */
    enum class AnimationType {
        AlphaIn, ScaleIn, SlideInBottom, SlideInLeft, SlideInRight
    }

    /**
     * 使用内置默认动画设置
     * @param animationType AnimationType
     */
    fun setAnimationWithDefault(animationType: AnimationType) {
        adapterAnimation = when (animationType) {
            AnimationType.AlphaIn -> AlphaInAnimation()
            AnimationType.ScaleIn -> ScaleInAnimation()
            AnimationType.SlideInBottom -> SlideInBottomAnimation()
            AnimationType.SlideInLeft -> SlideInLeftAnimation()
            AnimationType.SlideInRight -> SlideInRightAnimation()
        }
    }
    //endregion


    fun setOnItemClickListener(itemClickListener: OnItemClickListener<T>) {
        this.mClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(itemLongClickListener: OnItemLongClickListener) {
        this.mLongClickListener = itemLongClickListener
    }

    interface OnItemClickListener<I> {
        fun onItemClick(itemView: View, position: Int, itemViewType: Int, item: I)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(itemView: View, position: Int, itemViewType: Int)
    }

}