package com.acorn.myframeapp.ui.recyclerview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.acorn.myframeapp.extendfun.getRandomList
import com.acorn.myframeapp.ui.recyclerview.adapter.ConventionalRecyclerAdapter
import com.acorn.myframeapp.ui.recyclerview.animation.CustomAnimation1
import com.acorn.myframeapp.ui.recyclerview.animation.CustomAnimation2
import kotlinx.android.synthetic.main.activity_conventional_recyclerview.*

/**
 * Created by acorn on 2022/5/24.
 */
class AnimationRecyclerViewActivity : BaseNoViewModelActivity() {
    private var mAdapter: ConventionalRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conventional_recyclerview)
    }

    override fun initView() {
        super.initView()
        showToolbar {
            it.title = "AnimationRecyclerView"
        }

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = ConventionalRecyclerAdapter(this, null)
        //Item动画
        mAdapter?.animationEnable = true
        //是否只播放一次
        mAdapter?.isAnimationFirstOnly = false
        rv.adapter = mAdapter
        mAdapter?.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View, position: Int, itemViewType: Int) {
                showTip(
                    "position:$position,itemViewType:$itemViewType,data:${
                        mAdapter?.getItem(
                            position
                        )
                    }}"
                )
            }
        })
    }

    override fun initData() {
        super.initData()
        mAdapter?.setData(getRandomList(600))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_animation_recyclerview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var isConsume = true
        when (item.itemId) {
            R.id.action_alpha_in -> {
                mAdapter?.setAnimationWithDefault(BaseRecyclerAdapter.AnimationType.AlphaIn)
            }
            R.id.action_scale_in -> {
                mAdapter?.setAnimationWithDefault(BaseRecyclerAdapter.AnimationType.ScaleIn)
            }
            R.id.action_slide_in_bottom -> {
                mAdapter?.setAnimationWithDefault(BaseRecyclerAdapter.AnimationType.SlideInBottom)
            }
            R.id.action_slide_in_left -> {
                mAdapter?.setAnimationWithDefault(BaseRecyclerAdapter.AnimationType.SlideInLeft)
            }
            R.id.action_slide_in_right -> {
                mAdapter?.setAnimationWithDefault(BaseRecyclerAdapter.AnimationType.SlideInRight)
            }
            R.id.action_custom1 -> {
                mAdapter?.adapterAnimation = CustomAnimation1()
            }
            R.id.action_custom2 -> {
                mAdapter?.adapterAnimation = CustomAnimation2()
            }
            else -> {
                isConsume = false
            }
        }
//        if (isConsume) {
//            rv.adapter = mAdapter
//        }
        return if (isConsume) true else super.onOptionsItemSelected(item)
    }
}