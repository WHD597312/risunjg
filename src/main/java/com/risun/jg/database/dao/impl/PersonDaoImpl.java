package com.risun.jg.database.dao.impl;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.risun.jg.database.DatabaseHelper;
import com.risun.jg.database.dao.IPersonDao;
import com.risun.jg.pojo.Person;

import java.util.List;

/**
 * Created by whd on 2017/12/28.
 */

public class PersonDaoImpl implements IPersonDao{
    private Context context;
    private DatabaseHelper helper;

    public PersonDaoImpl(Context context) {
        this.context = context;
        this.helper = DatabaseHelper.getInstance(context);
    }

    @Override
    public boolean insert(Person person) {
        int n=0;
        try{
            Dao dao=helper.getDao(person.getClass());
            n=dao.create(person);
        }catch (Exception e){
            e.printStackTrace();
        }
        return n>0?true:false;
    }

    @Override
    public boolean update(Person person) {
        int n=0;
        try{
            Dao dao=helper.getDao(person.getClass());
            n=dao.update(person);
        }catch (Exception e){
            e.printStackTrace();
        }
        return n>0?true:false;
    }

    @Override
    public Person findByCodes(String codes) {
        Person person=null;
       try {
            Dao dao=helper.getDao(Person.class);
            person=(Person) dao.queryBuilder().where().eq("codes",codes).queryForFirst();
       }catch (Exception e){
           e.printStackTrace();
       }
        return person;
    }

    @Override
    public List<Person> findGroupPerson(String gcodes) {
        List<Person> list=null;
        try{
            Dao dao=helper.getDao(Person.class);
            list=dao.queryBuilder().where().eq("gcodes",gcodes).query();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
