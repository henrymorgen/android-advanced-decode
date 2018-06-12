package com.example.liuwangshu.pluginservice;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import com.example.liuwangshu.pluginservice.HookLibrary.HookHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class IActivityManagerProxy implements InvocationHandler {
    private Object mActivityManager;
    private static final String TAG = "IActivityManagerProxy";

    public IActivityManagerProxy(Object activityManager) {
        this.mActivityManager = activityManager;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if ("startService".equals(method.getName())) {
            Intent intent = null;
            int index = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            intent = (Intent) args[index];
            Intent proxyIntent = new Intent();
            String packageName = "com.example.liuwangshu.pluginservice";
            proxyIntent.setClassName(packageName, packageName + ".ProxyService");
            proxyIntent.putExtra(ProxyService.TARGET_SERVICE, intent.getComponent().getClassName());
            args[index] = proxyIntent;
            Log.d(TAG, "Hook成功");
        }
        return method.invoke(mActivityManager, args);
    }
}