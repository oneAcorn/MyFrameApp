package com.acorn.myframeapp.ui.webview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import kotlinx.android.synthetic.main.activity_x5_webview.*

/**
 * Created by acorn on 2022/5/29.
 */
class X5WebviewActivity : BaseNoViewModelActivity(), X5WebviewFragment.IWebviewCallback {
    private lateinit var fragment: X5WebviewFragment

    companion object {
        fun open(activity: Activity, url: String, title: String?, isFixedTitle: Boolean = false) {
            val intent = Intent(activity, X5WebviewActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("title", title)
            intent.putExtra("isFixedTitle", isFixedTitle)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_x5_webview)
    }

    override fun initView() {
        super.initView()

        val url = intent.getStringExtra("url")
        val title = intent.getStringExtra("title")
        val isFixedTitle = intent.getBooleanExtra("isFixedTitle", false)
        fragment = X5WebviewFragment.newInstance(url, title, isFixedTitle, this)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        fragment.onBackPress()
    }

    override fun onTitleChange(title: String?) {
        showToolbar { it.title = title }
    }

    override fun onChooseFile(isVideo: Boolean, isByCamera: Boolean) {

    }

    override fun isRootUrl(url: String?): Boolean {
        return false
    }
}