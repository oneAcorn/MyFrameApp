package com.acorn.myframeapp.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.acorn.basemodule.extendfun.logI
import com.acorn.myframeapp.home.HomeFragment
import com.acorn.myframeapp.test.TestFragment

/**
 * Created by acorn on 2022/5/18.
 */
class MainPagerAdapter(fragmentManager: FragmentManager, behavior: Int) :
    FragmentStatePagerAdapter(fragmentManager, behavior) {
    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        logI("PagerAdapter create Fragment:$position")
        return when (position) {
            0 -> {
                HomeFragment.newInstance()
            }
            1 -> {
                TestFragment.newInstance()
            }
            else -> {
                HomeFragment.newInstance()
            }
        }
    }
}