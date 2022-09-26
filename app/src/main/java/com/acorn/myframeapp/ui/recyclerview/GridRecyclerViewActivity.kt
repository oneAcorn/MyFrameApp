package com.acorn.myframeapp.ui.recyclerview

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.acorn.myframeapp.extendfun.getRandomList
import com.acorn.myframeapp.ui.recyclerview.adapter.ConventionalRecyclerAdapter
import com.acorn.myframeapp.ui.recyclerview.adapter.GridRecyclerAdapter
import kotlinx.android.synthetic.main.activity_conventional_recyclerview.*

/**
 * Created by acorn on 2022/5/24.
 */
class GridRecyclerViewActivity : BaseNoViewModelActivity() {
    private var mAdapter: GridRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conventional_recyclerview)
    }

    override fun initView() {
        super.initView()
        showToolbar {
            it.title = "GridRecyclerView"
        }
        rv.layoutManager = GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType =
                        mAdapter?.getItemViewType(position) ?: GridRecyclerAdapter.ITEM_TYPE_2
                    return if (viewType == GridRecyclerAdapter.ITEM_TYPE_1) 1 else 4
                }
            }
        }
        mAdapter = GridRecyclerAdapter(this, null)
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
        mAdapter?.addHeaderView(getHeaderView())
        mAdapter?.addFooterView(getFooterView())
        mAdapter?.setData(getRandomList())
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
}