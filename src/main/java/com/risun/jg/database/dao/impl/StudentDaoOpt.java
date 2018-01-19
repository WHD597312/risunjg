package com.risun.jg.database.dao.impl;

import android.content.Context;

import com.risun.dao.DaoMaster;
import com.risun.dao.DaoSession;
import com.risun.dao.StudentDao;
import com.risun.jg.database.DBManager;
import com.risun.jg.pojo.Student;

import java.util.List;

/**
 * Created by whd on 2018/1/19.
 */

public class StudentDaoOpt {
    private DaoSession daoSession;
    private StudentDao studentDao;
    public StudentDaoOpt(Context context){
        DBManager dbManager=DBManager.getInstance(context);
        DaoMaster daoMaster=new DaoMaster(dbManager.getWritableDatabase());
        daoSession=daoMaster.newSession();
        studentDao=daoSession.getStudentDao();
    }
    public void insert(Student student){
        studentDao.insert(student);
    }
    public List<Student> queryAllStudent(){
        return daoSession.loadAll(Student.class);
    }
}
