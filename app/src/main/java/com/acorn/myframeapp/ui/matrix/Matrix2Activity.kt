package com.acorn.myframeapp.ui.matrix

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import com.acorn.basemodule.extendfun.getDrawableCompat
import com.acorn.myframeapp.R
import com.acorn.myframeapp.demo.BaseNoViewModelDemoActivity
import com.acorn.myframeapp.demo.Demo
import kotlinx.android.synthetic.main.activity_matrix2.iv

/**
 * Created by acorn on 2024/9/14.
 */
class Matrix2Activity : BaseNoViewModelDemoActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matrix2)
        val drawable = getDrawableCompat(R.drawable.baseline_build_black_48)
        val img = (drawable as? BitmapDrawable)?.bitmap
        if (img != null)
            iv.setImage(img)
    }

    override fun getItems(): Array<Demo> {
        return arrayOf(Demo("sdff"))
    }

    override fun onItemClick(data: Demo, idOrPosition: Int) {

    }
}