package com.acorn.myframeapp.ui.coroutines.flow

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.BaseBindingActivity
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.basemodule.network.createViewModel
import com.acorn.myframeapp.databinding.ActivityCancelableFlowBinding
import com.acorn.myframeapp.ui.coroutines.flow.viewmodels.CancelableNewsViewModel
import com.acorn.myframeapp.ui.coroutines.flow.viewmodels.LatestNewsUiState
import com.acorn.myframeapp.ui.recyclerview.adapter.ConventionalRecyclerAdapter
import kotlinx.coroutines.launch

/**
 * Created by acorn on 2023/2/17.
 */
class CancelableFlowActivity :
    BaseBindingActivity<CancelableNewsViewModel, ActivityCancelableFlowBinding>() {
    private lateinit var adaper: ConventionalRecyclerAdapter

    override fun initView() {
        super.initView()
        binding.rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adaper = ConventionalRecyclerAdapter(this, null)
        binding.rv.adapter = adaper
    }

    override fun initListener() {
        super.initListener()
        binding.startBtn.singleClick {
            mViewModel?.startFetchNews()
        }
        binding.stopBtn.singleClick {
            mViewModel?.stopFetchNews()
        }
    }

    override fun initData() {
        super.initData()
        initFlow()
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
                                binding.rv.smoothScrollToPosition(0)
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

    override fun getViewModel(): CancelableNewsViewModel =
        createViewModel(CancelableNewsViewModel::class.java)
}