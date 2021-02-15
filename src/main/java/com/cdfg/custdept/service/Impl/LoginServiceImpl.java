package com.cdfg.custdept.service.Impl;

import cn.cdfg.exceptionHandle.ExceptionPrintMessage;
import cn.cdfg.exceptionHandle.CustDeptNotFoundException;
import com.alibaba.fastjson.JSONObject;
import com.cdfg.custdept.dao.CustJczgDao;
import com.cdfg.custdept.pojo.dto.CustJczgDto;
import com.cdfg.custdept.pojo.dto.DmzgDto;
import com.cdfg.custdept.pojo.until.HttpClientUtil;
import com.cdfg.custdept.pojo.until.TableInfo;
import com.cdfg.custdept.service.LoginService;
import com.cdfg.custdept.dao.LoginDao;
import com.cdfg.custdept.pojo.dto.ThduserDto;
import com.cdfg.custdept.pojo.dto.UserDto;
import com.cdfg.custdept.pojo.until.Jwt;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.cdfg.custdept.pojo.until.Constant.*;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginDao logindao = null;

    @Autowired
    CustJczgDao custJczgdao =null;

    Logger logger = Logger.getLogger(LoginServiceImpl.class);

    /**
     * @param userDto
     * @return
     */
    @Override
    public Map<String, Object> login(UserDto userDto) {
        Map<String, Object> payload = new HashMap<String, Object>();
        try {
            logger.info("取到登录信息"+userDto.getUserId()+"@"+userDto.getPassWord());
            ThduserDto thduser = logindao.selectByPrimaryKey(userDto);

            if (thduser == null) {
                logger.error("员工ID在表中不存在");
                throw new CustDeptNotFoundException(errCode_2,errMsg_2);
            }else {
                String worknumber = thduser.getUserCode();//员工工号
                String departmentid = thduser.getDeptId();// 部门id
                String status = thduser.getStatus();//状态
                String accountname = thduser.getUserName();// 用户名称

                Date date = new Date();
                payload.put("accountname",accountname);// 用户名称
                payload.put("departmentid",departmentid);// 部门id
                payload.put("status",status);//状态
                payload.put("worknumber",worknumber);//员工工号
                payload.put("iat", date.getTime());// 生成时间
                payload.put("ext", date.getTime() + 10000 * 1000 * 60 * 60 * 24);// 过期时间1 小时 单位是毫秒

                //得到token
                String token = new Jwt().createToken(payload);

                payload.put("token",token);
            }
        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("员工号不存在或者密码错误");
            throw new CustDeptNotFoundException(errCode_2,errMsg_2);
        }
        return payload;
    }

//    @Override
//    public void task() {
//        List<CustJczgDto> cjlist = null;
//        List<DmzgDto> dmzglist = new ArrayList<DmzgDto>();
//        try {
//            Map param = new HashMap();
//            custJczgdao.QryCustJczg(param);
//            cjlist = (List<CustJczgDto>) param.get("jcRc");
//        } catch (Exception e) {
//            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
//            logger.error("员工号不存在或者密码错误");
//        }
//        for (int i = 0; i < cjlist.size(); i++) {
//            CustJczgDto cjDto = cjlist.get(i);
//            String user_name = cjDto.getGmname();
//            String id_type = cjDto.getGmzjlb();
//            String card_id = cjDto.getGmzjno();
//
//            //调用海关接口，查询客人是否岛民
//            HttpClientUtil htUtil = new HttpClientUtil();
//            TableInfo tinfo = new TableInfo();
//            tinfo.setBuyerName(user_name);
//            tinfo.setIdcardNo(card_id);
//            tinfo.setIdcardType(id_type);
//
//            JSONObject jsonObject = htUtil.doPost(tinfo);
//            String status = jsonObject.getString("status");
//            String data = jsonObject.getString("data");
//            String msg = jsonObject.getString("msg");
//            logger.info("获取海关岛民身份接口返回值" + status + data + msg + card_id);
//            logger.info("获取岛内居民信息返回标志：" + "用户名：" + user_name + "身份证：" + card_id);
//            DmzgDto dmzgdto = new DmzgDto();
//            if ("error".equals(status)) {
//                logger.error("海关验证岛民身份失败");
//                //throw new CustDeptNotFoundException(errCode, "海关验证身份接口返回失败");
//            } else {
//                if ("true".equals(data)) {
//                    dmzgdto.setIsdm("Y");
//                    dmzgdto.setGmname(user_name);
//                    dmzgdto.setGmzjlb(id_type);
//                    dmzgdto.setGmzjno(card_id);
//                    logger.error(user_name + card_id + "是岛内居民，可以申请办理寄存业务");
//                    //return new Result<String>(sucCode, sucMsg, "true");
//                } else {
//                    dmzgdto.setIsdm("N");
//                    dmzgdto.setGmname(user_name);
//                    dmzgdto.setGmzjlb(id_type);
//                    dmzgdto.setGmzjno(card_id);
//                    logger.error(user_name + card_id + "非岛内居民，不能申请办理寄存业务");
//                    //return new Result<String>(sucCode, sucMsg, "false");
//                }
//            }
//            dmzglist.add(dmzgdto);
//        }
//        //将海关返回值，更新表
//        try {
//            custJczgdao.update(dmzglist);
//        } catch (Exception e) {
//            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
//            logger.error("员工号不存在或者密码错误");
//        }
//    }
}
