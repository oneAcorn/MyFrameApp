package com.acorn.myframeapp.ui.internationalization

import android.content.Context
import android.os.Build

/**
 * Created by acorn on 2022/6/27.
 */

fun Context.currentLanguage(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales[0].language
    } else {
        resources.configuration.locale.language
    }
}