package com.gankaowangxiao.xzl;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gankaowangxiao.xzl.base.BaseActivity;
import com.tencent.bugly.beta.Beta;

import java.lang.ref.WeakReference;

/**
 * Created by xiaotantan on 17/10/11.
 */

public class LaunchActivity extends BaseActivity {
    private static MyHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        handler = new MyHandler(this);
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    public static class MyHandler extends Handler {
        private final WeakReference<LaunchActivity> mhandleActivity;

        public MyHandler(LaunchActivity activity) {
            mhandleActivity = new WeakReference<LaunchActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mhandleActivity.get() == null) {
                return;
            }
            mhandleActivity.get().launchHome();
        }
    }

    public void launchHome() {
        startActivity(new Intent(this, WebActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
