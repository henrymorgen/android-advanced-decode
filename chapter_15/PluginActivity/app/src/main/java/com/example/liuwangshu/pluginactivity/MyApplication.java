package com.example.liuwangshu.pluginactivity;

import android.app.Application;
import android.content.Context;

import com.example.liuwangshu.pluginactivity.HookLibrary.HookHelper;

/**
 * Created by Administrator on 2018/4/12 0012.
 */

public class MyApplication extends Application {
    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
              //Hook AMS
            HookHelper.hookAMS();
            HookHelper.hookHandler();

            //Hook Instrumentation
//          HookHelper.hookInstrumentation(base);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
