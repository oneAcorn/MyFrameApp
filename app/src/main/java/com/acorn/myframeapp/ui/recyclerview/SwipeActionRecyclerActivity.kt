package com.acorn.myframeapp.ui.recyclerview

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.acorn.myframeapp.extendfun.getRandomList
import com.acorn.myframeapp.ui.recyclerview.adapter.ConventionalRecyclerAdapter
import com.acorn.myframeapp.ui.recyclerview.adapter.SwipeActionRecyclerAdapter
import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction
import kotlinx.android.synthetic.main.activity_conventional_recyclerview.*

/**
 * Created by acorn on 2022/5/25.
 */
class SwipeActionRecyclerActivity : BaseNoViewModelActivity() {
    private var mAdapter: SwipeActionRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conventional_recyclerview)
    }

    override fun initView() {
        super.initView()
        showToolbar { it.title = "SwipeAction" }
        getSwipeAction().attachToRecyclerView(rv)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = SwipeActionRecyclerAdapter(this, getRandomList())
        rv.adapter = mAdapter
        mAdapter?.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View, position: Int, itemViewType: Int) {
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
    }

    private fun getSwipeAction(): QMUIRVItemSwipeAction {
        return QMUIRVItemSwipeAction(true, object : QMUIRVItemSwipeAction.Callback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showTip("onSwiped?")
                mAdapter?.remove(viewHolder.adapterPosition)
            }

            override fun getSwipeDirection(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return QMUIRVItemSwipeAction.SWIPE_LEFT
            }

            override fun onClickAction(
                swipeAction: QMUIRVItemSwipeAction?,
                selected: RecyclerView.ViewHolder?,
                action: QMUISwipeAction?
            ) {
                super.onClickAction(swipeAction, selected, action)
                showTip("你点击了第${selected?.adapterPosition}个 item 的${action?.text}")
                if (action == mAdapter?.mDeleteAction) {
                    selected?.adapterPosition?.let { mAdapter?.remove(it) }
                } else {
                    swipeAction?.clear()
                }
            }
        })
    }
}