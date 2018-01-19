package com.risun.jg.activity;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.risun.jg.R;
import com.risun.jg.fragment.ContactFragment;
import com.risun.jg.fragment.MessageFragment;
import com.risun.jg.fragment.MyFragment;
import com.risun.jg.utils.AnnotationUtils;
import com.risun.jg.utils.ViewId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{


    private String TAG="MainActivity";
    @BindView(R.id.image_message) ImageButton image_message;

    @BindView(R.id.image_contact) ImageButton image_contact;

    @BindView(R.id.image_me) ImageButton image_me;

    private SharedPreferences login;//个人登录状态
    private FragmentManager fragmentManager;//碎片管理
    private FragmentTransaction fragmentTransaction;//碎片事务
    private ContactFragment contactFragment;//联系人碎片
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        login=getSharedPreferences("login", Context.MODE_PRIVATE);
        fragmentManager=getFragmentManager();//获取碎片管理者实例
        fragmentTransaction=fragmentManager.beginTransaction();//开启事务
        contactFragment=new ContactFragment();
        fragmentTransaction.replace(R.id.body_layout,contactFragment);
        fragmentTransaction.commit();

    }


    @OnClick({R.id.image_message,R.id.image_contact,R.id.image_me})
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.image_message:
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.body_layout,new MessageFragment());
                fragmentTransaction.commit();
                Log.d(TAG,"image_message");
                break;
            case R.id.image_contact:
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.body_layout,contactFragment);
                fragmentTransaction.commit();
                break;
            case R.id.image_me:
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.body_layout,new MyFragment());
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        removeCheched();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeCheched();
    }
    private void removeCheched(){
        boolean state=login.contains("checked");
        if (state){
            SharedPreferences.Editor editor=login.edit();
            editor.remove("checked");
            editor.commit();
            Log.d(TAG,"onStop");
        }
    }
}
