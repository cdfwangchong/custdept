package com.cdfg.custdept.dao;

import com.cdfg.custdept.pojo.dto.DmzgDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CustJczgDao {

    Map QryCustJczg(Map param);

    int update(List<DmzgDto> record);
}
