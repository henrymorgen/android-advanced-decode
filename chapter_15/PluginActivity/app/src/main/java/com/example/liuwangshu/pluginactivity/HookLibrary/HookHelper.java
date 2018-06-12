package com.example.liuwangshu.pluginactivity.HookLibrary;


import android.app.Instrumentation;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class HookHelper {
    public static final String TARGET_INTENT = "target_intent";
    public static final String TARGET_INTENT_NAME = "target_intent_name";
    public static void hookAMS() throws Exception {
        Object defaultSingleton=null;
        if (Build.VERSION.SDK_INT >= 26) {
            Class<?> activityManagerClazz = Class.forName("android.app.ActivityManager");
            defaultSingleton=  FieldUtil.getField(activityManagerClazz,null,"IActivityManagerSingleton");
        } else {
            Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
            defaultSingleton=  FieldUtil.getField(activityManagerNativeClass,null,"gDefault");
        }
        Class<?> singletonClazz = Class.forName("android.util.Singleton");
        Field mInstanceField= FieldUtil.getField(singletonClazz,"mInstance");
        Object iActivityManager = mInstanceField.get(defaultSingleton);
        Class<?> iActivityManagerClass = Class.forName("android.app.IActivityManager");
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[] { iActivityManagerClass }, new IActivityManagerProxy(iActivityManager));
        mInstanceField.set(defaultSingleton, proxy);
    }
    public static void hookHandler() throws Exception {
        Class<?> activityThreadClazz = Class.forName("android.app.ActivityThread");
        Object currentActivityThread= FieldUtil.getField(activityThreadClazz,null,"sCurrentActivityThread");
        Field mHField = FieldUtil.getField(activityThreadClazz,"mH");
        Handler mH = (Handler) mHField.get(currentActivityThread);
        FieldUtil.setField(Handler.class,mH,"mCallback",new HCallback(mH));
    }
    public static void hookInstrumentation(Context context) throws Exception {
        Class<?> contextImplClazz = Class.forName("android.app.ContextImpl");
        Field mMainThreadField  =FieldUtil.getField(contextImplClazz,"mMainThread");
        Object activityThread = mMainThreadField.get(context);
        Class<?> activityThreadClazz = Class.forName("android.app.ActivityThread");
        Field mInstrumentationField=FieldUtil.getField(activityThreadClazz,"mInstrumentation");
        FieldUtil.setField(activityThreadClazz,activityThread,"mInstrumentation",new InstrumentationProxy((Instrumentation) mInstrumentationField.get(activityThread),
                context.getPackageManager()));
    }
}
