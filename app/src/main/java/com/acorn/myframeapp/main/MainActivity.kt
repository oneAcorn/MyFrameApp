package com.acorn.myframeapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import com.acorn.basemodule.base.BaseActivity
import com.acorn.basemodule.extendfun.getColorCompat
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import kotlinx.android.synthetic.main.activity_main.*
import me.majiajie.pagerbottomtabstrip.NavigationController

class MainActivity : BaseActivity<BaseNetViewModel>() {
    private lateinit var navigationController: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun initView() {
        super.initView()
        initNavigationBar()
        val pagerAdapter = MainPagerAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        vp.adapter = pagerAdapter
//        navigationController.setupWithViewPager(vp.view)
        navigationController.setupWithViewPager(vp)
    }

    private fun initNavigationBar() {
        navigationController = navigationView.material()
            .addItem(
                R.drawable.baseline_home_black_48,
                "首页",
                getColorCompat(R.color.navigation_check_color)
            )
            .addItem(
                R.drawable.baseline_build_black_48,
                "测试",
                getColorCompat(R.color.navigation_check_color)
            )
            .setDefaultColor(getColorCompat(R.color.navigation_default_color))
            .enableAnimateLayoutChanges()
            .build()
        navigationController.setHasMessage(0, true)
        navigationController.setMessageNumber(1, 3)
    }

    override fun getViewModel(): BaseNetViewModel? = null
}