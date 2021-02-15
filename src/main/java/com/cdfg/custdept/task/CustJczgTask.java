package com.cdfg.custdept.task;

import cn.cdfg.exceptionHandle.ExceptionPrintMessage;
import com.alibaba.fastjson.JSONObject;
import com.cdfg.custdept.controller.HoldByDateController;
import com.cdfg.custdept.dao.CustJczgDao;
import com.cdfg.custdept.pojo.dto.CustJczgDto;
import com.cdfg.custdept.pojo.dto.DmzgDto;
import com.cdfg.custdept.pojo.until.HttpClientUtil;
import com.cdfg.custdept.pojo.until.TableInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustJczgTask {
    @Autowired
    CustJczgDao custJczgdao;

    Logger logger = Logger.getLogger(HoldByDateController.class);

    /**
     * 定时任务-获取顾客寄存资格
     * @return
     */
    @Scheduled(cron = "* */5 * * * *")
    public void qryJczgTask() {
        List<CustJczgDto> cjlist = null;
        List<DmzgDto> dmzglist = new ArrayList<DmzgDto>();
        try {
            Map param = new HashMap();
            custJczgdao.QryCustJczg(param);
            cjlist = (List<CustJczgDto>) param.get("jcRc");
        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("员工号不存在或者密码错误");
        }
        for (int i = 0; i < cjlist.size(); i++) {
            CustJczgDto cjDto = cjlist.get(i);

            String user_name = cjDto.getGmname();
            String id_type = cjDto.getGmzjlb();
            String card_id = cjDto.getGmzjno();
            //调用海关接口，查询客人是否岛民
            HttpClientUtil htUtil = new HttpClientUtil();
            TableInfo tinfo = new TableInfo();
            tinfo.setBuyerName(user_name);
            tinfo.setIdcardNo(card_id);
            tinfo.setIdcardType(id_type);

            JSONObject jsonObject = htUtil.doPost(tinfo);
            String status = jsonObject.getString("status");
            String data = jsonObject.getString("data");
            String msg = jsonObject.getString("msg");
            logger.info("获取海关岛民身份接口返回值" + status + data + msg + card_id);
            logger.info("获取岛内居民信息返回标志：" + "用户名：" + user_name + "身份证：" + card_id);
            DmzgDto dmzgdto = new DmzgDto();
            if ("error".equals(status)) {
                logger.error("海关验证岛民身份失败");
                //throw new CustDeptNotFoundException(errCode, "海关验证身份接口返回失败");
            } else {
                if ("true".equals(data)) {
                    dmzgdto.setIsdm("Y");
                    dmzgdto.setGmname(user_name);
                    dmzgdto.setGmzjlb(id_type);
                    dmzgdto.setGmzjno(card_id);
                    logger.error(user_name + card_id + "是岛内居民，可以申请办理寄存业务");
                    //return new Result<String>(sucCode, sucMsg, "true");
                } else {
                    dmzgdto.setIsdm("N");
                    dmzgdto.setGmname(user_name);
                    dmzgdto.setGmzjlb(id_type);
                    dmzgdto.setGmzjno(card_id);
                    logger.error(user_name + card_id + "非岛内居民，不能申请办理寄存业务");
                    //return new Result<String>(sucCode, sucMsg, "false");
                }
                if (!dmzgdto.getIsdm().isEmpty()) {
                    dmzglist.add(dmzgdto);
                }
            }
        }
        //将海关返回值，更新表
        try {
            if (dmzglist.size() > 0) {
                for (int i = 0; i <dmzglist.size() ; i++) {
                    DmzgDto dd = dmzglist.get(i);
                    logger.info("取到要更新岛民标志的顾客："+dd.getGmname()+"证件号："+dd.getGmzjno()
                            +"证件类型："+dd.getGmzjlb()+"是否岛民："+dd.getIsdm());
                }
                custJczgdao.update(dmzglist);
            }
        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("是否岛民标志更新异常");
        }
    }
}
