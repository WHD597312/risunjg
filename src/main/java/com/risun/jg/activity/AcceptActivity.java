package com.risun.jg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.risun.jg.R;
import com.risun.jg.adapter.PersonAdapter;
import com.risun.jg.database.dao.IGroupDao;
import com.risun.jg.database.dao.IPersonDao;
import com.risun.jg.database.dao.impl.GroupDaoImpl;
import com.risun.jg.database.dao.impl.PersonDaoImpl;
import com.risun.jg.pojo.Group;
import com.risun.jg.pojo.ItemContent;
import com.risun.jg.pojo.Person;
import com.risun.jg.utils.AnnotationUtils;
import com.risun.jg.utils.HttpUtils;
import com.risun.jg.utils.Utils;
import com.risun.jg.utils.ViewId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AcceptActivity extends AppCompatActivity{

    private String TAG="AcceptActivity";
    @BindView(R.id.perinfo_list) ListView perinfo_list;
    @BindView(R.id.accept_button) Button accept_button;
    @BindView(R.id.et_group) EditText et_group;
    private List<ItemContent> list;
    private PersonAdapter adapter;
    private Person person;
    private IPersonDao personDao;
    private IGroupDao groupDao;
    private SharedPreferences my;
    private int news_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        my=getSharedPreferences("my", Context.MODE_PRIVATE);
        Intent intent=getIntent();
        String my_key=intent.getStringExtra("my_key");
        news_id=intent.getIntExtra("news_id",0);
        Map<String,String> map=new HashMap<String,String>();
        map.put("phone",my_key);
        new RequestAsyncTask().execute(map);
        list=new ArrayList<ItemContent>();
        adapter=new PersonAdapter(this,list);
        perinfo_list.setAdapter(adapter);

        personDao=new PersonDaoImpl(this);
        groupDao=new GroupDaoImpl(this);

    }

    @OnClick({R.id.accept_button})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.accept_button:
                String group=et_group.getText().toString();
                Map<String,String> map=new HashMap<String,String>();
                map.put("name",group);
                new AddPersonGroupAsyncTask().execute(map);
                break;
        }
    }

    class RequestAsyncTask extends AsyncTask<Map<String,String>,Void,Person>{
        @Override
        protected Person doInBackground(Map<String, String>[] maps) {
            String option="person/getPerson";
            Map<String,String> map=maps[0];
            String response=HttpUtils.getOkHpptRequest(option,map);
            Log.d(TAG,response);
            person=parsePerson(response);
            return person;
        }
        @Override
        protected void onPostExecute(Person person) {
            super.onPostExecute(person);
            if(person!=null){
                list.add(new ItemContent("姓名",person.getUsername()));
                list.add(new ItemContent("电话",person.getPhone()));
                adapter.notifyDataSetChanged();
            }
        }
    }
    class AddPersonGroupAsyncTask extends AsyncTask<Map<String,String>,Void,String>{

        @Override
        protected String doInBackground(Map<String, String>[] maps) {
            String response=null;

            if(person!=null){
                Person person2=personDao.findByCodes(person.getCodes());
                if(person2==null){
                    String option="group/insert";
                    Map<String,String> map=maps[0];
                    String result=HttpUtils.getOkHpptRequest(option,map);
                    Log.d(TAG,result);
                    Group group=parseJSONGroup(result);
                    if(group==null){
                        response="添加失败";
                    }else{
                        response="添加成功";
                    }
                }else{
                    Group group=groupDao.findByCodes(person2.getGcodes());
                    if(group!=null){
                        response="该用户已分组";
                    }
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(!Utils.StringIsNullOrEmpty(s)){
                Intent intent=new Intent(AcceptActivity.this,MainActivity.class);
                if("添加成功".equals(s)){
                    Utils.show(AcceptActivity.this,s);
                    new RequestAddPersonAsyncTask().execute(person);
                    startActivity(intent);
                }else{
                    Utils.show(AcceptActivity.this,s);
                    startActivity(intent);
                }
            }
        }
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
                        android.put("alert",my.getString("username","")+"已添加你为好友");
                        android.put("title",my.getString("username",""));
                        android.put("builder_id",2);
                        notification.put("android",android);
                        JSONObject extras=new JSONObject();
                        extras.put("news_id",2);
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
    }
    private Group parseJSONGroup(String response){
        Group group=null;
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONObject msg=jsonObject.getJSONObject("msg");
            int state=msg.getInt("state");
            if(state==0){
                return null;
            }else{
                JSONObject cont=msg.getJSONObject("cont");
                JSONObject data=cont.getJSONObject("data");

                group=JSON.parseObject(data.toString(),Group.class);

                if(groupDao.insert(group)){
                    if(person!=null){
                        person.setGcodes(group.getCodes());//添加分组
                        if(personDao.insert(person)){//添加联系人
                            return group;
                        }else{
                            return null;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return group;
    }
    private Person parsePerson(String response){
        Person person=null;
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONObject msg=jsonObject.getJSONObject("msg");
            int state=msg.getInt("state");
            if(state==0){
                return person;
            }else{
                JSONObject cont=msg.getJSONObject("cont");
                JSONObject data=cont.getJSONObject("data");
                person= JSON.parseObject(data.toString(),Person.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return person;
    }
}
