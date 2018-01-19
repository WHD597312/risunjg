package com.risun.jg.pojo;

/**
 * Created by whd on 2018/1/2.
 */

public class ItemContent {
    private String name;
    private String content;

    public ItemContent() {
    }

    public ItemContent(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
