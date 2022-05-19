package com.acorn.basemodule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by acorn on 2022/5/18.
 */
abstract class BaseFragment : Fragment() {
    private var mIsFirstVisible = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
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
}