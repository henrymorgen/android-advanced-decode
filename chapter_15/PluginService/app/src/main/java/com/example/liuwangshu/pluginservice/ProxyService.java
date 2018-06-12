package com.example.liuwangshu.pluginservice;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;


import com.example.liuwangshu.pluginservice.HookLibrary.FieldUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by Administrator on 2018/4/16 0016.
 */

public class ProxyService extends Service {
    public static final String TARGET_SERVICE = "target_service";
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent || !intent.hasExtra(TARGET_SERVICE)) {
            return START_STICKY;
        }
        String serviceName = intent.getStringExtra(TARGET_SERVICE);
        if (null == serviceName) {
            return START_STICKY;
        }
        Service targetService=null;
        try {
            Class activityThreadClazz = Class.forName("android.app.ActivityThread");
            Method getActivityThreadMethod = activityThreadClazz.getDeclaredMethod("getApplicationThread");
            getActivityThreadMethod.setAccessible(true);
            Object activityThread = FieldUtil.getField(activityThreadClazz, null, "sCurrentActivityThread");
            Object applicationThread = getActivityThreadMethod.invoke(activityThread);
            Class iInterfaceClazz = Class.forName("android.os.IInterface");
            Method asBinderMethod = iInterfaceClazz.getDeclaredMethod("asBinder");
            asBinderMethod.setAccessible(true);
            Object token = asBinderMethod.invoke(applicationThread);
            Class serviceClazz = Class.forName("android.app.Service");
            Method attachMethod = serviceClazz.getDeclaredMethod("attach",
                    Context.class, activityThreadClazz, String.class, IBinder.class, Application.class, Object.class);
            attachMethod.setAccessible(true);
            Object defaultSingleton=null;
            if (Build.VERSION.SDK_INT >= 26) {
                Class<?> activityManageClazz = Class.forName("android.app.ActivityManager");
                //获取activityManager中的IActivityManagerSingleton字段
                defaultSingleton = FieldUtil.getField(activityManageClazz, null, "IActivityManagerSingleton");
            } else {
                Class<?> activityManagerNativeClazz = Class.forName("android.app.ActivityManagerNative");
                //获取ActivityManagerNative中的gDefault字段
                defaultSingleton = FieldUtil.getField(activityManagerNativeClazz, null, "gDefault");
            }
            Class<?> singletonClazz = Class.forName("android.util.Singleton");
            Field mInstanceField = FieldUtil.getField(singletonClazz, "mInstance");
            //获取iActivityManager
            Object iActivityManager = mInstanceField.get(defaultSingleton);
            targetService = (Service) Class.forName(serviceName).newInstance();
            attachMethod.invoke(targetService, this, activityThread, intent.getComponent().getClassName(), token,
                    getApplication(), iActivityManager);
            targetService.onCreate();
        } catch (Exception e) {
            e.printStackTrace();
            return START_STICKY;
        }
        targetService.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
}



