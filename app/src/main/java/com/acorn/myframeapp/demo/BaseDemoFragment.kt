package com.acorn.myframeapp.demo

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.BaseFragment
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import kotlinx.android.synthetic.main.layout_demo.*

/**
 * Created by acorn on 2022/5/19.
 */
abstract class BaseDemoFragment : BaseFragment<BaseNetViewModel>() {
    private val onItemClickListener: (data: Demo, position: Int) -> Unit =
        { data, position ->
            if (data.activity != null) {
                val intent = Intent(requireContext(), data.activity)
                if (null != data.bundle) {
                    intent.putExtras(data.bundle)
                }
                startActivity(intent)
            } else {
                onItemClick(data, position)
            }
        }

    override fun initView() {
        super.initView()
        rv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = DemoAdapter(requireContext(), getItems()).apply {
                onItemClickListener = this@BaseDemoFragment.onItemClickListener
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.layout_demo

    abstract fun getItems(): Array<Demo>

    abstract fun onItemClick(data: Demo, idOrPosition: Int)

    override fun createViewModel(): BaseNetViewModel? = null
}