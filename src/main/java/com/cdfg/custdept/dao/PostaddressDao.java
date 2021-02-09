package com.cdfg.custdept.dao;

import com.cdfg.custdept.pojo.dto.Jcyysjinfo;
import com.cdfg.custdept.pojo.dto.YysjDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface PostaddressDao {
    Map insert(Map<String, String> param);

    Jcyysjinfo selectByPrimaryKey(String cardId);

    Map updateByPrimaryKey(Map<String, String> param);
}