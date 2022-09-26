package com.acorn.basemodule.extendfun

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.*
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.acorn.basemodule.R
import com.acorn.basemodule.base.BaseApplication
import com.acorn.basemodule.utils.CommonCaches

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

private fun getDisplayApiL(context: Context): Display? {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return wm.defaultDisplay
}

@RequiresApi(api = Build.VERSION_CODES.R)
private fun getDisplayApiR(context: Context): Display? {
    return context.display
}

fun Context.showToast(@StringRes resId: Int) {
    //使用appContext无法获取到正确的语言
//    val str = appContext.getString(resId)
    val str = getString(resId)
    showToast(str)
}

fun showToast(str: String) {
    Toast.makeText(appContext, null, Toast.LENGTH_SHORT).apply {
        //在这里setText，防止MIUI toast时带上app名称
        setText(str)
        setGravity(Gravity.CENTER, 0, 0)
    }.show()
}

fun Context.getCurrentLanguage(): String {
    val curLanguage = CommonCaches.currentLanguage
    if (curLanguage != null) {
        return curLanguage
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales[0].language
    } else {
        resources.configuration.locale.language
    }
}

//是否为中文
fun Context.isLanguageChinese(): Boolean {
    return getCurrentLanguage() == "zh"
}

fun Activity.startActivityWithAnim(
    intent: Intent,
    enterAnim: Int = R.anim.enter_from_right,
    exitAnim: Int = R.anim.exit_to_right
) {
    startActivity(intent)
    overridePendingTransition(enterAnim, exitAnim)
}

fun Context.getVersionCodeCompat(): Long {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageManager.getPackageInfo(packageName, 0).longVersionCode
    } else {
        packageManager.getPackageInfo(packageName, 0).versionCode.toLong()
    }
}