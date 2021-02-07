package com.cdfg.custdept.service.Impl;

import cn.cdfg.exceptionHandle.ExceptionPrintMessage;
import cn.cdfg.exceptionHandle.SelfMailNotFoundException;
import com.cdfg.custdept.dao.QryBillIsPostDao;
import com.cdfg.custdept.pojo.dto.XsdnoDto;
import com.cdfg.custdept.pojo.until.BillEntity;
import com.cdfg.custdept.pojo.until.CustAddrlistEntity;
import com.cdfg.custdept.pojo.until.Login;
import com.cdfg.custdept.service.QryBillIsPostService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cdfg.custdept.pojo.until.Constant.*;

@Service
public class QryBillIsPostServiceImpl implements QryBillIsPostService {
    @Autowired
    private QryBillIsPostDao qbipDao=null;
    Logger logger = Logger.getLogger(QryBillIsPostServiceImpl.class);

    /**
     * 未寄存提货单接口
     * @param login
     * @return
     */
    @Override
    public XsdnoDto qryNotPostBill(Login login) {
        List<BillEntity> beList;
        String ret_card;
        String ret_name;
        Map param = new HashMap<String,String>();
        XsdnoDto xsdnoDto = new XsdnoDto();

        try {
            param.put("openId",login.getOpen_id());
            qbipDao.qryNotPostBill(param);
            //取出结果集
            beList = (List<BillEntity>) param.get("wyjRc");
            ret_name = (String)param.get("ret_name");
            ret_card = (String)param.get("ret_card");

            xsdnoDto.setOrderList(beList);
            xsdnoDto.setRet_card(ret_card);
            xsdnoDto.setRet_name(ret_name);
        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("查找未邮寄的提货单存储过程返回值异常");
            throw new SelfMailNotFoundException(errCode,errMsg);
        }
        //取出ret_flag
        String ret_flag = (String) param.get("ret_flag");
        if ("0".equals(ret_flag)) {
            logger.error("该日期使用邮寄提货的人数已经满");
            throw new SelfMailNotFoundException(errCode6,errMsg6);
        }
        return xsdnoDto;
    }

    /**
     * 寄存提货单查询接口
     * @param login
     * @return
     */
    @Override
    public List<CustAddrlistEntity> qryPostBill(Login login) {
        Map param = new HashMap<String,String>();
        param.put("openId",login.getOpen_id());
        List<CustAddrlistEntity> beyList;

        try {
            qbipDao.qryPostBill(param);

            //取出结果集
            beyList = (List<CustAddrlistEntity>) param.get("yjRc");
        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("查找已邮寄的提货单存储过程返回值异常");
            throw new SelfMailNotFoundException(errCode,errMsg);
        }
        return beyList;
    }
}
