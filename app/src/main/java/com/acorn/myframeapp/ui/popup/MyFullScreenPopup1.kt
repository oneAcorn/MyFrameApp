package com.acorn.myframeapp.ui.popup

import android.content.Context
import android.view.View
import com.acorn.basemodule.base.popup.FullScreenPopup
import com.acorn.basemodule.extendfun.showToast
import com.acorn.myframeapp.R
import kotlinx.android.synthetic.main.popup_my_full_screen1.view.*

/**
 * Created by acorn on 2022/5/30.
 */
class MyFullScreenPopup1(context: Context) : FullScreenPopup(context) {
    override fun getLayoutId(): Int = R.layout.popup_my_full_screen1

    override fun initView(view: View) {
        super.initView(view)

        onBlankClickCallback= {
            showToast("点击空白")
        }

        view.tv1.text = "abcdefg,啊我额"
        view.iv.setOnClickListener {
            showToast("点击")
        }
    }
}