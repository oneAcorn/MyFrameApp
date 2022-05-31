package com.acorn.basemodule.base.popup

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.acorn.basemodule.extendfun.getDisplayCompat
import com.acorn.basemodule.extendfun.getNavigationBarHeight
import com.acorn.basemodule.extendfun.hasNavigationBar
import com.acorn.basemodule.extendfun.screenHeight

/**
 * Created by acorn on 2022/5/7.
 */
abstract class CommonBasePopupWindow(
    private val activity: Activity,
    @LayoutRes layoutId: Int,
    lifecycle: Lifecycle? = null
) :
    PopupWindow() {
    protected val rootView: View = LayoutInflater.from(activity).inflate(layoutId, null)
    private var popHeight = 0

    init {
        contentView = rootView
        width = getPopWidth()
        lifecycle?.addObserver(LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    dismiss()
                }
                else -> {
                }
            }
        })
    }

    override fun showAsDropDown(anchor: View) {
        if (popHeight == 0) {
            popHeight = calculateHeight(anchor)
            if (popHeight > 0)
                height = popHeight
        }
        super.showAsDropDown(anchor)
    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int, gravity: Int) {
        var vXOff = xoff
        var vYOff = yoff
        if (gravity == Gravity.RIGHT) {
            val offPoint = getXYOffset(xoff, yoff)
            vXOff = offPoint.x
            vYOff = offPoint.y
        }
        super.showAsDropDown(anchor, vXOff, vYOff, gravity)
    }

    protected open fun getPopWidth(): Int {
        return ViewGroup.LayoutParams.MATCH_PARENT
    }

    /**
     * 计算PopupWindow的高度
     */
    private fun calculateHeight(anchor: View): Int {
        val viewRect = Rect()
        anchor.getGlobalVisibleRect(viewRect)
        val screenRealSize = Point()
        activity.getDisplayCompat()?.getRealSize(screenRealSize)
        val heightOffset = screenRealSize.y - screenHeight - activity.getNavigationBarHeight()
        return screenHeight - viewRect.bottom + heightOffset
    }

    private fun getXYOffset(x: Int, y: Int): Point {
        val outLocation = Point()
        outLocation.y = y
        val mConfiguration: Configuration = activity.resources.configuration
        val ori = mConfiguration.orientation
        if (ori == Configuration.ORIENTATION_LANDSCAPE && activity.hasNavigationBar()) {
            outLocation.x = x + activity.getNavigationBarHeight()
        } else {
            outLocation.x = x
        }
        return outLocation
    }
}