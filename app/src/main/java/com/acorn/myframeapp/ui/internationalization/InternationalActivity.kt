package com.acorn.myframeapp.ui.internationalization

import android.os.Bundle
import com.acorn.basemodule.extendfun.singleClick
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import com.acorn.basemodule.utils.CommonCaches
import kotlinx.android.synthetic.main.activity_international.*

/**
 * 国际化
 * 使用MyContextWrapper在BaseActivity中实现.
 * Created by acorn on 2022/6/27.
 */
class InternationalActivity : BaseNoViewModelActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_international)
    }

    override fun initView() {
        super.initView()
        showToolbar { it.title = getString(R.string.internationalization_title) }
        internationalBtn.singleClick {
            if (currentLanguage() == "en") {
                CommonCaches.currentLanguage = "zh"
            } else {
                CommonCaches.currentLanguage = "en"
            }
            finish()
        }
    }
}