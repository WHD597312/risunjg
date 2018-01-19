package com.risun.jg.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by whd on 2018/1/18.
 */

@Entity
public class Student {
    @Id
    private Long id;
    private String name;
    private int age;

    public Student(){

    }
    public Student(String name,int age){
        this.name=name;
        this.age=age;
    }
    public Student(Long id,String name,int age){
        this.id=id;
        this.name=name;
        this.age=age;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }



    @Keep
    @Override
    public String toString() {
        return "Student{" +
                "id="+id+
                ",name="+name+
                ",age="+age+
                "}";
    }
}
