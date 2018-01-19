package com.risun.jg.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.risun.jg.R;
import com.risun.jg.adapter.MyPagerAdapter;
import com.risun.jg.pojo.Ad;
import com.risun.jg.service.BoundService;
import com.risun.jg.service.HandlerService;
import com.risun.jg.service.IntentService2;
import com.risun.jg.service.StartService;
import com.risun.jg.utils.HttpUtils;
import com.risun.jg.utils.ImageLoader;
import com.risun.jg.utils.SDHelper;
import com.risun.jg.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity{
    private static final int FIRST_PAGE = 1;
    private String TAG="TestActivity";
    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.dot_layout) LinearLayout dot_layout;
    private List<Ad> list=new ArrayList<Ad>();
    private int currentPosition=0;
    private boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        list.add(new Ad(R.drawable.img01));
        list.add(new Ad(R.drawable.img02));
        list.add(new Ad(R.drawable.img03));

        initDos();
        viewpager.setAdapter(new MyPagerAdapter(this,list));
        viewpager.setCurrentItem(0);
        updataDos();
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updataDos();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //        若viewpager滑动未停止，直接返回
                Log.d(TAG,"state:"+state);

            }
        });
    }
    private void initDos(){
        for(int i=0;i<list.size();i++){
            View view=new View(this);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(10,10);
            if (i!=0){
                params.leftMargin=20;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.dot_select);
            dot_layout.addView(view);
        }
    }
    public void updataDos(){
        int currentPage=viewpager.getCurrentItem() % list.size();
        for(int i=0;i<dot_layout.getChildCount();i++){
            dot_layout.getChildAt(i).setEnabled(i==currentPage);
        }
    }
}
