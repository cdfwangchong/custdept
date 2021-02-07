package com.cdfg.custdept.service;

import com.cdfg.custdept.pojo.dto.HoldByDateDto;

public interface HoldByDateService {
    String qryCheckBill(HoldByDateDto hbdDto);

    String insertDts(HoldByDateDto hbdDto, String worknumber);
}
