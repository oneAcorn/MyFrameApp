package com.acorn.myframeapp.ui.lazyfragment.old

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by acorn on 2022/6/7.
 */
class OldLazySubPagerAdapter(fragmentManager: FragmentManager, private val name:String) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return MyFragment1.newInstance("$name$position")
    }
}