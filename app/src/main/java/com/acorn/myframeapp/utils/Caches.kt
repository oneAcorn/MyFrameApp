package com.acorn.myframeapp.utils

import com.acorn.basemodule.extendfun.string
import com.tencent.mmkv.MMKV

/**
 * Created by acorn on 2022/6/27.
 */
object Caches {
    private val mmkv = MMKV.mmkvWithID("myFrameApp", MMKV.MULTI_PROCESS_MODE)

    var currentLanguage: String? by mmkv.string()
}