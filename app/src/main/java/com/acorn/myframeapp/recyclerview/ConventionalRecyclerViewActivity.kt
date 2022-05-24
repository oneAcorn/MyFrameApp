package com.acorn.myframeapp.recyclerview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.BaseActivity
import com.acorn.basemodule.base.BaseRecyclerAdapter
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.recyclerview.adapter.ConventionalRecyclerAdapter
import kotlinx.android.synthetic.main.activity_conventional_recyclerview.*
import kotlin.random.Random

/**
 * RecyclerView常规使用.
 * Created by acorn on 2022/5/23.
 */
class ConventionalRecyclerViewActivity : BaseActivity<BaseNetViewModel>() {
    private var mAdapter: ConventionalRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conventional_recyclerview)
    }

    override fun initView() {
        super.initView()
        showToolbar("ConventionalRecyclerViewActivity")
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = ConventionalRecyclerAdapter(this, null)
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
        randomData()
    }

    private fun randomData() {
        mAdapter?.setData(getRandomList())
    }

    private fun addData() {
        mAdapter?.add(Random.nextInt(mAdapter?.itemCount ?: 0), randomString())
    }

    private fun prependData() {
        mAdapter?.prepend(getRandomList())
    }

    private fun appendData() {
        mAdapter?.append(getRandomList())
    }

    private fun removeData() {
        mAdapter?.remove(Random.nextInt(mAdapter?.itemCount ?: 0))
    }

    private fun clearData() {
        mAdapter?.setData(null)
    }

    private fun getRandomList(): List<String> {
        val list = mutableListOf<String>()
        for (i in 0..Random.nextInt(10)) {
            list.add(randomString())
        }
        return list
    }

    private fun randomString(): String = "RandomItem:${Random.nextInt(200)}"

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_conventional_recyclerview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var isConsume = true
        when (item.itemId) {
            R.id.action_random_data -> {
                randomData()
            }
            R.id.action_add_data -> {
                addData()
            }
            R.id.action_prepend_data -> {
                prependData()
            }
            R.id.action_append_data -> {
                appendData()
            }
            R.id.action_remove_data -> {
                removeData()
            }
            R.id.action_clear_data -> {
                clearData()
            }
            else -> {
                isConsume = false
            }
        }
        return if (isConsume) true else super.onOptionsItemSelected(item)
    }

    override fun getViewModel(): BaseNetViewModel? = null

    override fun isEmbedInBaseLayout(): Boolean = true
}