package com.cdfg.custdept.dao;

import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface HoldByDateDao {
    Map qryCheckBill(Map param);

    Map insertDts(Map param);
}
