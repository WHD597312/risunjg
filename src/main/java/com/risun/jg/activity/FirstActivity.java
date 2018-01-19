package com.risun.jg.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.risun.jg.R;
import com.risun.jg.pojo.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static cn.jpush.android.api.JPushInterface.a.t;

public class FirstActivity extends AppCompatActivity {

    @BindView(R.id.message) TextView message;
    @BindView(R.id.btn_msg) Button btn_msg;
    @BindView(R.id.childThread) Button childThread;
    @BindView(R.id.text) TextView text;
    String TAG="FirstActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        message.setText("MainActivity");

    }

    @OnClick({R.id.btn_msg,R.id.childThread})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_msg:
                startActivity(new Intent(this,SecondActivity.class));
                break;
            case R.id.childThread:
                new Thread("子线程"){
                    @Override
                    public void run() {
                        EventBus.getDefault().post("我发射了");
                    }
                }.start();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessagePostThread(String event){
        Log.d(TAG,"onMessagePostThread："+event+"thread:"+Thread.currentThread().getName());
    }
    @Subscribe(threadMode=ThreadMode.MAIN)
    public void onMessageMainThread(String event){
        Log.d(TAG,"onMessageMainThread："+event+"thread:"+Thread.currentThread().getName());
    }
    @Subscribe(threadMode=ThreadMode.BACKGROUND)
    public void onMessageBackgroundThread(String event){
        Log.d(TAG,"onMessageBackgroundThread："+event+"thread:"+Thread.currentThread().getName());
    }
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageAsyncThread(String event){
        Log.d(TAG,"onMessageAsyncThread："+event+"thread:"+Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent event){
        text.setText(event.getMessage());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
