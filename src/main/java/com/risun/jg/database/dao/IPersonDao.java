package com.risun.jg.database.dao;

import com.risun.jg.pojo.Person;

import java.util.List;

/**
 * Created by whd on 2017/12/28.
 */

public interface IPersonDao {
    public boolean insert(Person person);
    public boolean update(Person person);
    public Person findByCodes(String codes);
    public List<Person> findGroupPerson(String gcodes);
}
