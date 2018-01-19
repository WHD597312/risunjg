package com.risun.jg.database.dao.impl;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.risun.jg.database.DatabaseHelper;
import com.risun.jg.database.dao.IGroupDao;
import com.risun.jg.pojo.Group;

import java.util.List;

/**
 * Created by whd on 2017/12/29.
 */

public class GroupDaoImpl implements IGroupDao {
    private Context context;
    private DatabaseHelper helper;

    public GroupDaoImpl(Context context) {
        this.context = context;
        this.helper = DatabaseHelper.getInstance(context);
    }

    @Override
    public boolean insert(Group group) {
        int n=0;
        try{
            Dao dao=helper.getDao(group.getClass());
            n=dao.create(group);
        }catch (Exception e){
            e.printStackTrace();
        }
        return n>0?true:false;
    }

    @Override
    public List<Group> findAllGroup() {
        List<Group> groups=null;
        try{
            Dao dao=helper.getDao(Group.class);
            groups=dao.queryBuilder().query();
        }catch (Exception e){
            e.printStackTrace();
        }
        return groups;
    }

    @Override
    public Group findByCodes(String codes) {
        Group group=null;
        try{
            Dao dao=helper.getDao(Group.class);
            group= (Group) dao.queryBuilder().where().eq("codes",codes).queryForFirst();
        }catch (Exception e){
            e.printStackTrace();
        }
        return group;
    }
}
