package com.acorn.myframeapp.ui.coroutine.flow

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.BaseActivity
import com.acorn.basemodule.network.createViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.ui.coroutine.flow.viewmodels.LatestNewsUiState
import com.acorn.myframeapp.ui.coroutine.flow.viewmodels.LatestNewsViewModel
import com.acorn.myframeapp.ui.recyclerview.adapter.ConventionalRecyclerAdapter
import kotlinx.android.synthetic.main.activity_coroutine_flow.*
import kotlinx.coroutines.launch

/**
 * https://developer.android.com/kotlin/flow?hl=en
 * Created by acorn on 2023/1/5.
 */
class CoroutineFlowActivity : BaseActivity<LatestNewsViewModel>() {
    private lateinit var adaper: ConventionalRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_flow)
    }

    override fun initView() {
        super.initView()
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adaper = ConventionalRecyclerAdapter(this, null)
        rv.adapter = adaper
    }

    override fun initListener() {
        super.initListener()
        initFlow()
    }

    override fun initData() {
        super.initData()
    }

    private fun initFlow() {
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                mViewModel?.uiState?.collect { uiState ->
                    when (uiState) {
                        is LatestNewsUiState.Success -> {
                            uiState.news.map {
                                it.content
                            }.let {
//                                adaper.append(it)
                                adaper.prependWithoutNotify(it)
                                adaper.notifyItemRangeInserted(0, it.size)
                            }
                        }
                        is LatestNewsUiState.Error -> {
                            showTip("error:${uiState.exception.message}")
                        }
                    }
                }
            }
        }
    }

    override fun getViewModel(): LatestNewsViewModel =
        createViewModel(LatestNewsViewModel::class.java)
}