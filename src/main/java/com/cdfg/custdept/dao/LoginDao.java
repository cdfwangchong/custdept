package com.cdfg.custdept.dao;

import com.cdfg.custdept.pojo.dto.ThduserDto;
import com.cdfg.custdept.pojo.dto.UserDto;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginDao {
    ThduserDto selectByPrimaryKey(UserDto userDto);
}
