package com.acorn.basemodule.extendfun

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.acorn.basemodule.R
import com.acorn.basemodule.base.BaseApplication
import com.acorn.basemodule.utils.CommonCaches
import com.acorn.basemodule.utils.systembar.*

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

fun showToastAndLog(str: String) {
    showToast(str)
    logI(str)
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

//region 获取状态栏,导航栏相关属性
//参考: https://github.com/gyf-dev/ImmersionBar
fun Activity.hasNavigationBar(): Boolean {
    val barConfig = BarConfig(this)
    return barConfig.hasNavigationBar()
}

fun Fragment.hasNavigationBar(): Boolean {
    return requireActivity().hasNavigationBar()
}

fun Context.hasNavigationBar(): Boolean {
    return getNavigationBarHeight() > 0
}

fun Activity.getNavigationBarHeight(): Int {
    return BarConfig(this).navigationBarHeight
}

fun Fragment.getNavigationBarHeight(): Int {
    return activity?.getNavigationBarHeight() ?: 0
}

fun Context.getNavigationBarHeight(): Int {
    val bean: GestureUtils.GestureBean = GestureUtils.getGestureBean(this)
    return if (bean.isGesture && !bean.checkNavigation) {
        0
    } else {
        BarConfig.getNavigationBarHeightInternal(this)
    }
}

fun Activity.getNavigationBarWidth(): Int {
    return BarConfig(this).navigationBarWidth
}

fun Fragment.getNavigationBarWidth(): Int {
    return activity?.getNavigationBarWidth() ?: 0
}

fun Context.getNavigationBarWidth(): Int {
    val bean: GestureUtils.GestureBean = GestureUtils.getGestureBean(this)
    return if (bean.isGesture && !bean.checkNavigation) {
        0
    } else {
        BarConfig.getNavigationBarWidthInternal(this)
    }
}

fun Activity.isNavigationAtBottom(): Boolean {
    return BarConfig(this).isNavigationAtBottom
}

fun Fragment.isNavigationAtBottom(): Boolean {
    return activity?.isNavigationAtBottom() ?: false
}

fun Activity.getStatusBarHeight(): Int {
    return BarConfig(this).statusBarHeight
}

fun Fragment.getStatusBarHeight(): Int = activity?.getStatusBarHeight() ?: 0

fun Context.getStatusBarHeight(): Int =
    BarConfig.getInternalDimensionSize(this, SystemBarConstants.IMMERSION_STATUS_BAR_HEIGHT)

fun Activity.getActionBarHeight(): Int = BarConfig(this).actionBarHeight

fun Fragment.getActionBarHeight(): Int = activity?.getActionBarHeight() ?: 0

/**
 * 是否为刘海屏
 */
fun Activity.hasNotchScreen(): Boolean = NotchUtils.hasNotchScreen(this)

/**
 * 是否为刘海屏
 */
fun Fragment.hasNotchScreen(): Boolean = activity?.hasNotchScreen() ?: false

/**
 * 是否为刘海屏
 */
fun View.hasNotchScreen(): Boolean = NotchUtils.hasNotchScreen(this)

/**
 * 刘海屏高度
 */
fun Activity.getNotchHeight(): Int = NotchUtils.getNotchHeight(this)

/**
 * 刘海屏高度
 */
fun Fragment.getNotchHeight(): Int = activity?.getNotchHeight() ?: 0

/**
 * 刘海屏高度
 */
fun Activity.getNotchHeight(callback: NotchCallback) {
    NotchUtils.getNotchHeight(this, callback)
}

/**
 * 刘海屏高度
 */
fun Fragment.getNotchHeight(callback: NotchCallback) {
    activity?.getNotchHeight(callback)
}

fun Window.hideStatusBar() {
    setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}

/**
 * 显示状态栏
 * Show status bar.
 *
 * @param window the window
 */
fun Window.showStatusBar(window: Window) {
    clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}
//endregion

fun Context.isPortraitMode(): Boolean {
    val orientation: Int = resources.configuration.orientation
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        return false
    }
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        return true
    }
    return false
}