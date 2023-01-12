package com.acorn.myframeapp.ui.reflect

import com.acorn.basemodule.extendfun.getGenericityClass
import com.acorn.basemodule.extendfun.reflectFun
import com.acorn.basemodule.extendfun.reflectStaticFun
import com.acorn.basemodule.utils.UnicodeUtils
import com.acorn.myframeapp.ui.test.Cat
import com.acorn.myframeapp.demo.BaseDemoActivity
import com.acorn.myframeapp.demo.Demo
import com.acorn.myframeapp.ui.test.Dog

/**
 *
 * 获取Class对象的三种方式
 * //一 通过forName获取对象是无法确定类型的,所以使用无限制泛型通配符Class<*>(#kotlin)/Class<?>(#java)
val user_class: Class<*> = Class.forName("com.acorn.testanything.reflection.User")
(user_class.newInstance() as? User).let {
it?.name = "wang"
println(it?.name)
}

//二 通过已知类型获取对象(userTwo.javaClass(#kotlin)/userTwo.getClass(#java)),
// 由于对象类型已知所以这个Class是User或其子类(Class<out User>(#kotlin)/Class<? extends User>(#java))
val userTwo = User()
val user_class_two: Class<out User> = userTwo.javaClass
println("同一个类只有一个Class对象,所以user_class === user_class_two=${user_class === user_class_two}")

//三 直接根据类型获取Class对象(User.class(#java)/User::class.java(#kotlin)),这种方式最直接,效率最高
val user_class_three:Class<User> = User::class.java
println("同一个类只有一个Class对象,所以user_class === user_class_three=${user_class === user_class_three}")
 * Created by acorn on 2023/1/12.
 */
class ReflectActivity : BaseGenerictReflectActivity<Dog, Cat>() {

    companion object {
        private const val CLICK_STATIC_FUN = 0
        private const val CLICK_NORMAL_FUN = 1
        private const val CLICK_KOTLIN_OBJECT_FUN = 2
        private const val CLICK_GET_CLASS_FROM_GENERICITY = 3
    }

    override fun getItems(): Array<Demo> {
        return arrayOf(
            Demo("static fun", CLICK_STATIC_FUN),
            Demo("normal fun", CLICK_NORMAL_FUN),
            Demo(
                "kotlin object class fun",
                CLICK_KOTLIN_OBJECT_FUN,
                description = "这个会崩溃,暂时不知道怎么解决"
            ),
            Demo("get class from genericity", CLICK_GET_CLASS_FROM_GENERICITY)
        )
    }

    override fun onItemClick(data: Demo, idOrPosition: Int) {
        when (idOrPosition) {
            CLICK_STATIC_FUN -> {
                invokeStaticFunc()
            }
            CLICK_NORMAL_FUN -> {
                invokeNormalFunc()
            }
            CLICK_KOTLIN_OBJECT_FUN -> {
                invokeKotlinObjectFun()
            }
            CLICK_GET_CLASS_FROM_GENERICITY -> {
                invokeGetClassFromGenericity()
            }
        }
    }

    private fun invokeStaticFunc() {
        val res = Dog::class.java.reflectStaticFun(
            "bark",
            arrayOf(String::class.java),
            "张三"
        ) as String
        showTip(res)
    }

    private fun invokeNormalFunc() {
        val res = Cat::class.java.reflectFun(
            "bark",
            arrayOf(String::class.java, Int::class.java),
            "老王",
            33
        ) as String
        showTip(res)
    }

    private fun invokeKotlinObjectFun() {
        //TODO kotlin的Object XXX{} 有问题,会空指针
        val res = UnicodeUtils::class.java.reflectStaticFun(
            "encode",
            arrayOf(String::class.java),
            "老王"
        ) as String
        showTip(res)
    }

    private fun invokeGetClassFromGenericity() {
        val res = getGenericityClass(1).reflectFun(
            "bark",
            arrayOf(String::class.java, Int::class.java),
            "老王",
            33
        ) as String
        showTip(res)
    }
}