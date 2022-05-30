package com.acorn.myframeapp.ui.popup

import com.acorn.myframeapp.demo.BaseDemoActivity
import com.acorn.myframeapp.demo.Demo
import kotlinx.android.synthetic.main.layout_demo.*

/**
 * Created by acorn on 2022/5/30.
 */
class PopupActivity : BaseDemoActivity() {

    companion object {
        private const val CLICK_FULLSCREEN_POPUP1 = 0
    }

    override fun getItems(): Array<Demo> = arrayOf(
        Demo("FullScreen popup", CLICK_FULLSCREEN_POPUP1)
    )

    override fun onItemClick(data: Demo, idOrPosition: Int) {
        when (idOrPosition) {
            CLICK_FULLSCREEN_POPUP1 -> {
                MyFullScreenPopup1(this).show(rootView)
            }
            else -> {}
        }
    }
}