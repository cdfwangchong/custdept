package com.cdfg.custdept.dao;

import com.cdfg.custdept.pojo.dto.Jcyysjinfo;
import java.math.BigDecimal;

public interface JcyysjinfoDao {
    int deleteByPrimaryKey(BigDecimal yyseq);

    int insert(Jcyysjinfo record);

    int insertSelective(Jcyysjinfo record);

    Jcyysjinfo selectByPrimaryKey(BigDecimal yyseq);

    int updateByPrimaryKeySelective(Jcyysjinfo record);

    int updateByPrimaryKey(Jcyysjinfo record);
}