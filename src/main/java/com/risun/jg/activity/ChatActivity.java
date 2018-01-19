package com.risun.jg.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ListView;
import android.widget.TextView;

import com.risun.jg.R;
import com.risun.jg.adapter.ChatAdapter;
import com.risun.jg.database.dao.IChatDao;
import com.risun.jg.database.dao.IPersonDao;
import com.risun.jg.database.dao.impl.ChatDaoImpl;
import com.risun.jg.database.dao.impl.PersonDaoImpl;
import com.risun.jg.pojo.Chat;
import com.risun.jg.pojo.Person;
import com.risun.jg.utils.AnnotationUtils;
import com.risun.jg.utils.ViewId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG="ChatActivity";
    public static boolean isForeground=false;

    @ViewId(id = R.id.tv_name)
    private TextView tv_name;
    @ViewId(id = R.id.et_content)
    private EditText et_content;
    @ViewId(id=R.id.button_send)
    private Button button_send;
    @ViewId(id = R.id.chat_list)
    private ListView chat_list;

    private ChatAdapter adapter;
    private List<Chat> chats;


    private SharedPreferences preferences;
    private String codes;//与交谈用户的codes
    private IPersonDao personDao;
    private Person person;
    private Person my;
    private IChatDao chatDao;
    class MessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String content=intent.getStringExtra("message");
            Log.d(TAG,content);
            Chat chat=new Chat();
            chat.setName(person.getUsername());
            chat.setIsMeSend("0");
            chat.setContent(content);
            Message message=handler.obtainMessage();
            message.obj=chat;
            handler.sendMessage(message);
        }
    }
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Chat chat= (Chat) msg.obj;
            if(chat!=null){
                chat.setCodes(codes);
                if(chatDao.insert(chat)){
                    chats.add(chat);
                    adapter.notifyDataSetChanged();
                    chat_list.setSelection(chats.size());
                }

            }
        }
    };
    private MessageReceiver messageReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_chat);
        AnnotationUtils.findActivityById("com.risun.jg.activity.ChatActivity",this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();
        codes=intent.getStringExtra("codes");
        personDao=new PersonDaoImpl(this);
        person=personDao.findByCodes(codes);


        preferences=getSharedPreferences("my", Context.MODE_PRIVATE);
        String username=preferences.getString("username","");
        String phone=preferences.getString("phone","");
        my=new Person();
        my.setUsername(username);
        my.setPhone(phone);
        button_send.setOnClickListener(this);

        tv_name.setText(person.getUsername());

        chatDao=new ChatDaoImpl(this);
        chats=chatDao.findAllChatByCodes(codes);

        adapter=new ChatAdapter(this,chats);
        chat_list.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        isForeground=true;
        messageReceiver=new MessageReceiver();
        IntentFilter intentFilter=new IntentFilter("receiver");
        registerReceiver(messageReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        isForeground=false;
        if(messageReceiver!=null){
            unregisterReceiver(messageReceiver);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_send:
                String msg_content=et_content.getText().toString();
                new ChatAsyncTask().execute(msg_content);
                break;
        }
    }
    class ChatAsyncTask extends AsyncTask<String,Void,Chat>{

        @Override
        protected Chat doInBackground(String... strings) {
            Chat chat=null;
            String msg_content=strings[0];
            boolean flag=postData(msg_content);
            if (flag){
                chat=new Chat();
                chat.setName(person.getUsername());
                chat.setIsMeSend("1");
                chat.setContent(msg_content);
                chat.setCodes(codes);
               if(chatDao.insert(chat)){
                   chats.add(chat);
               }
            }
            return chat;
        }

        @Override
        protected void onPostExecute(Chat chat) {
            super.onPostExecute(chat);
            if (chat!=null){
                adapter.notifyDataSetChanged();
                chat_list.setSelection(chats.size());
                et_content.setText("");
            }
        }
    }
    private boolean postData(String msg_content){
        boolean flag=false;
        try{
            OkHttpClient client=new OkHttpClient();
            JSONObject audience=new JSONObject();
            JSONArray registration_id=new JSONArray();
            registration_id.put(0,JPushInterface.getRegistrationID(ChatActivity.this));
            audience.put("registration_id",registration_id);
            JSONObject message=new JSONObject();
            message.put("msg_content",msg_content);
            JSONObject notification=new JSONObject();
            JSONObject android=new JSONObject();
            android.put("alert",msg_content);
            android.put("title",person.getUsername());
            android.put("builder_id",2);

            JSONObject extras=new JSONObject();
            extras.put("news_id",3);
            extras.put("my_key",my.getCodes());
            android.put("extras",extras);
            notification.put("android",android);

            FormBody body=new FormBody.Builder()
                    .addEncoded("platform","all")
                    .addEncoded("audience",audience.toString())
                    .addEncoded("message",message.toString())
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
            flag=response.isSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }
}
