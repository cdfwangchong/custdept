package com.cdfg.custdept.service.Impl;

import cn.cdfg.exceptionHandle.ExceptionPrintMessage;
import cn.cdfg.exceptionHandle.CustDeptNotFoundException;
import com.cdfg.custdept.dao.HoldByDateDao;
import com.cdfg.custdept.pojo.dto.HoldByDateDto;
import com.cdfg.custdept.pojo.until.SellHeadEntity;
import com.cdfg.custdept.service.HoldByDateService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.cdfg.custdept.pojo.until.Constant.*;

@Service
public class HoldByDateServiceImpl implements HoldByDateService {
    @Autowired
    HoldByDateDao hbdDao = null;
    Logger logger = Logger.getLogger(HoldByDateServiceImpl.class);

    @Override
    public String qryCheckBill(HoldByDateDto hbdDto) {
        if (hbdDto == null) {
            throw new CustDeptNotFoundException(errCode_5, errMsg_5);
        }
        Date ldrq = hbdDto.getLdrq();
        String market = hbdDto.getMarket();
        String billNO = hbdDto.getBillNO();
        SellHeadEntity shEntity;
        try {
            shEntity = hbdDao.qryCheckBill(billNO);
        } catch (Exception e) {
//            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("表数据查询返回值异常");
            throw new CustDeptNotFoundException(errCode_3, errMsg_3);
        }

        if (shEntity == null) {
            logger.error("提货单："+billNO+"不存在");
            throw new CustDeptNotFoundException(errCode_13, errMsg_8+billNO+errMsg_13);
        }
        String orlMarket = shEntity.getMARKET();
        Date orlDpttime = shEntity.getSHDPTTIME();
        String orlStatus = shEntity.getSHSTATUS();
        String orlIsth = shEntity.getSHISTH();
        String orlThfs = shEntity.getSHTHFS();
        String xsdno = shEntity.getSHXSDNO();

        Calendar dateOne = Calendar.getInstance();
        Calendar dateTwo = Calendar.getInstance();

        dateOne.setTime(ldrq);
        dateTwo.setTime(orlDpttime);

        long timeOne=dateOne.getTimeInMillis();
        long timeTwo=dateTwo.getTimeInMillis();

        long dates=(timeOne-timeTwo)/(1000*60*60*24);//转化天数

         if ("Y".equals(orlIsth)) {//是否退货
            logger.info("提货单："+billNO+"已经退货");
            throw new CustDeptNotFoundException(errCode_10, errMsg_8+billNO+errMsg_10);
        } else if (!"J".equals(orlThfs)) {//提货方式：邮寄提货
            logger.info("提货单："+billNO+"提货方式不是【邮寄提货】");
            throw new CustDeptNotFoundException(errCode_11, errMsg_8+billNO+errMsg_11);
        } else if (!"X".equals(orlStatus)) {//提货状态：待邮寄
            logger.info("提货单："+billNO+"提货状态不是【待邮寄】");
            throw new CustDeptNotFoundException(errCode_12, errMsg_8+billNO+errMsg_12);
        } else if (dates != 0) {//离岛日期
            logger.info("提货单："+billNO+"离岛日期与输入日期不一致");
            throw new CustDeptNotFoundException(errCode_9, errMsg_8+billNO+errMsg_9);
        } else {
            return xsdno;
        }
    }

    @Override
    public String insertDts(HoldByDateDto hbdDto,String worknumber) {
        if (hbdDto == null) {
            throw new CustDeptNotFoundException(errCode_5, errMsg_5);
        }
        Map param  = new HashMap<String,String>();
        param.put("operator",worknumber);
        param.put("billNO",hbdDto.getBillNO());
        param.put("market",hbdDto.getMarket());
        param.put("ldrq",hbdDto.getLdrq());
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
            logger.error("邮寄日期暂存存储过程写入异常");
            throw new CustDeptNotFoundException(errCode, errMsg);
        }else {
            return tmpCode;
        }
    }
}
