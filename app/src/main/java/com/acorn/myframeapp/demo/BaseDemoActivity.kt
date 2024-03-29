package com.acorn.myframeapp.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.basemodule.base.BaseActivity
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import kotlinx.android.synthetic.main.layout_demo.*

/**
 * Created by acorn on 2022/5/19.
 */
abstract class BaseDemoActivity<T : BaseNetViewModel> : BaseActivity<T>() {
    private val onItemClickListener: (data: Demo, position: Int) -> Unit =
        { data, position ->
            if (data.activity != null) {
                val intent = Intent(this, data.activity)
                if (null != data.bundle) {
                    intent.putExtras(data.bundle)
                }
                startActivity(intent)
            } else {
                onItemClick(data, position)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.layout_demo)
        rv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@BaseDemoActivity)
            adapter = DemoAdapter(this@BaseDemoActivity, getItems()).apply {
                onItemClickListener = this@BaseDemoActivity.onItemClickListener
            }
        }
    }

    override fun setContentView(layoutResID: Int) {
        layoutInflater.inflate(layoutResID, container, true)
        container.visibility = View.VISIBLE
    }

    abstract fun getItems(): Array<Demo>

    abstract fun onItemClick(data: Demo, idOrPosition: Int)
}