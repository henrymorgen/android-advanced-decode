package com.example.liuwangshu.pluginactivity.HookLibrary;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2018/3/29 0029.
 */

public class HCallback implements Handler.Callback{
    public static final int LAUNCH_ACTIVITY = 100;
    Handler mHandler;

    public HCallback(Handler handler) {
        mHandler = handler;
    }
    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == LAUNCH_ACTIVITY) {
            Object r = msg.obj;
            try {
                Intent intent = (Intent) FieldUtil.getField(r.getClass(), r, "intent");
                Intent target = intent.getParcelableExtra(HookHelper.TARGET_INTENT);
                intent.setComponent(target.getComponent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mHandler.handleMessage(msg);
        return true;
    }
}
