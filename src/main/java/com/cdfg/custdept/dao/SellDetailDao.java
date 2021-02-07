package com.cdfg.custdept.dao;

import com.cdfg.custdept.pojo.dto.PickBillDto;
import com.cdfg.custdept.pojo.dto.PickNumDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellDetailDao {
    List<PickBillDto> QrySellDetail(PickNumDto picknumdto);
}
