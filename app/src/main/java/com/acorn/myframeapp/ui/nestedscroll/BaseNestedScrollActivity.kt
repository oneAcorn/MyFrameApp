package com.acorn.myframeapp.ui.nestedscroll

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.acorn.basemodule.extendfun.dp
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout
import kotlinx.android.synthetic.main.activity_base_nested_scroll.*

/**
 * Created by acorn on 2022/5/25.
 */
abstract class BaseNestedScrollActivity : BaseNoViewModelActivity() {
    private val mSavedScrollInfo = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_nested_scroll)
    }

    override fun initView() {
        super.initView()
        initPullRefreshLayout()
        initCoordinatorLayout()
        coordinatorLayout.setDraggableScrollBarEnabled(true)
    }

    private fun initPullRefreshLayout() {
        pullRefreshLayout.setOnPullListener(object : QMUIPullRefreshLayout.OnPullListener {
            override fun onMoveTarget(offset: Int) {
            }

            override fun onMoveRefreshView(offset: Int) {
            }

            override fun onRefresh() {
                pullRefreshLayout.postDelayed({
                    pullRefreshLayout.finishRefresh()
                }, 1200)
            }
        })
    }

    protected abstract fun initCoordinatorLayout()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nested_scroll_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var isConsume = true
        when (item.itemId) {
            R.id.action_scroll_to_bottom -> {
                coordinatorLayout.scrollToBottom()
            }
            R.id.action_scroll_to_top -> {
                coordinatorLayout.scrollToTop()
            }
            R.id.action_scroll_bottomview_to_top -> {
                coordinatorLayout.scrollBottomViewToTop()
            }
            R.id.action_scrollby_40 -> {
                coordinatorLayout.scrollBy(40.dp)
            }
            R.id.action_scrollby_minus_40 -> {
                coordinatorLayout.scrollBy((-40).dp)
            }
            R.id.action_scrollby_100dp_per_second -> {
                coordinatorLayout.smoothScrollBy(100.dp, 1000)
            }
            R.id.action_scrollby_minus_100dp_per_second -> {
                coordinatorLayout.smoothScrollBy((-100).dp, 1000)
            }
            R.id.action_save_current_scroll_info -> {
                coordinatorLayout.saveScrollInfo(mSavedScrollInfo)
            }
            R.id.action_restore_scroll_info -> {
                coordinatorLayout.restoreScrollInfo(mSavedScrollInfo)
            }
            else -> {
                isConsume = false
            }
        }
        return if (isConsume) true else super.onOptionsItemSelected(item)
    }
}