package com.acorn.myframeapp.ui.recyclerview

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.basemodule.base.recyclerAdapter.drag.listener.OnItemDragListener
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.acorn.myframeapp.extendfun.getRandomList
import com.acorn.myframeapp.ui.recyclerview.adapter.DragRecyclerAdapter
import kotlinx.android.synthetic.main.activity_conventional_recyclerview.*

/**
 * Created by acorn on 2022/5/25.
 */
class DragRecyclerViewActivity : BaseNoViewModelActivity() {
    private var mAdapter: DragRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conventional_recyclerview)
    }

    override fun initView() {
        super.initView()
        showToolbar { it.title = "Draggable" }
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = DragRecyclerAdapter(this, null)
        initDragModule()
        rv.adapter = mAdapter
        mAdapter?.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<String> {
            override fun onItemClick(
                itemView: View,
                position: Int,
                itemViewType: Int,
                item: String
            ) {
                showTip(
                    "position:$position,itemViewType:$itemViewType,data:${
                        mAdapter?.getItem(
                            position
                        )
                    }}"
                )
            }
        })
    }

    override fun initData() {
        super.initData()
        mAdapter?.addHeaderView(layoutInflater.inflate(R.layout.view_header, rv, false))
        mAdapter?.setData(getRandomList())
    }

    private fun initDragModule() {
        val dragListener = object : OnItemDragListener {
            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                // 开始时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                val startColor = Color.WHITE
                val endColor = Color.rgb(245, 245, 245)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val v = ValueAnimator.ofArgb(startColor, endColor)
                    v.addUpdateListener { animation ->
                        viewHolder?.itemView?.setBackgroundColor(
                            animation.animatedValue as Int
                        )
                    }
                    v.duration = 300
                    v.start()
                }
            }

            override fun onItemDragMoving(
                source: RecyclerView.ViewHolder?,
                from: Int,
                target: RecyclerView.ViewHolder?,
                to: Int
            ) {
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                // 结束时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                val startColor = Color.rgb(245, 245, 245)
                val endColor = Color.WHITE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val v = ValueAnimator.ofArgb(startColor, endColor)
                    v.addUpdateListener { animation ->
                        viewHolder?.itemView?.setBackgroundColor(
                            animation.animatedValue as Int
                        )
                    }
                    v.duration = 300
                    v.start()
                }
            }

        }
        mAdapter?.draggableModule?.isDragEnabled = true
        mAdapter?.draggableModule?.setOnItemDragListener(dragListener)
    }
}