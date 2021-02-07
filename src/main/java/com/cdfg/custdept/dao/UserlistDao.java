package com.cdfg.custdept.dao;

import com.cdfg.custdept.pojo.dto.Userlist;
import org.springframework.stereotype.Repository;

@Repository
public interface UserlistDao {

    Userlist selectByPrimaryKey(String openId);

}