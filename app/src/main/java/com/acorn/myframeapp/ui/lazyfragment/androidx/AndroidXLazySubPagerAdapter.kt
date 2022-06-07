package com.acorn.myframeapp.ui.lazyfragment.androidx

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by acorn on 2022/6/7.
 */
class AndroidXLazySubPagerAdapter(fragmentManager: FragmentManager, private val name: String) :
    FragmentPagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return MyAndroidXLazyFragment1.newInstance("$name$position")
    }
}