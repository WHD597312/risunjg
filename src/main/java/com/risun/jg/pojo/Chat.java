package com.risun.jg.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by whd on 2018/1/2.
 */

@DatabaseTable(tableName = "t_chat")
public class Chat {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "name")
    private String name;//聊天人的姓名
    @DatabaseField(columnName = "time")
    private String time;//聊天人的时间
    @DatabaseField(columnName = "isMeSend")
    private String isMeSend;//是否是自己发送的
    @DatabaseField(columnName = "content")
    private String content;//聊天的内容
    @DatabaseField(columnName ="codes")
    private String codes;//聊天信息人的codes
    @DatabaseField(columnName = "token")
    private String token;//
    public Chat() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsMeSend() {
        return isMeSend;
    }

    public void setIsMeSend(String isMeSend) {
        this.isMeSend = isMeSend;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
