package com.acorn.myframeapp.ui.annotation

import android.app.Activity
import android.view.View
import java.lang.reflect.Field

/**
 * Created by acorn on 2022/6/10.
 */
object BindViewInjectUtils {
    fun inject(activity: Activity) {
        val fields: Array<Field> = activity::class.java.declaredFields
        for (field in fields) {
            //允许访问私有变量
            field.isAccessible = true
            val bindView: MyBindView? = field.getAnnotation(
                MyBindView::class.java)
            if (bindView != null) {
                val value: Int = bindView.value
                val view: View = activity.findViewById(value)
                try {
                    field.set(activity, view)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }
    }
}