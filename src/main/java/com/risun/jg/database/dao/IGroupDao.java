package com.risun.jg.database.dao;

import com.risun.jg.pojo.Group;

import java.util.List;

/**
 * Created by whd on 2017/12/29.
 */

public interface IGroupDao {
    public boolean insert(Group group);
    public List<Group> findAllGroup();
    public Group findByCodes(String codes);
}
