package com.risun.jg.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by whd on 2018/1/10.
 */

public class IntentService2 extends IntentService {
    private String TAG="IntentService2";
    public IntentService2() {
        super("IntentService2");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG,"onHandleIntent");
        String name=intent.getStringExtra("name");
        Log.d(TAG,name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}
