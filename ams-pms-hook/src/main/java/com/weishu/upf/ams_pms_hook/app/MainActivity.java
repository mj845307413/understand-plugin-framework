package com.weishu.upf.ams_pms_hook.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author weishu
 * @date 16/3/7
 */
public class MainActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getPackageManager();
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
    }

    // 这个方法比onCreate调用早; 在这里Hook比较好.
    @Override
    protected void attachBaseContext(Context newBase) {
        HookHelper.hookActivityManager();
        HookHelper.hookPackageManager(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:

                // 测试AMS HOOK (调用其相关方法)
                Uri uri = Uri.parse("http://wwww.baidu.com");
                Intent t = new Intent(Intent.ACTION_VIEW);
                t.setData(uri);
                startActivity(t);
                break;
            case R.id.btn2:
                // 测试PMS HOOK (调用其相关方法)
                getPackageManager().getInstalledApplications(0);
                break;
        }
    }

    //通过token来调用相应的Activity，所以在activityManagerService的token是subactivity，而在本进程中token对应的是targetactivity。
    //具体的生命周期相关，可以看相关的文档。
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("majun_log", "onresume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("majun_log", "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("majun_log", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("majun_log", "onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("majun_log", "onDestroy");
    }
}
