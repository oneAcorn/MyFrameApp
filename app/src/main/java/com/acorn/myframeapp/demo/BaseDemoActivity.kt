package com.acorn.myframeapp.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.acorn.myframeapp.R
import com.acorn.myframeapp.base.BaseNoViewModelActivity
import kotlinx.android.synthetic.main.layout_demo.*

/**
 * Created by acorn on 2022/5/19.
 */
abstract class BaseDemoActivity : BaseNoViewModelActivity() {
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
        setContentView(R.layout.layout_demo)
        rv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@BaseDemoActivity)
            adapter = DemoAdapter(this@BaseDemoActivity, getItems()).apply {
                onItemClickListener = this@BaseDemoActivity.onItemClickListener
            }
        }
    }

    abstract fun getItems(): Array<Demo>

    abstract fun onItemClick(data: Demo, idOrPosition: Int)
}