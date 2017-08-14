package com.example.mytest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by majun on 17/8/10.
 */

public class MyBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "我是插件收到广播", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent("com.weishu.upf.demo.app2.PLUGIN_ACTION");
        context.sendBroadcast(myIntent);
    }
}
