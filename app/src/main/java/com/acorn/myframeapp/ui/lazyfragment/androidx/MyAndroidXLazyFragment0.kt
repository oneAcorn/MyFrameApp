package com.acorn.myframeapp.ui.lazyfragment.androidx

import android.os.Bundle
import android.view.View
import com.acorn.basemodule.base.BaseFragment
import com.acorn.basemodule.extendfun.getColorCompat
import com.acorn.basemodule.extendfun.logI
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import kotlinx.android.synthetic.main.fragment_lazy_fragment.*
import kotlinx.android.synthetic.main.fragment_lazy_fragment.view.*
import me.majiajie.pagerbottomtabstrip.NavigationController

/**
 * Created by acorn on 2022/6/7.
 */
class MyAndroidXLazyFragment0 : BaseFragment<BaseNetViewModel>() {
    private lateinit var navigationController: NavigationController
    private var name: String? = null

    companion object {
        fun newInstance(name: String): MyAndroidXLazyFragment0 {
            val args = Bundle()
            args.putString("name", name)
            val fragment = MyAndroidXLazyFragment0()
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * androidx.fragment 1.1.0之后通过BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT可以直接
     * 在onResume中判断Fragment可见状态
     */
    override fun onResume() {
        super.onResume()
        logI("$name OnResume")
    }

    override fun initView() {
        super.initView()
        name = arguments?.getString("name")
        initNavigationBar()
        val pagerAdapter = AndroidXLazySubPagerAdapter(childFragmentManager, "SubFragment")
        vp?.run {
            adapter = pagerAdapter
            navigationController.setupWithViewPager(this)
        }
    }

    private fun initNavigationBar() {
        navigationView?.let {
            navigationController = it.navigationView.material()
                .addItem(
                    R.drawable.baseline_home_black_48,
                    "子页签1",
                    requireContext().getColorCompat(R.color.navigation_check_color)
                )
                .addItem(
                    R.drawable.baseline_build_black_48,
                    "子页签2",
                    requireContext().getColorCompat(R.color.navigation_check_color)
                )
                .setDefaultColor(requireContext().getColorCompat(R.color.navigation_default_color))
                .enableAnimateLayoutChanges()
                .build()
//        navigationController.setHasMessage(0, true)
//        navigationController.setMessageNumber(1, 3)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_lazy_fragment

    override fun getViewModel(): BaseNetViewModel? = null
}