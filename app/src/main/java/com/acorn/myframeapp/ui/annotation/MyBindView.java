package com.acorn.myframeapp.ui.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.annotation.IdRes;

/**
 * Created by acorn on 2022/6/10.
 */
//表示注解是否出现在 javadoc中，缺省则表示不出现在javadoc中。
@Documented
//指定Annotation的类型，如缺失@Targetz则Annotation可用于任何地方，否则用于指定的地方。
@Target(ElementType.FIELD)
//指定Annotation的策略。
@Retention(RetentionPolicy.RUNTIME)
public @interface MyBindView {
    //定义接收的参数
    @IdRes int value();
}
