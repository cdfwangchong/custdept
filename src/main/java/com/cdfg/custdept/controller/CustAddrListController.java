package com.cdfg.custdept.controller;

import cn.cdfg.exceptionHandle.SelfMailNotFoundException;
import com.cdfg.custdept.pojo.dto.InsertCustAddrAndListDto;
import com.cdfg.custdept.pojo.until.BillEntity;
import com.cdfg.custdept.pojo.until.Result;
import com.cdfg.custdept.service.CustAddrListService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.cdfg.custdept.pojo.until.Constant.*;

/*
 * project name :自助邮寄
 * for:商品邮寄接口
 * author：wangc
 * time：2020-10-10
 *
 * */
@CrossOrigin
@RestController
@RequestMapping("/cdfg")
public class CustAddrListController {
    @Autowired
    CustAddrListService calService = null;
    Logger logger = Logger.getLogger(CustAddrListController.class);

    @PostMapping("insertCustAddr")
    @ResponseBody
    public Result<String> insertCustAddrList(@RequestBody InsertCustAddrAndListDto ica){
        for (int i = 0; i < ica.getOrderList().size(); i++) {
            BillEntity pi = ica.getOrderList().get(i);
            logger.info("取到要寄存的提货单："+pi.getXsdno()+"门店："+pi.getMarket());
        }
        boolean bl = calService.insertCustAddrList(ica);
        if (bl) {
            return new Result<String>(sucCode,sucMsg,"");
        }else {
            throw new SelfMailNotFoundException(errCode3,errMsg3);
        }
    }
}
