package com.acorn.myframeapp.ui.recyclerview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.BaseActivity
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.extendfun.getRandomList
import com.acorn.myframeapp.ui.recyclerview.adapter.HeaderFooterAdapter
import kotlinx.android.synthetic.main.activity_conventional_recyclerview.*

/**
 * Created by acorn on 2022/5/23.
 */
class HeaderFooterRecyclerViewActivity : BaseActivity<BaseNetViewModel>() {
    private var mAdapter: HeaderFooterAdapter? = null

    override fun getViewModel(): BaseNetViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_header_footer_recyclerview)
    }

    override fun initView() {
        super.initView()
        showToolbar {
            it.title = "HeaderFooterRecyclerView"
        }
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = HeaderFooterAdapter(this, null)
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
        addHeader()
        randomData()
    }

    private fun randomData() {
        mAdapter?.setData(getRandomList())
    }

    private fun clearData() {
        mAdapter?.setData(null)
    }

    private fun addHeader() {
        mAdapter?.addHeaderView(getHeaderView())
    }

    private fun removeHeader() {
        mAdapter?.removeHeaderView(0)
    }

    private fun addFooter() {
        mAdapter?.addFooterView(getFooterView())
    }

    private fun removeFooter() {
        mAdapter?.removeFooterView(0)
    }

    private fun testNotify() {
        mAdapter?.data?.set(2, "what")
        mAdapter?.notifyItemChanged(3)
    }

    private fun getHeaderView(): View {
        val view = layoutInflater.inflate(R.layout.view_header, rv, false)
        view.setOnClickListener {
            showTip("Header click")
        }
        return view
    }

    private fun getFooterView(): View {
        return layoutInflater.inflate(R.layout.view_footer, rv, false).apply {
            setOnClickListener {
                showTip("Footer click")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_header_footer_recyclerview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var isConsume = true
        when (item.itemId) {
            R.id.action_add_data -> {
                randomData()
            }

            R.id.action_add_header -> {
                addHeader()
            }

            R.id.action_clear_data -> {
                clearData()
            }

            R.id.action_add_footer -> {
                addFooter()
            }

            R.id.action_remove_header -> {
                removeHeader()
            }

            R.id.action_remove_footer -> {
                removeFooter()
            }

            R.id.action_test_notify -> {
                testNotify()
            }

            else -> {
                isConsume = false
            }
        }
        return if (isConsume) true else super.onOptionsItemSelected(item)
    }

    override fun isEmbedInBaseLayout(): Boolean = true
}