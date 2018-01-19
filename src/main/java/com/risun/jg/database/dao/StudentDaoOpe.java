package com.risun.jg.database.dao;

import android.content.Context;

import com.risun.jg.database.DBManager;
import com.risun.jg.pojo.Student;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by whd on 2018/1/18.
 */

public class StudentDaoOpe {
    private static final String DB_NAME="test.db";
    private static final String PASSWORD="password";

    /**
     * 添加数据至数据库
     * @param context
     * @param student
     */
    public static void insertData(Context context, Student student){
        DBManager.getDaoSession(context,DB_NAME,PASSWORD).insertOrReplace(student);
    }
    public static void insertData(Context context, List<Student> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
//        DbManager.getDaoSession(context, DB_NAME, PASSWPRD).getStudentDao().insertInTx(list);
        DBManager.getDaoSession(context, DB_NAME, PASSWORD).getStudentDao().insertOrReplaceInTx(list);
    }
    public static List<Student> queryAll(Context context){
        QueryBuilder<Student> builder= DBManager.getDaoSession(context,DB_NAME,PASSWORD).getStudentDao().queryBuilder();
        return builder.build().list();
    }
}
