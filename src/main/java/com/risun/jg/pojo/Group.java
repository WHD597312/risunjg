package com.risun.jg.pojo;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by whd on 2017/12/29.
 */

@DatabaseTable(tableName = "t_group")
public class Group {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "codes")
    private String codes;
    @DatabaseField(columnName = "name")
    private String name;

    public Group() {
    }
    public Group(String codes, String name) {
        this.codes = codes;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
