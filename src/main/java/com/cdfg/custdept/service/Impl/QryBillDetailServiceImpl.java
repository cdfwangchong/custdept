package com.cdfg.custdept.service.Impl;

import com.cdfg.custdept.dao.SellDetailDao;
import com.cdfg.custdept.pojo.dto.PickBillDto;
import com.cdfg.custdept.pojo.dto.PickNumDto;
import com.cdfg.custdept.service.QryBillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QryBillDetailServiceImpl implements QryBillDetailService {
    @Autowired
    SellDetailDao selldetaildao;

    @Override
    public List<PickBillDto> getselldetail(PickNumDto picknumdto) {
        return selldetaildao.QrySellDetail(picknumdto);
    }
}
