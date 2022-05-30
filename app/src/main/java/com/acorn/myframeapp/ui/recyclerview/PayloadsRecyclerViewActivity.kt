package com.acorn.myframeapp.ui.recyclerview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.acorn.myframeapp.extendfun.getRandomList
import com.acorn.myframeapp.ui.recyclerview.adapter.ConventionalRecyclerAdapter
import com.acorn.myframeapp.ui.recyclerview.adapter.PayloadsRecyclerAdapter
import kotlinx.android.synthetic.main.activity_conventional_recyclerview.*

/**
 * Created by acorn on 2022/5/24.
 */
class PayloadsRecyclerViewActivity : BaseNoViewModelActivity() {
    private var mAdapter: PayloadsRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conventional_recyclerview)
    }

    override fun initView() {
        super.initView()
        showToolbar { it.title = "PayloadsRecyclerView" }
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = PayloadsRecyclerAdapter(this, null)
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
        val list = mutableListOf<PayloadsRecyclerAdapter.PayloadsData>()
        for (i in 0..2) {
            val item =
                PayloadsRecyclerAdapter.PayloadsData(i.toString(), "我是要更新的View1", "我是要更新的View2")
            list.add(item)
        }
        mAdapter?.setData(list)
    }

    private fun refreshSingleView1() {
        val data0 = mAdapter?.data?.get(0)
        data0 ?: return
        mAdapter ?: return
        data0.str1 = "修改后的值"
        mAdapter?.notifyItemChanged(
            0 + mAdapter!!.headerLayoutCount,
            PayloadsRecyclerAdapter.PAYLOAD_TV1
        )
    }

    private fun refreshSingleView2() {
        val data0 = mAdapter?.data?.get(0)
        data0 ?: return
        mAdapter ?: return
        data0.str2 = "修改后的值2"
        mAdapter?.notifyItemChanged(
            0 + mAdapter!!.headerLayoutCount,
            PayloadsRecyclerAdapter.PAYLOAD_TV2
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_payloads_recyclerview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var isConsume = true
        when (item.itemId) {
            R.id.action_refresh_single_view -> {
                refreshSingleView1()
            }
            R.id.action_refresh_single_view2 -> {
                refreshSingleView2()
            }
            else -> {
                isConsume = false
            }
        }
        return if (isConsume) true else super.onOptionsItemSelected(item)
    }
}