package com.acorn.myframeapp.ui.lazyfragment.old

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by acorn on 2022/6/7.
 */
class OldLazyPagerAdapter(fragmentManager: FragmentManager,private val name:String) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        if(position==0){
            return MyFragment0.newInstance("$name$position")
        }
        return MyFragment1.newInstance("$name$position")
    }
}