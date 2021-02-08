package com.cdfg.custdept.controller;

import cn.cdfg.exceptionHandle.ExceptionPrintMessage;
import cn.cdfg.exceptionHandle.CustDeptNotFoundException;
import com.alibaba.fastjson.JSONObject;
import com.cdfg.custdept.pojo.dto.Postvercode;
import com.cdfg.custdept.pojo.dto.WechatCode;
import com.cdfg.custdept.pojo.until.*;
import com.cdfg.custdept.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.cdfg.custdept.pojo.until.Constant.*;

;
/*
 * project name :自助邮寄
 * for:用户注册
 * author：wangc
 * time：2020-10-10
 * */

@Controller
@RequestMapping("/cdfg")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService registerservice=null;

    Logger logger = Logger.getLogger(UserController.class);

    /**
     * 注册接口
     * @param reguser
     * @return
     */
    @PostMapping("/getregister")
    @ResponseBody
    public Result<String> cdfgdeposit(@RequestBody RegUser reguser) {

        String id_type = reguser.getId_type();//证件类型
        String card_id = reguser.getCard_id();//证件号
        String tel_num = reguser.getTel_num();//手机号
        String open_id = reguser.getOpen_id();//密码

        logger.info("取到注册用户信息"+id_type+card_id+tel_num+open_id);

        Map<String,String> param=new HashMap<String,String>();
        param.put("id_type", id_type);
        param.put("card_id", card_id);
        param.put("tel_num", tel_num);
        param.put("open_id", open_id);

        try {
            registerservice.getCoupon(param);
        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("获取注册用户信息存储过程的返回值异常");
            throw new CustDeptNotFoundException(errCode,errMsg);
        }
        int code = Integer.parseInt(param.get("ret_flag")) ;
        //写入日志
        logger.info("ret_flag（返回标志）:"+ param.get("ret_flag"));
        if ("1".equals(param.get("ret_flag"))) {
            return new Result<String>(code, LoginErrCode.getMsg(param.get("ret_flag")), null);
        }else {
            throw new CustDeptNotFoundException(code,LoginErrCode.getMsg(param.get("ret_flag")));
        }
    }

    /**
     * 登录接口
     * @param login
     * @return
     */
    @PostMapping("/getlogin")
    @ResponseBody
    public Result<Map> login(@RequestBody Login login) {

        String open_id = login.getOpen_id();//密码

        Map<String,String> retparam=new HashMap<String,String>();

        logger.info("取到登录用户信息"+open_id);

        Map<String,String> param=new HashMap<String,String>();
        param.put("open_id", open_id);

        String user_name = null;
        String card_id = null;
        String ret_flag;
        String tel_num = null;
        try {
            registerservice.login(param);
            //获得返回值
            ret_flag=param.get("ret_flag");
        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("取到登录用户信息存储过程的返回值异常");
            throw new CustDeptNotFoundException(errCode9,errMsg9);
        }

        if ("2002".equals(ret_flag)) {
            logger.error("未注册，无记录");
            throw new CustDeptNotFoundException(errCode7,errMsg7);
        }else {
            retparam.put("card_id",param.get("card_id"));
            retparam.put("tel_num",param.get("tel_num"));
            retparam.put("user_name",param.get("user_name"));
            retparam.put("id_type",param.get("id_type"));
        }

        logger.info("获取登录信息返回标志："+ret_flag+"用户名："+user_name+"身份证："+card_id+"电话："+tel_num);
        return new Result<Map>(sucCode,sucMsg,retparam);
    }

    /**
     * 接收验收码接口
     * @param vercode
     * @return
     */
    @PostMapping("/insertvercode")
    @ResponseBody
    public Result<String> cdfgdeposit(@RequestBody Vercode vercode) {

        int ret_flag;
        String tel_num = vercode.getTel_num();//取到电话代码
        String open_Id = vercode.getOpenId();
        String gwkh = vercode.getCardType()+vercode.getCardId();
        //生成一个6位数的随机数
        String code = String.valueOf((Math.random()*9+1)*100000);
        int idx = code.lastIndexOf(".");
        String ver_code = code.substring(0,idx);

        try{
            Postvercode pv = new Postvercode();
            pv.setGwkh(gwkh);
            pv.setOpenid(open_Id);
            pv.setTelnum(tel_num);
            pv.setVercode(ver_code);

            logger.info("获取到客人电话号码："+tel_num+" 验证码："+ver_code);
            ret_flag = registerservice.insert(pv);

            if (ret_flag == 0) {
                logger.info(tel_num+"验证码写入返回值异常");
            }
            //写入日志
            logger.info("ret_flag（返回标志）:"+ret_flag);
        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("生成验证码后写入表异常");
            throw new CustDeptNotFoundException(errCode11,errMsg11);
        }
        return new Result<String>(sucCode,sucMsg,"");
    }

    /**
     * @param code
     * @return
     */
    @PostMapping("/getvercode")
    @ResponseBody
    public Result<String> getVercode(@RequestBody Vercode code) {
        Postvercode pvcode;
        String tel_num = code.getTel_num();//取到电话代码
        String gwkh = code.getCardType()+code.getCardId();
        String userCode = code.getVerCode();

        Map param=new HashMap<String,String>();
        param.put("tel_num", tel_num);
        param.put("gwkh", gwkh);

        try {
            pvcode = registerservice.selectByPrimaryKey(param);
        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("验证码获取存返回值异常");
            throw new CustDeptNotFoundException(errCode17,errMsg17);
        }
        if (pvcode == null) {
            logger.error("该手机未获取验证码");
            throw new CustDeptNotFoundException(errCode18,errMsg18);
        }
        String orlCode = pvcode.getVercode();
        Date fsTime = pvcode.getFstime();

        Calendar dateOne = Calendar.getInstance();
        Calendar dateTwo = Calendar.getInstance();

        dateOne.setTime(new Date());
        dateTwo.setTime(fsTime);

        long timeOne=dateOne.getTimeInMillis();
        long timeTwo=dateTwo.getTimeInMillis();

        long minute=(timeOne-timeTwo)/(1000*60);//转化minute
        if (minute > 5) {
            logger.info("验证码已过期");
            throw new CustDeptNotFoundException(errCode15,errMsg15);
        }else if (userCode.equals(orlCode)) {
            return new Result<String>(sucCode,sucMsg,"");
        }else {
            logger.info("验证码错误");
            throw new CustDeptNotFoundException(errCode16,errMsg16);
        }
    }
    /**
     * 获取Openid接口
     * @param code
     * @return
     */
//    @PostMapping("/getopenid")
    @RequestMapping(value={"/getopenid"}, method = RequestMethod.POST)
    @ResponseBody
    public Result<Map> wechatopenid(@RequestBody WechatCode code) {
        if (code == null) {
            logger.error("传入的对象值为null");
            throw new CustDeptNotFoundException(errCode19,errMsg19);
        }
        Map<String,String> retparam=new HashMap<String,String>();
        String vercode = code.getVer_code();
        logger.info("取到code"+vercode);

        JSONObject jsonObject;
        jsonObject = AuthUtil.doGetJson(vercode);

        //从返回的JSON数据中取出access_token和openid，拉取用户信息时用
        if (jsonObject.containsKey("access_token")) {
            String token =  jsonObject.getString("access_token");
            String openId = jsonObject.getString("openid");
            logger.info("token:"+token+";"+"openid:"+openId);

            String ret = WxtxUtil.getWxtx(openId,token);

            //获取到客人信息
            Map<String,String> param=new HashMap<String,String>();
            param.put("open_id", openId);

            retparam.put("open_id",openId);
            retparam.put("access_token",token);
            retparam.put("userinfo",ret);
            logger.info("获取到客人openid："+openId+token+ret);
        }else {
            String errcode =  jsonObject.getString("errcode");
            String errmsg = jsonObject.getString("errmsg");

            logger.info("errcode:"+errcode+";"+"errmsg:"+errmsg);
            logger.info("ret_result（返回结果）:"+"errcode:"+errcode+"errmsg:"+errmsg);
            throw new CustDeptNotFoundException(errCode8,errMsg8);
        }
        return new Result<Map>(sucCode,sucMsg,retparam);
    }

    @RequestMapping(value={"/qryjczg"}, method = RequestMethod.POST)
    @ResponseBody
    public Result<String> qryjczg(@RequestBody Login login) {
        String open_id = login.getOpen_id();//密码
        Map<String,String> retparam=new HashMap<String,String>();
        logger.info("取到登录用户信息"+open_id);
        Map<String,String> param=new HashMap<String,String>();
        param.put("open_id", open_id);

        String user_name;
        String card_id;
        String ret_flag;
        String tel_num;
        String id_type;
        try {
            registerservice.login(param);
            //获得返回值
            ret_flag=param.get("ret_flag");
        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("取到登录用户信息存储过程的返回值异常");
            throw new CustDeptNotFoundException(errCode9,errMsg9);
        }
        if ("2002".equals(ret_flag)) {
            logger.error("未注册，无记录");
            throw new CustDeptNotFoundException(errCode7,errMsg7);
        }else {
            String isdm = param.get("isdm");
            card_id = param.get("card_id");
            tel_num = param.get("tel_num");
            user_name = param.get("user_name");
            id_type = param.get("id_type");

            if ("Y".equals(isdm)) {
                retparam.put("card_id",card_id);
                retparam.put("tel_num",tel_num);
                retparam.put("user_name",user_name);
                retparam.put("id_type",id_type);
            }else {
                //调用海关接口，查询客人是否岛民
                HttpClientUtil htUtil = new HttpClientUtil();
                TableInfo tinfo = new TableInfo();
                tinfo.setBuyerName(user_name);
                tinfo.setIdcardNo(card_id);
                tinfo.setIdcardType(id_type);

                JSONObject jsonObject = htUtil.doPost(tinfo);
                String status =  jsonObject.getString("status");
                String data = jsonObject.getString("data");
                String msg = jsonObject.getString("msg");
                logger.info("获取海关岛民身份接口返回值"+status+data+msg+card_id);
                logger.info("获取岛内居民信息返回标志："+ret_flag+"用户名："+user_name+"身份证："+card_id+"电话："+tel_num);
                if("error".equals(status)) {
                    logger.error("海关验证岛民身份失败");
                    throw new CustDeptNotFoundException(errCode,"海关验证身份接口返回失败");
                }else {
                    if ("true".equals(data)) {
                        logger.error(user_name+card_id+"是岛内居民，可以申请办理寄存业务");
                        return new Result<String>(sucCode,sucMsg,"true");
                    }else {
                        logger.error(user_name+card_id+"非岛内居民，不能申请办理寄存业务");
                        return new Result<String>(sucCode,sucMsg,"false");
                    }
                }
            }
        }
        logger.info("获取岛内居民信息返回标志："+ret_flag+"用户名："+user_name+"身份证："+card_id+"电话："+tel_num);
        return new Result<String>(sucCode,sucMsg,"true");
    }
}
