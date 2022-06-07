package com.acorn.myframeapp.ui.lazyfragment.old

import android.os.Bundle
import android.view.View
import com.acorn.basemodule.extendfun.getColorCompat
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import kotlinx.android.synthetic.main.fragment_lazy_fragment.view.*
import me.majiajie.pagerbottomtabstrip.NavigationController

/**
 * Created by acorn on 2022/6/7.
 */
class MyFragment0 : LazyFragment<BaseNetViewModel>() {
    private lateinit var navigationController: NavigationController
    private var name: String? = null

    companion object {
        fun newInstance(name: String): MyFragment0 {
            val args = Bundle()
            args.putString("name", name)
            val fragment = MyFragment0()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        //For Test Log
        name = arguments?.getString("name")
        tagName = name
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun onFragmentFirstVisible() {
//        logI("$name onFragmentFirstVisible")
    }

    override fun initView(rootView: View?) {
        initNavigationBar(rootView)
        val pagerAdapter = OldLazySubPagerAdapter(childFragmentManager, "SubFragment")
        rootView?.vp?.run {
            adapter = pagerAdapter
            navigationController.setupWithViewPager(this)
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_lazy_fragment

    override fun onFragmentResume() {
        super.onFragmentResume()
//        logI("$name onFragmentResume")
    }

    override fun onFragmentPause() {
        super.onFragmentPause()
//        logI("$name onFragmentPause")
    }

    private fun initNavigationBar(rootView: View?) {
        rootView?.navigationView?.let {
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
}