package com.cdfg.custdept.service.Impl;

import cn.cdfg.exceptionHandle.CustDeptNotFoundException;
import com.cdfg.custdept.dao.HoldByDateDao;
import com.cdfg.custdept.pojo.dto.HoldByDateDto;
import com.cdfg.custdept.pojo.until.JcXsdbillEntity;
import com.cdfg.custdept.service.HoldByDateService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import static com.cdfg.custdept.pojo.until.Constant.*;

@Service
public class HoldByDateServiceImpl implements HoldByDateService {
    @Autowired
    HoldByDateDao hbdDao = null;
    Logger logger = Logger.getLogger(HoldByDateServiceImpl.class);

    @Override
    public List<JcXsdbillEntity> qryCheckBill(HoldByDateDto hbdDto) {
        if (hbdDto == null) {
            throw new CustDeptNotFoundException(errCode_5, errMsg_5);
        }
        Date yysj = hbdDto.getYysj();
        String market = hbdDto.getMarket();
        String qhdd = hbdDto.getQhdd();

        Map param = new HashMap();
        param.put("yysj",yysj);
        param.put("market",market);
        param.put("qhdd",qhdd);
        List<JcXsdbillEntity> beyList;
        try {
            hbdDao.qryCheckBill(param);
            //取出结果集
            beyList = (List<JcXsdbillEntity>) param.get("zcRc");

        } catch (Exception e) {
//            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("表数据查询返回值异常");
            throw new CustDeptNotFoundException(errCode_3, errMsg_3);
        }

        for (int i = 0; i <beyList.size() ; i++) {
            JcXsdbillEntity jxEntity = new JcXsdbillEntity();
            logger.info("取到寄存提货单查询返回值"+jxEntity.getIsth()+jxEntity.getMarket()
            +jxEntity.getQhdd()+jxEntity.getYysj()+jxEntity.getXsdno());
        }
        return beyList;
    }

    @Override
    public String insertDts(HoldByDateDto hbdDto,String worknumber) {
        if (hbdDto == null) {
            throw new CustDeptNotFoundException(errCode_5, errMsg_5);
        }
        Map param  = new HashMap<String,String>();
        param.put("operator",worknumber);
        param.put("xsdno",hbdDto.getXsdno());
        param.put("market",hbdDto.getMarket());
        param.put("yysj",hbdDto.getYysj());
        param.put("tmpCode",hbdDto.getTmpCode());
        try {
            hbdDao.insertDts(param);
        } catch (Exception e) {
//            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("邮寄日期暂存写入异常");
            throw new CustDeptNotFoundException(errCode_3, errMsg_3);
        }
        String tmpCode = (String) param.get("o_TmpCode");
        if (tmpCode == "") {
            logger.error("寄存日期暂存写入异常");
            throw new CustDeptNotFoundException(errCode, errMsg);
        }else {
            return tmpCode;
        }
    }
}
