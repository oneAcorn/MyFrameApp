package com.acorn.myframeapp.ui.webview;

import android.content.Context;
import android.webkit.JavascriptInterface;


public class MyAndroidJs {
    private Context mContext;
    private CallBack mCallBack;

    public MyAndroidJs(Context context, CallBack callBack) {
        this.mContext = context;
        this.mCallBack = callBack;
    }

    @JavascriptInterface
    public void anyAndroidFunc() {
        if (mCallBack != null) {
            mCallBack.anyAndroidFunc();
        }
    }

    @JavascriptInterface
    public void anyAndroidFunc2() {
        if (mCallBack != null) {
            mCallBack.anyAndroidFunc2();
        }
    }

    public interface CallBack {
        void anyAndroidFunc();
        void anyAndroidFunc2();
    }
}
