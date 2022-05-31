package com.acorn.myframeapp.ui.popup

import android.os.Bundle
import com.acorn.basemodule.extendfun.showToast
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.acorn.myframeapp.demo.BaseDemoActivity
import com.acorn.myframeapp.demo.Demo
import kotlinx.android.synthetic.main.activity_popup.*
import kotlinx.android.synthetic.main.layout_demo.*

/**
 * Created by acorn on 2022/5/30.
 */
class PopupActivity : BaseNoViewModelActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)
    }

    override fun initView() {
        super.initView()
        showToolbar { it.title="PopupWindow" }
        btn1.setOnClickListener {
            TaskStepPopup(
                this,
                arrayListOf("啊", "abc", "范德萨", "发广告"),
                lifecycle
            ) { index, nodeName ->
                showToast("点击($index):$nodeName")
            }.showAsDropDown(it)
        }
        btn2.setOnClickListener {
            MyFullScreenPopup1(this).show(rootView)
        }
    }

}