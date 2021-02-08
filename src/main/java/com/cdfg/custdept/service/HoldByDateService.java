package com.cdfg.custdept.service;

import com.cdfg.custdept.pojo.dto.HoldByDateDto;
import com.cdfg.custdept.pojo.until.JcXsdbillEntity;

import java.util.List;

public interface HoldByDateService {
    List<JcXsdbillEntity> qryCheckBill(HoldByDateDto hbdDto);

    String insertDts(HoldByDateDto hbdDto, String worknumber);
}
