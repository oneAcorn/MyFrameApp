package com.acorn.basemodule.extendfun

import java.lang.IllegalArgumentException
import java.lang.reflect.ParameterizedType

/**
 * Created by acorn on 2023/1/12.
 */

/**
 * 获取泛型的Class,比如Cat<Animal,Evil>类,
 * 当index=0 retun Animal::class.java,index=1 return Evil::class.java
 */
fun Any.getGenericityClass(index: Int = 0): Class<*> {
    if (index < 0) throw IllegalArgumentException("index must more than 0")
    //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
    val type = this.javaClass.genericSuperclass as ParameterizedType
    //返回表示此类型实际类型参数的 Type 对象的数组(),赋值给this.classt
    return type.actualTypeArguments[index] as Class<*> //<T>
}

fun Class<*>.reflectStaticFun(
    funcName: String,
    paramsTypes: Array<out Class<*>>,
    vararg params: Any?
): Any? {
    val method = getDeclaredMethod(funcName, *paramsTypes)
    //第一个参数是类对象,但这个是静态方法,所以不需要传.
    return method.invoke(null, *params)
}

fun Class<*>.reflectFun(
    funcName: String,
    paramsTypes: Array<out Class<*>>,
    vararg params: Any?
): Any? {
    //newInstance只能调用无参构造方法
    val mInstance = this.newInstance()
    val method = getMethod(funcName, *paramsTypes)
    //第一个参数是类对象
    return method.invoke(mInstance, *params)
}