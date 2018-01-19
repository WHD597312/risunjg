package com.risun.jg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.risun.jg.R;
import com.risun.jg.database.dao.IGroupDao;
import com.risun.jg.database.dao.IPersonDao;
import com.risun.jg.database.dao.impl.GroupDaoImpl;
import com.risun.jg.database.dao.impl.PersonDaoImpl;
import com.risun.jg.pojo.Group;
import com.risun.jg.pojo.Person;
import com.risun.jg.utils.AnnotationUtils;
import com.risun.jg.utils.HttpUtils;
import com.risun.jg.utils.Utils;
import com.risun.jg.utils.ViewId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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

public class AddActivity extends AppCompatActivity{

    private String TAG="AddActivity";

    @BindView(R.id.edit_search) EditText edit_search;
    @BindView(R.id.image_search) ImageView image_search;
    @BindView(R.id.text_name) TextView text_name;
    @BindView(R.id.et_group) EditText et_group;
    @BindView(R.id.button_add) Button button_add;

    private IPersonDao personDao;
    private IGroupDao groupDao;
    private Person person;
    private SharedPreferences my;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        personDao=new PersonDaoImpl(this);
        groupDao=new GroupDaoImpl(this);
        my=getSharedPreferences("my", Context.MODE_PRIVATE);
    }

    @OnClick({R.id.image_search,R.id.button_add})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_search:
                searchAddPerson();
                break;
            case R.id.button_add:
                requestAddPerson(person);
                break;
        }
    }

    /**
     * 请求添加用户
     * @param person
     */
    public void requestAddPerson(Person person){

        new RequestAddPersonAsyncTask().execute(person);
    }
    class RequestAddPersonAsyncTask extends  AsyncTask<Person,Void,String>{


        @Override
        protected String doInBackground(Person... people) {
            String result=null;
            Person person=people[0];
            if(person!=null){
                    try {

                        OkHttpClient client=new OkHttpClient();
                        JSONObject audience=new JSONObject();
                        JSONArray registration_id=new JSONArray();
                        registration_id.put(0,person.getToken());
                        audience.put("registration_id",registration_id);

                        JSONObject notification=new JSONObject();
                        JSONObject android=new JSONObject();
                        android.put("alert","请求添加好友");
                        android.put("title",my.getString("username",""));
                        android.put("builder_id",2);
                        notification.put("android",android);
                        JSONObject extras=new JSONObject();
                        extras.put("news_id",1);
                        extras.put("my_key",my.getString("phone",""));
                        android.put("extras",extras);
                        notification.put("android",android);

                        FormBody body=new FormBody.Builder()
                                .addEncoded("platform","all")
                                .addEncoded("audience",audience.toString())
                                .addEncoded("notification",notification.toString())
                                .build();
                        String url="https://api.jpush.cn/v3/push";
                        Request request=new Request.Builder()
                                .url(url)
                                .addHeader("Authorization","Basic MWNkYjkyZDM0MTk2YmNmYmExODk0OGU5OjIzMjUxZDU4YjE5ZGNlZTdmZmE2OWJjNw==")
                                .post(body)
                                .build();
                        Call call=client.newCall(request);
                        Response response=call.execute();
                        if(response.isSuccessful()){
                            result="发送成功";
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(Utils.StringIsNullOrEmpty(s)){
                Utils.show(AddActivity.this,"发送失败");
            }else{
                Utils.show(AddActivity.this,s);
                Intent intent=new Intent(AddActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }
    }
    /**
     * 搜索要添加的用户
     */
    private void searchAddPerson(){
        String phone=edit_search.getText().toString();
        Map<String,String> map=new HashMap<String,String>();
        map.put("phone",phone);
        new SearchAddPersonAsyncTack().execute(map);

    }
    class SearchAddPersonAsyncTack extends AsyncTask<Map<String,String>,Void,String>{

        @Override
        protected String doInBackground(Map<String, String>[] maps) {
            String response=null;
            String option="person/getPerson";
            Map<String,String> map=maps[0];
            String result=HttpUtils.getOkHpptRequest(option,map);
            Log.d(TAG,result);
            person=parseJSONDPerson(result);
            if(person!=null){
                Person person2=personDao.findByCodes(person.getCodes());
                if(person2!=null){
                    response="该用户已添加";
                }else{
                    response="用户未添加";
                }
            }else{
                response="用户不存在";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!Utils.StringIsNullOrEmpty(s)){
                Utils.show(AddActivity.this,s);
                if("用户不存在".equals(s)){
                    Utils.show(AddActivity.this,s);
                }else if("该用户已添加".equals(s)){
                    Utils.show(AddActivity.this,s);
                    Intent intent=new Intent(AddActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    text_name.setText(person.getUsername());
                }
            }
        }
    }


    private Person parseJSONDPerson(String response){
        Person person=null;
        try {
            if(!Utils.StringIsNullOrEmpty(response)) {
                JSONObject jsonData = new JSONObject(response);
                JSONObject msg = jsonData.getJSONObject("msg");
                int state=msg.getInt("state");
                if(state==0){
                    return null;
                }
                JSONObject cont = msg.getJSONObject("cont");
                JSONObject data = cont.getJSONObject("data");
                person=JSON.parseObject(data.toString(),Person.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return person;
    }
}
