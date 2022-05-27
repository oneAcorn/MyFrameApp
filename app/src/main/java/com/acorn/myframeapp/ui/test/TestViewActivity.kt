package com.acorn.myframeapp.ui.test

import android.os.Bundle
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity

/**
 * Created by acorn on 2022/5/26.
 */
class TestViewActivity:BaseNoViewModelActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_view)
    }
}