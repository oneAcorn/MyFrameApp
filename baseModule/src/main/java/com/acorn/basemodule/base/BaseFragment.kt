package com.acorn.basemodule.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acorn.basemodule.R
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.basemodule.network.INetworkUI
import kotlinx.android.synthetic.main.base_fragment_layout.*
import kotlinx.android.synthetic.main.base_fragment_layout.view.*

/**
 * Created by acorn on 2022/5/18.
 */
abstract class BaseFragment : Fragment(), INetworkUI {
    private var mIsFirstVisible = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.base_fragment_layout, container, false)
        inflater.inflate(getLayoutId(), rootView.baseContentLayout)
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParameters()
    }

    override fun onResume() {
        super.onResume()
        //懒加载,如果在ViewPager中使用,需要用BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        if (mIsFirstVisible) {
            mIsFirstVisible = false
            initView()
            initData()
        }
    }

    abstract fun getLayoutId(): Int

    open fun initParameters() {}

    open fun initView() {}

    open fun initData() {}

    /**
     * 网络错误布局
     */
    open fun errorLayoutResId(): Int = R.layout.common_net_fail_view

    open fun nullLayoutResId(): Int = R.layout.common_empty_view

    /**
     * 重新加载网络
     */
    open fun refreshNet() {

    }

    override fun showProgressDialog() {
    }

    override fun dismissProgressDialog() {
    }

    override fun showErrorLayout() {
        baseContentLayout.visibility = View.GONE
        baseErrorLayout.visibility = View.VISIBLE
        baseErrorLayout.removeAllViews()
        LayoutInflater.from(requireContext()).inflate(errorLayoutResId(), baseErrorLayout)
        baseErrorLayout.findViewById<View>(R.id.refreshNetBtn)?.singleClick {
            refreshNet()
        }
    }

    override fun showContentLayout() {
        baseContentLayout.visibility = View.VISIBLE
        baseErrorLayout.visibility = View.GONE
    }

    /**
     * 整体展示空布局,特殊情况可重写.
     */
    override fun showNullLayout() {
        baseContentLayout.visibility = View.GONE
        baseErrorLayout.visibility = View.VISIBLE
        baseErrorLayout.removeAllViews()
        LayoutInflater.from(context).inflate(nullLayoutResId(), baseErrorLayout)
    }

    override fun showToast(string: String) {
    }
}