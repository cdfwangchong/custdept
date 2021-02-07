package com.cdfg.custdept.dao;

import com.cdfg.custdept.pojo.until.SellHeadEntity;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface HoldByDateDao {
    SellHeadEntity qryCheckBill(String billNO);

    Map insertDts(Map<String, String> param);
}
