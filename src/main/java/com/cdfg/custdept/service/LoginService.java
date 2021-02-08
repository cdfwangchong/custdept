package com.cdfg.custdept.service;

import com.cdfg.custdept.pojo.dto.UserDto;

import java.util.Map;

public interface LoginService {
    public Map<String, Object> login(UserDto userDto);
}
