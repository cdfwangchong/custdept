package com.cdfg.custdept.service;

import com.cdfg.custdept.pojo.dto.PickBillDto;
import com.cdfg.custdept.pojo.dto.PickNumDto;

import java.util.List;

public interface QryBillDetailService {

    List<PickBillDto> getselldetail(PickNumDto picknumdto);
}
