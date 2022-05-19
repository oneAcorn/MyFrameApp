package com.acorn.basemodule.network

/**
 * Created by acorn on 2019-08-21.
 */
interface IResponse {
    fun isSuccess(): Boolean

    fun failMessage(): String?

    fun code(): Int

    /**
     * token 失效
     */
    fun isTokenExpired(): Boolean
}