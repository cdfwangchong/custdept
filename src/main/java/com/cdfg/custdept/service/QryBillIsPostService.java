package com.cdfg.custdept.service;

import com.cdfg.custdept.pojo.dto.XsdnoDto;
import com.cdfg.custdept.pojo.until.CustDeptlistDetEntity;
import com.cdfg.custdept.pojo.until.Login;

import java.util.List;


public interface QryBillIsPostService {

    XsdnoDto qryNotPostBill(Login login);

    List<CustDeptlistDetEntity> qryPostBill(Login login);

}
