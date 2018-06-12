package com.example.liuwangshu.pluginservice.HookLibrary;


import android.app.Instrumentation;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.example.liuwangshu.pluginservice.IActivityManagerProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class HookHelper {

    public static void hookAMS() throws Exception {
        Object defaultSingleton=null;
        if (Build.VERSION.SDK_INT >= 26) {
            Class<?> activityManagerClass = Class.forName("android.app.ActivityManager");
            defaultSingleton=  FieldUtil.getField(activityManagerClass,null,"IActivityManagerSingleton");
        } else {
            Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
            defaultSingleton=  FieldUtil.getField(activityManagerNativeClass,null,"gDefault");
        }
        Class<?> singletonClass = Class.forName("android.util.Singleton");
        Field mInstanceField= FieldUtil.getField(singletonClass,"mInstance");
        Object iActivityManager = mInstanceField.get(defaultSingleton);
        Class<?> iActivityManagerClass = Class.forName("android.app.IActivityManager");
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[] { iActivityManagerClass }, new IActivityManagerProxy(iActivityManager));
        mInstanceField.set(defaultSingleton, proxy);
    }
}
