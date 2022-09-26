package com.acorn.myframeapp.ui.pulllayout

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.acorn.myframeapp.extendfun.getRandomList
import com.acorn.myframeapp.ui.recyclerview.adapter.ConventionalRecyclerAdapter
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout
import kotlinx.android.synthetic.main.activity_qmui_pull_layout.*

/**
 * 注:此Activity或整个Application的Theme需要继承自QMUI.Compat.NoActionBar.
 * 否则QMUIPullLoadMoreView会拿不到相应的属性.
 * Created by acorn on 2022/5/26.
 */
class QMUIPullLayoutActivity : BaseNoViewModelActivity() {
    private lateinit var mAdapter: ConventionalRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qmui_pull_layout)
    }

    override fun initView() {
        super.initView()
        showToolbar { it.title = "QMUI PullLayout" }
        initPullLayout()
        initRecyclerView()
    }

    private fun initPullLayout() {
        pullLayout.setActionListener {
            pullLayout.postDelayed({
                if (it.pullEdge == QMUIPullLayout.PULL_EDGE_TOP) {
                    refreshData()
                } else if (it.pullEdge == QMUIPullLayout.PULL_EDGE_BOTTOM) {
                    loadMoreData()
                }
                pullLayout.finishActionRun(it)
            }, 2000)
        }
    }

    private fun initRecyclerView() {
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = ConventionalRecyclerAdapter(this, getRandomList(15))
        rv.adapter = mAdapter
        mAdapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<String> {
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

    private fun refreshData() {
        mAdapter.prepend(getRandomList(5))
        rv.scrollToPosition(0)
    }

    private fun loadMoreData() {
        mAdapter.append(getRandomList(3))
    }
}