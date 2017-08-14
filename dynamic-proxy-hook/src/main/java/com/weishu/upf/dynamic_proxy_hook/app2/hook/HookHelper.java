package com.weishu.upf.dynamic_proxy_hook.app2.hook;

import android.app.Instrumentation;
import android.os.IBinder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author weishu
 * @date 16/1/28
 */
public class HookHelper {

    public static void attachContext() throws Exception {
        // 先获取到当前的ActivityThread对象
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        //currentActivityThread是一个static函数所以可以直接invoke，不需要带实例参数
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);

        // 拿到原始的 mInstrumentation字段
        Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);

        // 创建代理对象
        Instrumentation evilInstrumentation = new EvilInstrumentation(mInstrumentation);

        // 偷梁换柱
        mInstrumentationField.set(currentActivityThread, evilInstrumentation);
        //使用这个代理方法的话，new class必须要是interface
//        Instrumentation instrumentation = (Instrumentation) Proxy
//                .newProxyInstance(activityThreadClass.getClassLoader(), new Class<?>[]{Instrumentation.class}, new
//                        MyInvocationHandler(mInstrumentation));
//        mInstrumentationField.set(currentActivityThread, instrumentation);

    }
}
