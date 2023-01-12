package com.acorn.basemodule.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.acorn.basemodule.extendfun.getGenericityClass
import com.acorn.basemodule.extendfun.reflectStaticFun
import com.acorn.basemodule.network.BaseNetViewModel

/**
 * Created by acorn on 2023/1/11.
 */
abstract class BaseBindingActivity<T : BaseNetViewModel, U : ViewBinding> : BaseActivity<T>() {

    protected lateinit var binding: U

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createBinding()
        setContentView(binding.root)
    }

    private fun createBinding(): U {
        return getGenericityClass(1).reflectStaticFun(
            "inflate",
            arrayOf(LayoutInflater::class.java), layoutInflater
        ) as U
    }
}