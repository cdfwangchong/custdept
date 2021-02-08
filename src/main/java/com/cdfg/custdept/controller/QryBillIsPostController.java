package com.cdfg.custdept.controller;

import cn.cdfg.exceptionHandle.CustDeptNotFoundException;
import com.cdfg.custdept.pojo.dto.XsdnoDto;
import com.cdfg.custdept.pojo.until.BillEntity;
import com.cdfg.custdept.pojo.until.CustDeptlistDetEntity;
import com.cdfg.custdept.pojo.until.Login;
import com.cdfg.custdept.pojo.until.Result;
import com.cdfg.custdept.service.QryBillIsPostService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cdfg.custdept.pojo.until.Constant.*;

/*
 * project name :自助邮寄
 * for:未邮寄提货单接口和已经邮寄的提货单接口
 * author：wangc
 * time：2020-10-10
 * */
@CrossOrigin
@RestController
@RequestMapping("/cdfg")
public class QryBillIsPostController {

    @Autowired
    private QryBillIsPostService qbipService=null;
    Logger logger = Logger.getLogger(QryBillIsPostController.class);

    /**
     * 未邮寄提货单接口
     * @param login
     * @return
     */
    @PostMapping("/qrynotdeptbill")
    @ResponseBody
    public Result<XsdnoDto> qryNotPostBill(@RequestBody Login login) {
        XsdnoDto xsdnoDto;

        if (login == null){
            logger.error("寄存提货单查询接口传入的参数值为null");
            throw new CustDeptNotFoundException(errCode5,errMsg5);
        }
        xsdnoDto = qbipService.qryNotPostBill(login);
        for (int i = 0; i < xsdnoDto.getOrderList().size(); i++) {
            BillEntity be = xsdnoDto.getOrderList().get(i);
            logger.info("取到未寄存提货单接口返回值："+be.getMarket()+"#"+be.getXsdno()+"#"+be.getShoughtpay());
        }
        return new Result<XsdnoDto>(sucCode,sucMsg,xsdnoDto);
    }

    /**
     * 邮寄提货单查询接口
     * @param login
     * @return
     */
    @PostMapping("/qrydepttbill")
    @ResponseBody
    public Result<List<CustDeptlistDetEntity>> qryPostBill(@RequestBody Login login) {
        List<CustDeptlistDetEntity> beList;

        if (login == null){
            logger.error("邮寄提货单查询接口传入的参数值为null");
            throw new CustDeptNotFoundException(errCode5,errMsg5);
        }
        beList = qbipService.qryPostBill(login);
        for (int i = 0; i < beList.size(); i++) {
            CustDeptlistDetEntity be;
            be = beList.get(i);
            logger.info("取到邮寄提货单接口返回值："+be.getXsdno()+be.getYyseq()+be.getQhdd()+be.getMarket());
        }

        return new Result<List<CustDeptlistDetEntity>>(sucCode,sucMsg,beList);
    }
}
