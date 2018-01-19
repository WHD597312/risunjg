package com.risun.jg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.risun.jg.R;
import com.risun.jg.utils.AnnotationUtils;
import com.risun.jg.utils.HttpUtils;
import com.risun.jg.utils.Utils;
import com.risun.jg.utils.ViewId;


import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity{

    private String TAG="RegisterActivity";
    @BindView(R.id.et_name) EditText et_name;
    @BindView(R.id.et_pswd) EditText et_pswd;
    @BindView(R.id.et_phone) EditText et_phone;
    @BindView(R.id.button_register) Button button_register;
    boolean flag=false;//注册成功或者失败的标志
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        token=JPushInterface.getRegistrationID(getApplicationContext());
        Log.d(TAG,"Token:"+token);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick({R.id.button_register})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_register:
                String username=et_name.getText().toString();
                String password=et_pswd.getText().toString();
                String phone=et_phone.getText().toString();
                Map<String,String> map=new HashMap<String,String>();
                map.put("username",username);
                map.put("password",password);
                map.put("phone",phone);
                map.put("token",token);
                new RegiserAsyncTask().execute(map);
                break;
        }
    }
    class RegiserAsyncTask extends AsyncTask<Map<String,String>,Void,String>{

        @Override
        protected String doInBackground(Map<String, String>[] maps) {
            String option="person/getPerson";
            Map<String,String> map=maps[0];
            String result=HttpUtils.getOkHpptRequest(option,map);
            boolean existUser=parseExistUser(result);
            if(existUser){
                return "用户已注册";
            }else{
                option="person/insert";
                result=HttpUtils.getOkHpptRequest(option,map);
                boolean success=parseJSONDada(result);
                if(success){
                    return "注册成功";
                }else{
                    return "注册失败";
                }
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if("注册成功".equals(s)){
                Utils.show(RegisterActivity.this,s);
                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
            }else{
                Utils.show(RegisterActivity.this,s);
            }
        }
    }
    private boolean parseJSONDada(String response){
        int status=0;
        if(response!=null && response!=""){
            try {
                JSONObject jsonData=new JSONObject(response);
                JSONObject msg=jsonData.getJSONObject("msg");
                JSONObject cont=msg.getJSONObject("cont");
                JSONObject data=cont.getJSONObject("data");
                status=cont.getInt("status");
                SharedPreferences preferences=RegisterActivity.this.getSharedPreferences("my", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("username",data.getString("username"));
                editor.putString("password",data.getString("password"));
                editor.putString("codes",data.getString("codes"));
                editor.putString("phone",data.getString("phone"));
                editor.putString("token",data.getString("token"));
                editor.commit();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return status>0?true:false;
    }
    public boolean parseExistUser(String response){
        int state=1;
        try{
            JSONObject jsonData=new JSONObject(response);
            JSONObject msg=jsonData.getJSONObject("msg");
            state=msg.getInt("state");
        }catch (Exception e){
            e.printStackTrace();
        }
        return state==1?true:false;
    }
}
