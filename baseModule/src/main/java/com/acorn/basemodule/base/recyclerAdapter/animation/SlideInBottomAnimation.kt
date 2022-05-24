package com.acorn.basemodule.base.recyclerAdapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.acorn.basemodule.base.recyclerAdapter.animation.BaseAnimation

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
class SlideInBottomAnimation : BaseAnimation {
    override fun animators(view: View): Array<Animator> {
        val animator = ObjectAnimator.ofFloat(view, "translationY", view.measuredHeight.toFloat(), 0f)
        animator.duration = 400L
        animator.interpolator = DecelerateInterpolator(1.3f)
        return arrayOf(animator)
    }
}