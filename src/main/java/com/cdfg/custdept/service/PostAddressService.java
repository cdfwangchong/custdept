package com.cdfg.custdept.service;

import com.cdfg.custdept.pojo.dto.Jcyysjinfo;
import com.cdfg.custdept.pojo.dto.YysjDto;
import com.cdfg.custdept.pojo.until.Login;

import java.util.Map;

public interface PostAddressService {
    Jcyysjinfo qryPostAddress(Login login);

    Map insertPostAddress(YysjDto ipaDto);

    int updatePostAddress(Jcyysjinfo ipaDto);
}
