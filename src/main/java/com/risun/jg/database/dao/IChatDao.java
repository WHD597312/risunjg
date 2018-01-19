package com.risun.jg.database.dao;

import com.risun.jg.pojo.Chat;

import java.util.List;

/**
 * Created by whd on 2018/1/3.
 */

public interface IChatDao {
    public boolean insert(Chat chat);
    public List<Chat> findAllChatByCodes(String codes);
}
