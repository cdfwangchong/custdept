package com.cdfg.custdept.dao;

import com.cdfg.custdept.pojo.dto.Custdeptlist;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustaddrlistDao {

    int insert(List<Custdeptlist> record);

}