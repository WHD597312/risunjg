package com.risun.jg.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.risun.jg.R;
import com.risun.jg.pojo.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecondActivity extends AppCompatActivity {

    @BindView(R.id.send) Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.send)
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.send:
                EventBus.getDefault().post(new MessageEvent("欢迎EventBus"));
                finish();
                break;
        }
    }
}
