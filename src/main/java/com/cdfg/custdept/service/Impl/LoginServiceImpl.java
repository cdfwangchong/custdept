package com.cdfg.custdept.service.Impl;

import cn.cdfg.exceptionHandle.ExceptionPrintMessage;
import cn.cdfg.exceptionHandle.CustDeptNotFoundException;
import com.cdfg.custdept.service.LoginService;
import com.cdfg.custdept.dao.LoginDao;
import com.cdfg.custdept.pojo.dto.ThduserDto;
import com.cdfg.custdept.pojo.dto.UserDto;
import com.cdfg.custdept.pojo.until.Jwt;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.cdfg.custdept.pojo.until.Constant.*;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginDao logindao = null;

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
}
