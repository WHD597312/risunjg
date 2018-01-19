package com.risun.jg.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.risun.jg.pojo.Chat;
import com.risun.jg.pojo.Group;
import com.risun.jg.pojo.Person;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by whd on 2017/12/28.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String TABLE_NAME="risun-jg.db";
    private static DatabaseHelper instance=null;
    private Map<String,Dao> daoMap=new HashMap<String,Dao>();
    private DatabaseHelper(Context context){
        super(context,TABLE_NAME,null,2);
    }

    /**
     * 单例设计模式
     * @param context
     * @return
     */
    public static DatabaseHelper getInstance(Context context){
        if(instance==null){
            instance=new DatabaseHelper(context);
        }
        return instance;
    }

    /**
     *建表
     * @param database
     * @param connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            //创建pereon表
            TableUtils.createTableIfNotExists(connectionSource,Person.class);
            TableUtils.createTableIfNotExists(connectionSource, Group.class);
            TableUtils.createTableIfNotExists(connectionSource, Chat.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 更新表
     * @param database
     * @param connectionSource
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,Person.class,true);
            TableUtils.dropTable(connectionSource,Group.class,true);
            TableUtils.dropTable(connectionSource,Chat.class,true);
            onCreate(database,connectionSource);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public  Dao getDao(Class  clazz) throws SQLException {
        Dao dao=null;
        String className=clazz.getSimpleName();
        if (daoMap.containsKey(className)){
            dao=daoMap.get(className);
        }
        if(dao==null){
            dao=super.getDao(clazz);
            daoMap.put(className,dao);
        }
        return dao;
    }

    /**
     * 删除表中所有数据
     * @param clazz
     */
    public void clearData(Class clazz){
        try{
            TableUtils.clearTable(connectionSource,clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
