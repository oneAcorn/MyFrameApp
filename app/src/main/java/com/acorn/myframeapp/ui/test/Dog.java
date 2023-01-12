package com.acorn.myframeapp.ui.test;

import com.acorn.basemodule.extendfun.BaseContextExtendKt;
import com.luck.picture.lib.utils.ToastUtils;

/**
 * Created by acorn on 2023/1/12.
 */
public class Dog {
    public static String bark(String name) {
        BaseContextExtendKt.showToast(name + " bark");
        return "bark bark";
    }
}
