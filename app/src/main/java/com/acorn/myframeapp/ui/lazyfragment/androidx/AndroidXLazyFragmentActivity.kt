package com.acorn.myframeapp.ui.lazyfragment.androidx

import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import com.acorn.basemodule.extendfun.getColorCompat
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.acorn.myframeapp.ui.main.MainPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import me.majiajie.pagerbottomtabstrip.NavigationController

/**
 * Created by acorn on 2022/6/7.
 */
class AndroidXLazyFragmentActivity : BaseNoViewModelActivity() {
    private lateinit var navigationController: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun initView() {
        super.initView()
        initNavigationBar()
        val pagerAdapter = AndroidXLazyPagerAdapter(supportFragmentManager, "RootFragment")
        vp.adapter = pagerAdapter
//        navigationController.setupWithViewPager(vp.view)
        navigationController.setupWithViewPager(vp)
    }

    private fun initNavigationBar() {
        navigationController = navigationView.material()
            .addItem(
                R.drawable.baseline_home_black_48,
                "页签1",
                getColorCompat(R.color.navigation_check_color)
            )
            .addItem(
                R.drawable.baseline_build_black_48,
                "页签2",
                getColorCompat(R.color.navigation_check_color)
            )
            .setDefaultColor(getColorCompat(R.color.navigation_default_color))
            .enableAnimateLayoutChanges()
            .build()
//        navigationController.setHasMessage(0, true)
//        navigationController.setMessageNumber(1, 3)
    }
}