package com.acorn.myframeapp.recyclerview

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.acorn.myframeapp.recyclerview.adapter.ConventionalRecyclerAdapter
import kotlinx.android.synthetic.main.activity_conventional_recyclerview.*
import kotlinx.android.synthetic.main.activity_conventional_recyclerview.rv
import kotlinx.android.synthetic.main.activity_empty_recyclerview.*
import kotlin.random.Random

/**
 * Created by acorn on 2022/5/24.
 */
class EmptyRecyclerViewActivity : BaseNoViewModelActivity() {
    private var status = 0
    private var mAdapter: ConventionalRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty_recyclerview)
    }

    override fun initView() {
        super.initView()
        showToolbar {
            it.title = "EmptyRecyclerView"
        }
        refreshBtn.setOnClickListener {
            status = 0
            mAdapter?.clearData()
            refreshNet()
        }

        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = ConventionalRecyclerAdapter(this, null)
        rv.adapter = mAdapter
    }

    override fun initData() {
        super.initData()
        refreshNet()
    }

    private fun getLoadingView(): View {
        return layoutInflater.inflate(R.layout.view_loading, rv, false)
    }

    private fun getErrorView(): View {
        val view =
            layoutInflater.inflate(R.layout.view_error, rv, false)
        view.setOnClickListener {
            refreshNet()
        }
        return view
    }

    private fun getEmptyView(): View {
        return layoutInflater.inflate(R.layout.view_empty_data, rv, false)
            .apply {
                setOnClickListener {
                    refreshNet()
                }
            }
    }

    override fun refreshNet() {
        super.refreshNet()
        mAdapter?.setEmptyView(getLoadingView())
        Handler().postDelayed({
            if (status == 0) {
                status++
                mAdapter?.setEmptyView(getErrorView())
            } else if (status == 1) {
                status++
                mAdapter?.setEmptyView(getEmptyView())
            } else if (status == 2) {
                status = 0
                mAdapter?.setData(getRandomList())
            }
        }, 2000)
    }

    private fun getRandomList(): List<String> {
        val list = mutableListOf<String>()
        for (i in 0..Random.nextInt(100)) {
            list.add(randomString())
        }
        return list
    }

    private fun randomString(): String = "RandomItem:${Random.nextInt(200)}"
}