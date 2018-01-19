package com.risun.jg.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.risun.jg.R;
import com.risun.jg.database.dao.IPersonDao;
import com.risun.jg.database.dao.impl.PersonDaoImpl;
import com.risun.jg.adapter.PersonAdapter;
import com.risun.jg.pojo.ItemContent;
import com.risun.jg.pojo.Person;
import com.risun.jg.utils.AnnotationUtils;
import com.risun.jg.utils.ViewId;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PerInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG="PerInfoActivity";
    @BindView(R.id.perinfo_list) ListView perinfo_list;
    private List<ItemContent> list;
    private IPersonDao personDao;

    @BindView(R.id.send_button) Button send_button;
    private String codes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_perinfo);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();
        codes=intent.getStringExtra("codes");
        personDao=new PersonDaoImpl(this);
        Person person=personDao.findByCodes(codes);
        list=new ArrayList<ItemContent>();
        list.add(new ItemContent("姓名",person.getUsername()));
        list.add(new ItemContent("电话",person.getPhone()));
        PersonAdapter adapter=new PersonAdapter(this,list);
        perinfo_list.setAdapter(adapter);

        send_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send_button:
                Intent intent=new Intent(PerInfoActivity.this,ChatActivity.class);
                intent.putExtra("codes",codes);
                startActivity(intent);
                break;
        }
    }
}
