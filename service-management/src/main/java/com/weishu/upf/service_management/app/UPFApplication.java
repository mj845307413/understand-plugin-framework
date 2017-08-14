package com.weishu.upf.service_management.app;

import java.io.File;

import android.app.Application;
import android.content.Context;

import com.weishu.upf.service_management.app.hook.AMSHookHelper;
import com.weishu.upf.service_management.app.hook.BaseDexClassLoaderHookHelper;

/**
 * 这个类只是为了方便获取全局Context的.
 *
 * @author weishu
 * @date 16/3/29
 * 在这里之所以一定要使用一个代理的service，而不能直接像一个对象一样创建一个service，因为我们要实现service的特殊地位
 * 需要service在后台运行，不同的对象显然无法达到这个需求，因此我们需要一个傀儡service来完成这一个工作。
 *
 *
 *
 * 之所以不用插件化Activity的方式加载Service，是因为Service的数量是可以有很多的，无法做到提前声明一定数量的Service。
 * 而用一个stubService来启动不同的插件Service时，由于当stubService一旦启动后，不会调用create相应Service的方法，只会调用startCommand的方法，无法启动不同的Service。
 * 所以这边使用Activity插件化的方法是无法启动Service的。
 *
 *
 * 在这里不同于调用插件Activity，插件activity是直接将插件的classloader调用进来（loadedApk），而我们启动插件的Service还是用的主App的classloader，用ActivityThread
 * 中的Service创建方法手动创建插件Service。
 *
 * 在这里创建service
 * 1、将目标targetservice转换为stubService，这个步骤比较简单，跟以前差不多，
 * 首先需要hook掉ActivityManagerNative里面的IActivityManager
 * 将里面的startService和stopService分别进行处理
 * startService的话要注意将启动对象转换为stubService
 * stopService的话需要将通过遍历Service，将对应的Service给destroy掉，如果Service为0的话，将stubService也给destroy掉
 *
 * 2、需要将插件的插件包引入到app的缓存中（这个步骤跟之前的Activity的加载插件的过程的差不多）
 *
 * （Activity中是通过生成loadedApk，来进行activity管理的，我们这边使用的是保守方案）
 * 3、解析插件中的Service组件，将manifest中的Service信息给加载到serviceManger的mServiceInfoMap中统一管理（
 * 将运行的Service放入ServiceMap中
 *
 * 4、
 */
public class UPFApplication extends Application {

    private static Context sContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sContext = base;

        try {
            // 拦截startService, stopService等操作
            AMSHookHelper.hookActivityManagerNative();
            Utils.extractAssets(base, "test.jar");
            File apkFile = getFileStreamPath("test.jar");
            File odexFile = getFileStreamPath("test.odex");

            // Hook ClassLoader, 让插件中的类能够被成功加载
            BaseDexClassLoaderHookHelper.patchClassLoader(getClassLoader(), apkFile, odexFile);
            // 解析插件中的Service组件
            ServiceManager.getInstance().preLoadServices(apkFile);
        } catch (Exception e) {
            throw new RuntimeException("hook failed");
        }
    }

    public static Context getContext() {
        return sContext;
    }
}
