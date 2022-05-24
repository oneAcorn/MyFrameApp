package com.acorn.myframeapp.recyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.BaseActivity
import com.acorn.basemodule.base.BaseRecyclerAdapter
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.recyclerview.adapter.HeaderFooterAdapter
import kotlinx.android.synthetic.main.activity_conventional_recyclerview.*
import kotlin.random.Random

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

    private fun getHeaderView(): View {
        val view = layoutInflater.inflate(R.layout.view_header, rv, false)
        view.setOnClickListener {
            showTip("Header click")
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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
            }
            R.id.action_remove_header -> {
                removeHeader()
            }
            R.id.action_remove_footer -> {
            }
            else -> {
                isConsume = false
            }
        }
        return if (isConsume) true else super.onOptionsItemSelected(item)
    }

    private fun getRandomList(): List<String> {
        val list = mutableListOf<String>()
        for (i in 0..Random.nextInt(100)) {
            list.add(randomString())
        }
        return list
    }

    private fun randomString(): String = "RandomItem:${Random.nextInt(200)}"

    override fun isEmbedInBaseLayout(): Boolean = true
}