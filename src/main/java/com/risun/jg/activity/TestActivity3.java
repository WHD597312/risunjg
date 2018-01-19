package com.risun.jg.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.risun.jg.R;
import com.risun.jg.database.dao.impl.StudentDaoOpt;
import com.risun.jg.pojo.Student;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity3 extends AppCompatActivity {

    @BindView(R.id.add) Button add;
    @BindView(R.id.delete) Button delete;
    @BindView(R.id.update) Button update;
    @BindView(R.id.check) Button check;
    @BindView(R.id.queryAll) Button queryAll;

    private StudentDaoOpt studentDaoOpt;
    private String TAG="TestActivity3";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        context=this;
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        initData();
        studentDaoOpt=new StudentDaoOpt(this);
    }
    private void initData(){

    }

    @OnClick({R.id.add,R.id.delete,R.id.update,R.id.check,R.id.queryAll})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.add:
                Student student=new Student();
                student.setName("王海东");
                student.setAge(24);
                studentDaoOpt.insert(student);
                break;
            case R.id.delete:
                break;
            case R.id.update:
                break;
            case R.id.check:
                break;
            case R.id.queryAll:
                List<Student> list=studentDaoOpt.queryAllStudent();
                EventBus.getDefault().post(list);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void queryAllStudent(List<Student> students){
        for (Student student:students){
            Log.d(TAG,"id="+student.getId()+",name="+student.getName()+","+student.getAge());
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
