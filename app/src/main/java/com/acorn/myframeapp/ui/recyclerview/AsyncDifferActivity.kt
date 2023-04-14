package com.acorn.myframeapp.ui.recyclerview

import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.BaseBindingActivity
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.bean.Student
import com.acorn.myframeapp.databinding.ActivityConventionalRecyclerviewBinding
import com.acorn.myframeapp.ui.recyclerview.adapter.AsyncDifferAdapter
import kotlinx.android.synthetic.main.activity_conventional_recyclerview.*
import java.util.UUID

/**
 * Created by acorn on 2023/4/12.
 */
class AsyncDifferActivity :
    BaseBindingActivity<BaseNetViewModel, ActivityConventionalRecyclerviewBinding>() {
    private var mAdapter: AsyncDifferAdapter? = null

    override fun initView() {
        super.initView()
        showToolbar("AsyncDifferActivity")
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = AsyncDifferAdapter()
        rv.adapter = mAdapter
    }

    override fun initData() {
        super.initData()
        val list = mutableListOf<Student>()
        for (i in 0..20) {
            list.add(Student(UUID.randomUUID().toString(), "学生$i", i))
        }
        mAdapter?.setData(list)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_async_differ, menu)
        return true
    }

    private fun addData() {
        mAdapter?.add(2, Student(UUID.randomUUID().toString(), "Add Student", 10))
    }

    private fun prependData() {
        val list = mutableListOf<Student>()
        for (i in 0..3) {
            list.add(Student(UUID.randomUUID().toString(), "prepend 学生$i", i))
        }
        mAdapter?.prepend(list)
    }

    private fun appendData() {
        val list = mutableListOf<Student>()
        for (i in 0..3) {
            list.add(Student(UUID.randomUUID().toString(), "append 学生$i", i))
        }
        mAdapter?.append(list)
    }

    private fun removeData() {
        mAdapter?.remove(0)
    }

    private fun clearData() {
        mAdapter?.clearData()
    }

    private fun modifyData() {
        val newStudent = mAdapter?.getReadOnlyData()?.get(0)?.copy()?.also {
            it.age = 99
        }
        mAdapter?.replace(0, newStudent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var isConsume = true
        when (item.itemId) {
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
            R.id.modify_item_zero -> {
                modifyData()
            }
            else -> {
                isConsume = false
            }
        }
        return if (isConsume) true else super.onOptionsItemSelected(item)
    }

    override fun getViewModel(): BaseNetViewModel? = null
}