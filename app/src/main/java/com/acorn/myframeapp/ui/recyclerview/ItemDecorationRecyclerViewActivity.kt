package com.acorn.myframeapp.ui.recyclerview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.extendfun.getColorCompat
import com.acorn.basemodule.extendfun.getDrawableCompat
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.acorn.myframeapp.extendfun.getRandomList
import com.acorn.myframeapp.ui.recyclerview.adapter.ItemDecorationRecyclerAdapter
import com.acorn.myframeapp.ui.recyclerview.itemdecoration.RecycleViewDivider
import kotlinx.android.synthetic.main.activity_conventional_recyclerview.*

/**
 * Created by acorn on 2022/5/25.
 */
class ItemDecorationRecyclerViewActivity : BaseNoViewModelActivity() {
    private var mAdapter: ItemDecorationRecyclerAdapter? = null
    private lateinit var linearDivider: DividerItemDecoration

    //适用于LinearLayoutManager/GridLayoutManager
    private var commonDivider: RecycleViewDivider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conventional_recyclerview)
    }

    override fun initView() {
        super.initView()
        showToolbar { it.title = "ItemDecoration" }
        linearDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
            getDrawableCompat(R.drawable.shape_recyclerview_divider)?.let { setDrawable(it) }
        }

        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv.addItemDecoration(linearDivider!!)
        mAdapter = ItemDecorationRecyclerAdapter(this, getRandomList())
        rv.adapter = mAdapter
    }

    override fun initData() {
        super.initData()
        mAdapter?.addHeaderView(getHeaderView())
    }

    private fun getHeaderView(): View {
        val view = layoutInflater.inflate(R.layout.view_header, rv, false)
        view.setOnClickListener {
            showTip("Header click")
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item_decoration_recyclerview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var isConsume = true
        when (item.itemId) {
            R.id.action_linearlayout -> {
                commonDivider?.let { rv.removeItemDecoration(it) }
//                linearDivider?.let { rv.removeItemDecoration(it) }
                rv.addItemDecoration(linearDivider)
                rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            }
            R.id.action_gridlayout_color -> {
                rv.removeItemDecoration(linearDivider)
                commonDivider?.let { rv.removeItemDecoration(it) }
                commonDivider = RecycleViewDivider(
                    RecycleViewDivider.MODE_GRID,
                    getColorCompat(com.qmuiteam.qmui.R.color.qmui_config_color_red),
                    4, 0, 0
                )
                rv.addItemDecoration(commonDivider!!)
                rv.layoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
                //不加这行的话,HearView无法整行显示
                rv.adapter = mAdapter
            }
            R.id.action_gridlayout_img -> {
                rv.removeItemDecoration(linearDivider)
                commonDivider?.let { rv.removeItemDecoration(it) }
                commonDivider = RecycleViewDivider(
                    RecycleViewDivider.MODE_GRID,
                    this,
                    R.drawable.baseline_home_black_48
                )
                rv.addItemDecoration(commonDivider!!)
                rv.layoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
                //不加这行的话,HearView无法整行显示
                rv.adapter = mAdapter
            }
            else -> {
                isConsume = false
            }
        }
        return if (isConsume) true else super.onOptionsItemSelected(item)
    }
}