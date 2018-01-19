package com.risun.jg.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by whd on 2018/1/10.
 */

public class HandlerService extends Service {

    private String TAG="HandlerService";
    private Looper looper;
    private class ServiceHandler extends Handler{
        public ServiceHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG,"ServiceHandler");
            stopSelf(msg.arg1);
        }
    }
    private ServiceHandler serviceHandler;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread=new HandlerThread("HandlerService", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        looper=thread.getLooper();
        serviceHandler=new ServiceHandler(looper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg=serviceHandler.obtainMessage();
        msg.arg1=startId;
        serviceHandler.sendMessage(msg);
        return super.onStartCommand(intent, flags, startId);
    }
}
