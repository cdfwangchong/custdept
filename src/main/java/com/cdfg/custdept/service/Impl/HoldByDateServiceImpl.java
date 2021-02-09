package com.cdfg.custdept.service.Impl;

import cn.cdfg.exceptionHandle.CustDeptNotFoundException;
import cn.cdfg.exceptionHandle.ExceptionPrintMessage;
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
        Map param = new HashMap();
        param.put("yysj",hbdDto.getYysj());
        param.put("market",hbdDto.getMarket());
        param.put("qhdd",hbdDto.getQhdd());
        param.put("zctype",hbdDto.getZctype());
        List<JcXsdbillEntity> beyList;
        try {
            hbdDao.qryCheckBill(param);
            //取出结果集
            beyList = (List<JcXsdbillEntity>) param.get("zcRc");

        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
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
        param.put("qhdd",hbdDto.getQhdd());
        param.put("tmpCode",hbdDto.getTmpCode());
        try {
            hbdDao.insertDts(param);
        } catch (Exception e) {
//            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("邮寄日期暂存写入异常");
            throw new CustDeptNotFoundException(errCode_3, errMsg_3);
        }
        String tmpCode = (String) param.get("o_TmpCode");
        String billno = (String) param.get("o_billno");
        String isth = (String) param.get("o_isth");

        if (tmpCode == "") {
            logger.error("寄存日期暂存写入异常");
            throw new CustDeptNotFoundException(errCode, errMsg);
        }else {
            return tmpCode;
        }
    }
}
