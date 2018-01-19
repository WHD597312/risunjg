package com.risun.jg.database.dao.impl;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.risun.jg.database.DatabaseHelper;
import com.risun.jg.database.dao.IChatDao;
import com.risun.jg.pojo.Chat;

import java.util.List;

/**
 * Created by whd on 2018/1/3.
 */

public class ChatDaoImpl implements IChatDao {
    private Context context;
    private DatabaseHelper helper;
    public ChatDaoImpl(Context context) {
        this.context = context;
        this.helper=DatabaseHelper.getInstance(context);
    }

    @Override
    public boolean insert(Chat chat) {
        int n=0;
        try{
            Dao dao=helper.getDao(chat.getClass());
            n=dao.create(chat);
        }catch (Exception e){
            e.printStackTrace();
        }
        return n>0?true:false;
    }

    @Override
    public List<Chat> findAllChatByCodes(String codes) {
        List<Chat> list=null;
        try{
            Dao dao=helper.getDao(Chat.class);
            list=dao.queryBuilder().where().eq("codes",codes).query();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
