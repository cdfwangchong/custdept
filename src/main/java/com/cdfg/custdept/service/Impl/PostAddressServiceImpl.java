package com.cdfg.custdept.service.Impl;

import cn.cdfg.exceptionHandle.ExceptionPrintMessage;
import cn.cdfg.exceptionHandle.SelfMailNotFoundException;
import com.cdfg.custdept.dao.PostaddressDao;
import com.cdfg.custdept.dao.UserlistDao;
import com.cdfg.custdept.pojo.dto.Jcyysjinfo;
import com.cdfg.custdept.pojo.dto.Userlist;
import com.cdfg.custdept.pojo.dto.YysjDto;
import com.cdfg.custdept.pojo.until.Login;
import com.cdfg.custdept.service.PostAddressService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.cdfg.custdept.pojo.until.Constant.*;

@Service
public class PostAddressServiceImpl implements PostAddressService {

    @Autowired
    PostaddressDao paDao;

    @Autowired
    UserlistDao ulDao;

    Logger logger = Logger.getLogger(PostAddressServiceImpl.class);

    /**
     * 查询邮寄地址
     * @param login
     * @return
     */
    @Override
    public Jcyysjinfo qryPostAddress(Login login) {
        //查出顾客的购物卡号
        Userlist ul = ulDao.selectByPrimaryKey(login.getOpen_id());
        String gwkh = ul.getIdseq();//客人的购物卡号
        logger.info("获取顾客地址前查出客人购物卡号"+gwkh);

        Jcyysjinfo paDto;
        try {
            paDto = paDao.selectByPrimaryKey(gwkh);

            if (paDto != null) {
                logger.info("取到顾客"+ul.getName()+"的预约信息"+paDto.getQhdd()+
                        paDto.getYysj()+paDto.getYyseq());
            }else {
                logger.error("获取到的对象值为空");
                throw new SelfMailNotFoundException(errCode19,errMsg19);
            }
        } catch (Exception e) {
            logger.error(gwkh+"无预约信息");
            throw new SelfMailNotFoundException(errCode14,errMsg);
        }
        return paDto;
    }

    /**
     * 新增邮寄地址
     * @param ipdDto
     * @return
     */
    @Override
    public int insertPostAddress(YysjDto ipdDto) {
        //查出顾客的购物卡号
        Userlist ul = ulDao.selectByPrimaryKey(ipdDto.getOpen_id());
        ipdDto.setGwkh(ul.getIdseq());
        logger.info("新增预约信息前查出客人购物卡号"+ul.getIdseq());
        int result = 0;
        Map param = new HashMap<String,String>();
        try {
            param.put("gwkh",ipdDto.getGwkh());
            param.put("yysj",ipdDto.getYysj());
            param.put("qhdd",ipdDto.getQhdd());

            paDao.insert(param);
            if (param.get("ret_flag").equals("1000")) {
                logger.info("顾客"+ul.getName()+"预约信息新增成功");
                result = 1;
            }
            if (param.get("ret_flag").equals("1002")) {
                logger.info("顾客"+ul.getName()+"有未取货的寄存商品，不可以重新预约");
                throw new SelfMailNotFoundException(errCode,"顾客"+ul.getName()+"有未取货的寄存商品，不可以重新预约");
            }
            if (param.get("ret_flag").equals("1003")) {
                logger.info("顾客"+ul.getName()+"新增预约信息异常");
                throw new SelfMailNotFoundException(errCode,"顾客"+ul.getName()+"新增预约信息异常");
            }
            if (param.get("ret_flag").equals("1004")) {
                logger.info("预约第二天寄存取货，要在16:00前");
                throw new SelfMailNotFoundException(errCode,"预约第二天寄存取货，要在16:00前");
            }
        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("预约信息写入异常");
            throw new SelfMailNotFoundException(errCode,errMsg);
        }
        return result;
    }

    @Override
    public int updatePostAddress(Jcyysjinfo ipaDto) {

        //查出顾客的购物卡号
        Userlist ul = ulDao.selectByPrimaryKey(ipaDto.getOpen_id());
        ipaDto.setGwkh(ul.getIdseq());
        logger.info("更新预约信息前查出客人购物卡号"+ul.getIdseq());
        int result=0;
        Map param = new HashMap<String,String>();
        try {
            param.put("yyseq",ipaDto.getYyseq());
            param.put("yysj",ipaDto.getYysj());
            param.put("qhdd",ipaDto.getQhdd());
            param.put("preyysj",ipaDto.getPreyysj());
            param.put("preqhdd",ipaDto.getPreqhdd());

            paDao.updateByPrimaryKey(param);
            if (param.get("ret_flag").equals("1000")) {
                logger.info("顾客"+ul.getName()+"预约信息新增成功");
                result = 1;
            }
            if (param.get("ret_flag").equals("1001")) {
                logger.info("顾客"+ul.getName()+"预约信息已经被修改过，不可以多次更新");
                throw new SelfMailNotFoundException(errCode,"预约信息已经被修改过，不可以多次更新");
            }
            if (param.get("ret_flag").equals("1002")) {
                logger.info("顾客"+ul.getName()+"预约信息不存在");
                throw new SelfMailNotFoundException(errCode,"顾客"+ul.getName()+"预约信息不存在");
            }
            if (param.get("ret_flag").equals("1003")) {
                logger.info("顾客"+ul.getName()+"更新预约信息写入表异常");
                throw new SelfMailNotFoundException(errCode,"顾客"+ul.getName()+"更新预约信息写入表异常");
            }
            if (param.get("ret_flag").equals("1004")) {
                logger.info("取货时间小于48内的预约申请，要在16:00前提交");
                throw new SelfMailNotFoundException(errCode,"取货时间小于48内的预约申请，要在16:00前提交");
            }
        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("邮寄地址管理表更新异常");
            throw new SelfMailNotFoundException(errCode,errMsg);
        }
        return result;
    }
}
