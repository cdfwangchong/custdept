package com.cdfg.custdept.controller;

import cn.cdfg.exceptionHandle.CustDeptNotFoundException;
import com.cdfg.custdept.pojo.dto.Jcyysjinfo;
import com.cdfg.custdept.pojo.dto.YysjDto;
import com.cdfg.custdept.pojo.until.Login;
import com.cdfg.custdept.pojo.until.Result;
import com.cdfg.custdept.service.PostAddressService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.cdfg.custdept.pojo.until.Constant.*;

/*
 * project name :自助邮寄
 * for:收货地址管理和新增接口
 * author：wangc
 * time：2020-10-10
 * */
@CrossOrigin
@RestController
@RequestMapping("/cdfg")
public class PostAddressController {
    @Autowired
    PostAddressService paService = null;
    Logger logger = Logger.getLogger(PostAddressController.class);

    /**
     * 收货地址管理接口
     * @param login
     * @return
     */
    @PostMapping("qryyyinfo")
    @ResponseBody
    public Result<Jcyysjinfo> qryPostAddress(@RequestBody Login login) {
        Jcyysjinfo pd = paService.qryPostAddress(login);
        logger.info("取到收货地址管理接口的传入参数"+login.getOpen_id());

        return new Result<Jcyysjinfo>(sucCode,sucMsg,pd);
    }

    /**
     * 收货地址新增接口
     * @param paDto
     * @return
     */
    @PostMapping("/insertyyinfo")
    @ResponseBody
    public Result<String> insertPostAddress(@RequestBody YysjDto paDto) {

        logger.info("取到寄存新增预约信息接口的传入参数"+paDto.getOpen_id()+"详细地址："+paDto.getQhdd()+paDto.getYysj());
        int rs = paService.insertPostAddress(paDto);
        if(rs == 1) {
            return new Result<String>(sucCode,sucMsg,"");
        }else {
            throw new CustDeptNotFoundException(errCode,"新增预约信息失败");
        }
    }

    @PostMapping("updateyysinfo")
    @ResponseBody
    public Result<String> updatePostAddress(@RequestBody Jcyysjinfo paDto) {

        logger.info("取到寄存新增预约信息接口的传入参数"+paDto.getOpen_id()+"详细地址："+paDto.getQhdd()+paDto.getYysj());
        int rs = paService.updatePostAddress(paDto);
        if (rs == 1) {
            return new Result<String>(sucCode,sucMsg,"");
        }else {
            throw new CustDeptNotFoundException(errCode,"更新预约信息失败");
        }
    }
}
