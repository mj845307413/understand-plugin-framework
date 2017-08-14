package com.weishu.upf.dynamic_proxy_hook.app2.hook;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.app.Instrumentation;
import android.text.TextUtils;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by majun on 17/7/29.
 */

public class MyInvocationHandler implements InvocationHandler {

    Instrumentation mBase;

    public MyInvocationHandler(Instrumentation instrumentation) {
        mBase = instrumentation;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (TextUtils.equals(method.getName(), "execStartActivity")) {
            Log.i("EvilInstrumentation", "EvilInstrumentation");
        }
        return method.invoke(mBase, args);
    }
}
