package com.acorn.myframeapp.bean

import com.acorn.basemodule.network.IResponse

/**
 * Created by acorn on 2022/5/19.
 */
class BaseResponse<T : Any> : IResponse {
    var data: T? = null
    var code: Int = 0
    var msg: String? = null

    override fun isSuccess(): Boolean {
        return code == 0
    }

    override fun failMessage(): String? {
        return msg
    }

    override fun code(): Int {
        return code
    }

    override fun isTokenExpired(): Boolean {
        return code == -2
    }
}