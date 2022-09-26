package com.acorn.myframeapp.ui.nestedscroll

import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.myframeapp.extendfun.getRandomList
import com.acorn.myframeapp.ui.recyclerview.adapter.ConventionalRecyclerAdapter
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedBottomAreaBehavior
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedBottomRecyclerView
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedTopAreaBehavior
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedTopWebView
import com.qmuiteam.qmui.widget.webview.QMUIWebView
import kotlinx.android.synthetic.main.activity_base_nested_scroll.*

/**
 * Webview+RecyclerView嵌套滑动
 * Created by acorn on 2022/5/25.
 */
class WebviewRecyclerNestedActivity : BaseNestedScrollActivity() {
    private var mNestedWebview: QMUIWebView? = null
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ConventionalRecyclerAdapter

    override fun initCoordinatorLayout() {
        mNestedWebview = QMUIContinuousNestedTopWebView(this)
        val matchParent = ViewGroup.LayoutParams.MATCH_PARENT
        val webviewLp = CoordinatorLayout.LayoutParams(matchParent, matchParent)
        webviewLp.behavior = QMUIContinuousNestedTopAreaBehavior(this)
        coordinatorLayout.setTopAreaView(mNestedWebview, webviewLp)

        mRecyclerView = QMUIContinuousNestedBottomRecyclerView(this)
        val recyclerViewLp = CoordinatorLayout.LayoutParams(matchParent, matchParent)
        recyclerViewLp.behavior = QMUIContinuousNestedBottomAreaBehavior()
        coordinatorLayout.setBottomAreaView(mRecyclerView, recyclerViewLp)

        mNestedWebview?.loadUrl("http://jandan.net/p/110703")

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = ConventionalRecyclerAdapter(this, null)
        mRecyclerView.adapter = mAdapter
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

    override fun initView() {
        super.initView()
        showToolbar { it.title="Webview+RecyclerView" }
    }

    override fun initData() {
        super.initData()
        mAdapter.setData(getRandomList())
    }

    override fun onDestroy() {
        super.onDestroy()
        mNestedWebview?.let {
            coordinatorLayout.removeView(it)
            it.destroy()
            mNestedWebview = null
        }
    }
}