package com.example.liuwangshu.pluginservice;

import android.app.Application;
import android.content.Context;

import com.example.liuwangshu.pluginservice.HookLibrary.HookHelper;

/**
 * Created by Administrator on 2018/4/19 0019.
 */

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            HookHelper.hookAMS();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
