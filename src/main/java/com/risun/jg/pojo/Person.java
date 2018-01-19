package com.risun.jg.pojo;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by whd on 2017/12/23.
 */
@DatabaseTable(tableName = "t_person")
public class Person {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "username")
    private String username;
    @DatabaseField(columnName = "password")
    private String password;
    @DatabaseField(columnName = "codes")
    private String codes;//唯一标识
    @DatabaseField(columnName = "phone")
    private String phone;
    @DatabaseField(columnName = "gcodes")
    private String gcodes;//组名称
    @DatabaseField(columnName ="token")
    private String token;
    public Person() {
    }


    public Person(String username, String password, String codes, String phone) {
        this.username = username;
        this.password = password;
        this.codes = codes;
        this.phone = phone;
    }

    public Person(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGcodes() {
        return gcodes;
    }

    public void setGcodes(String gcodes) {
        this.gcodes = gcodes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
