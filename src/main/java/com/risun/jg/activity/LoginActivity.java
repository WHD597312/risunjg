package com.risun.jg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.risun.jg.R;
import com.risun.jg.pojo.Person;
import com.risun.jg.utils.AnnotationUtils;
import com.risun.jg.utils.HttpUtils;
import com.risun.jg.utils.Utils;
import com.risun.jg.utils.ViewId;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity{

    public String TAG="LoginActivity";

    @BindView(R.id.et_name) EditText et_name;
    @BindView(R.id.et_pswd) EditText et_pswd;
    @BindView(R.id.button_register) Button button_register;
    @BindView(R.id.button_login) Button button_login;
    @BindView(R.id.remeber_pswd) CheckBox remeber_pswd;

    SharedPreferences preferences;//用户个人信息
    SharedPreferences login;//用户登录状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        preferences=getSharedPreferences("my", Context.MODE_PRIVATE);//用户个人信息
        login=getSharedPreferences("login",Context.MODE_PRIVATE);//用户勾选状态
        if(login.contains("checked") && "1".equals(login.getString("checked",""))){
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }else{
            remeber_pswd.setChecked(false);
            et_name.setText("");
            et_pswd.setText("");
        }
        bindClickEvent();
    }

    /**
     * 绑定点击事件
     */
    private void bindClickEvent(){
        remeber_pswd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                if(isChecked){
                   if(preferences.contains("phone")){
                       if(login.contains("checked")){
                           et_name.setText(preferences.getString("phone",""));
                           et_pswd.setText(preferences.getString("password",""));
                           startActivity(intent);
                       }else{
                           SharedPreferences.Editor editor=login.edit();
                           editor.putString("checked","1");
                           editor.commit();
                           et_name.setText(preferences.getString("phone",""));
                           et_pswd.setText(preferences.getString("password",""));
                           startActivity(intent);
                       }
                   }else{
                       Utils.show(LoginActivity.this,"用户不存在");
                   }
                }
            }
        });
    }
    @OnClick({R.id.button_register,R.id.button_login})
    public void onClick(View view) {
        Intent intent=new Intent();
        switch (view.getId()){
            case R.id.button_register:
                intent.setClass(this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.button_login:
                String username=et_name.getText().toString();
                String password=et_pswd.getText().toString();
                Map<String,String> map=new HashMap<String,String>();
                map.put("phone",username);
                map.put("password",password);
                new LoginAsyncTask().execute(map);
                break;
        }
    }

    class LoginAsyncTask extends AsyncTask<Map<String,String>,Void,String>{

        @Override
        protected String doInBackground(Map[] maps) {
            String option="person/getPerson";
            Map<String,String> map=maps[0];
            String result=HttpUtils.getOkHpptRequest(option,map);
            Log.d(TAG,result);
            String response=parseJSONData(result);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!Utils.StringIsNullOrEmpty(s)){
                Utils.show(LoginActivity.this,s);
                if("登录成功".equals(s)){
                    Utils.show(LoginActivity.this,s);
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }else {
                    Utils.show(LoginActivity.this,s);
                }
            }
        }
    }
    private String parseJSONData(String response){
        String result=null;
        try {
                if(!Utils.StringIsNullOrEmpty(response)){
                    JSONObject jsonData=new JSONObject(response);
                    JSONObject msg=jsonData.getJSONObject("msg");
                    int state=msg.getInt("state");
                    if(state==0){
                        result=msg.getString("desc");
                        return result;
                    }else{
                        result= "登录成功";
                        JSONObject cont=msg.getJSONObject("cont");
                        JSONObject data=cont.getJSONObject("data");
                        if(preferences.contains("phone")){
                           return result;
                        }else{
                            SharedPreferences preferences=LoginActivity.this.getSharedPreferences("my", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putString("username",data.getString("username"));
                            editor.putString("password",data.getString("password"));
                            editor.putString("codes",data.getString("codes"));
                            editor.putString("phone",data.getString("phone"));
                            editor.putString("token",data.getString("token"));
                            editor.commit();
                        }
                    }
                }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
