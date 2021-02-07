package com.cdfg.custdept.service;

import com.cdfg.custdept.pojo.dto.Jcyysjinfo;
import com.cdfg.custdept.pojo.dto.YysjDto;
import com.cdfg.custdept.pojo.until.Login;

public interface PostAddressService {
    Jcyysjinfo qryPostAddress(Login login);

    int insertPostAddress(YysjDto ipaDto);

    int updatePostAddress(Jcyysjinfo ipaDto);
}
