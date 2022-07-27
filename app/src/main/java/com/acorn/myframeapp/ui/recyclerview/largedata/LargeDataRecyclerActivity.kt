package com.acorn.myframeapp.ui.recyclerview.largedata

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.extendfun.getDrawableCompat
import com.acorn.basemodule.network.createViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import kotlinx.android.synthetic.main.activity_large_data_recyclerview.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by acorn on 2022/7/27.
 */
class LargeDataRecyclerActivity : BaseNoViewModelActivity() {
    private val viewModel by lazy { createViewModel(PagingViewModel::class.java) }
    private val mAdapter by lazy { LargeDataAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_large_data_recyclerview)
    }

    override fun initView() {
        super.initView()
        rv.layoutManager= LinearLayoutManager(this)
        rv.adapter=mAdapter
        rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
            getDrawableCompat(R.drawable.shape_recyclerview_divider)?.let { setDrawable(it) }
        })
        lifecycleScope.launch {//协程
            //https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/
            viewModel.dataFlow.collectLatest {
                mAdapter.submitData(it)
            }
        }
    }
}