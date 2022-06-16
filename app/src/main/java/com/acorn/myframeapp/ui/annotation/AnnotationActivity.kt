package com.acorn.myframeapp.ui.annotation

import android.os.Bundle
import android.widget.TextView
import butterknife.BindView
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity

/**
 * Created by acorn on 2022/6/10.
 */
class AnnotationActivity : BaseNoViewModelActivity() {
    @MyBindView(R.id.annotationTv)
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annotation)
        BindViewInjectUtils.inject(this)
        textView?.text = "我是通过注解和反射找到的view"
    }
}