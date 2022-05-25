package com.acorn.basemodule.extendfun

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Display
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
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

fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resId)
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

fun showToast(str: String) {
    Toast.makeText(appContext, null, Toast.LENGTH_SHORT).apply {
        //在这里setText，防止MIUI toast时带上app名称
        setText(str)
        setGravity(Gravity.CENTER, 0, 0)
    }.show()
}

private fun getDisplayApiL(context: Context): Display? {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return wm.defaultDisplay
}

@RequiresApi(api = Build.VERSION_CODES.R)
private fun getDisplayApiR(context: Context): Display? {
    return context.display
}