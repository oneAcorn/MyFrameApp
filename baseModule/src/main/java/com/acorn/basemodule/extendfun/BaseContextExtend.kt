package com.acorn.basemodule.extendfun

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.Display
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.acorn.basemodule.base.BaseApplication

/**
 * Created by acorn on 2022/5/18.
 */

val appContext get() = BaseApplication.appContext

fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

/**
 * 兼容高版本getDisplay
 */
fun Context.getDisplayCompat(): Display? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        getDisplayApiR(this)
    } else {
        getDisplayApiL(this)
    }
}

private fun getDisplayApiL(context: Context): Display? {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return wm.defaultDisplay
}

@RequiresApi(api = Build.VERSION_CODES.R)
private fun getDisplayApiR(context: Context): Display? {
    return context.display
}