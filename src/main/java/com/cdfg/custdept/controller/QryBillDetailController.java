package com.cdfg.custdept.controller;

import cn.cdfg.exceptionHandle.ExceptionPrintMessage;
import cn.cdfg.exceptionHandle.CustDeptNotFoundException;
import com.cdfg.custdept.pojo.dto.PickBillDto;
import com.cdfg.custdept.pojo.dto.PickNumDto;
import com.cdfg.custdept.pojo.until.Result;
import com.cdfg.custdept.service.QryBillDetailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cdfg.custdept.pojo.until.Constant.*;

/*
* project name :自助邮寄
 * for:提货单明细接口
 * author：wangc
 * time：2020-10-10
 * */
@CrossOrigin
@RestController
@RequestMapping("/cdfg")
public class QryBillDetailController {

    @Autowired
    private QryBillDetailService selldetailService=null;
    Logger logger = Logger.getLogger(QryBillDetailController.class);

    @PostMapping("/qryselldetail")
    @ResponseBody
    public Result<List<PickBillDto>> qryselldetail(@RequestBody PickNumDto picknumdto) {
        String pick_num = picknumdto.getXsdno();//提货单号

        int resultCode = 0;
        String msg = null;
        List<PickBillDto> orderItem;

        logger.info("提货单明细接口收到的数据："+pick_num);
        try{
            orderItem = selldetailService.getselldetail(picknumdto);
            if (orderItem != null) {
                resultCode = sucCode;
                msg = sucMsg;
            }
            for (int i = 0; i < orderItem.size(); i++) {
                PickBillDto pbd = orderItem.get(i);
                pbd.getCount();
                pbd.getGb_name();
                pbd.getGbid();
                pbd.getTrans_amount();
                logger.info("取到提货单明细接口返回值："+"商品数量："+pbd.getCount()+
                        "商品名称："+pbd.getGb_name()+"商品编码："+pbd.getGbid()+"金额："+pbd.getTrans_amount());
            }
        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("提货单明细接口返回值异常");
            throw new CustDeptNotFoundException(errCode,errMsg);
        }
        return new Result<List<PickBillDto>>(resultCode,msg,orderItem);
    }
}
