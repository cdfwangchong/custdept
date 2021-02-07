package com.cdfg.custdept.service.Impl;

import cn.cdfg.exceptionHandle.ExceptionPrintMessage;
import cn.cdfg.exceptionHandle.CustDeptNotFoundException;
import com.cdfg.custdept.dao.CheckCanclePostDao;
import com.cdfg.custdept.dao.CustaddrlistDao;
import com.cdfg.custdept.dao.UserlistDao;
import com.cdfg.custdept.pojo.dto.Custdeptlist;
import com.cdfg.custdept.pojo.dto.InsertCustAddrAndListDto;
import com.cdfg.custdept.pojo.dto.Userlist;
import com.cdfg.custdept.pojo.until.BillEntity;
import com.cdfg.custdept.service.CustAddrListService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cdfg.custdept.pojo.until.Constant.*;

@Service
public class CustAddrListServiceImpl implements CustAddrListService {
    @Autowired
    CustaddrlistDao clDao;

    @Autowired
    UserlistDao ulDao;

    @Autowired
    CheckCanclePostDao ccpDao;

    Logger logger = Logger.getLogger(CustAddrListServiceImpl.class);

    /**
     * 商品寄存接口
     * @param ica
     * @return
     */
    @Override
    public boolean insertCustAddrList(InsertCustAddrAndListDto ica) {
        //查出顾客的购物卡号
        Userlist ul = ulDao.selectByPrimaryKey(ica.getOpen_id());
        String gwkh = ul.getIdseq();//客人的购物卡号
        String telphno = ul.getTelphno();

        List<BillEntity> PIlist = ica.getOrderList();
        Map<String,String> Markmap = new HashMap<String,String>();
        Markmap.put("6868",null);
        Markmap.put("6921",null);
        Markmap.put("6922",null);
        Markmap.put("6127",null);

        //记录包裹数
        Map<String, Integer> pcgCntMap = new HashMap<String,Integer>();
        pcgCntMap.put("6868",0);
        pcgCntMap.put("6921",0);
        pcgCntMap.put("6922",0);
        pcgCntMap.put("6127",0);

        int pcgCntsy= 0;
        int pcgCntba= 0;
        int pcgCnthk= 0;
        int pcgCntml= 0;
        List<Custdeptlist> icadList = new ArrayList<Custdeptlist>();

        try {
            //拼接各门店的提货单号
            if (PIlist != null) {
                for (int i = 0; i < PIlist.size(); i++) {
                    BillEntity pi = PIlist.get(i);
                    if ("6868".equals(pi.getMarket()) || "6874".equals(pi.getMarket())) {
                        String billno = Markmap.get("6868");
                        if (billno == null) {
                            Markmap.put("6868",pi.getXsdno());
                        }else {
                            billno = pi.getXsdno()+"|"+billno;
                            Markmap.put("6868",billno);
                        }
                        pcgCntsy++;
                        pcgCntMap.put("6868",pcgCntsy);

                    } else if("6921".equals(pi.getMarket())) {
                        String billno = Markmap.get("6921");
                        if (billno == null) {
                            Markmap.put("6921",pi.getXsdno());
                        }else {
                            billno = pi.getXsdno()+"|"+billno;
                            Markmap.put("6921",billno);
                        }
                        pcgCntba++;
                        pcgCntMap.put("6921",pcgCntba);

                    }else if ("6922".equals(pi.getMarket())) {
                        String billno = Markmap.get("6922");
                        if (billno == null) {
                            Markmap.put("6922",pi.getXsdno());
                        }else {
                            billno = pi.getXsdno()+"|"+billno;
                            Markmap.put("6922",billno);
                        }
                        pcgCnthk++;
                        pcgCntMap.put("6922",pcgCnthk);

                    }else if ("6127".equals(pi.getMarket())){
                        String billno = Markmap.get("6127");
                        if (billno == null) {
                            Markmap.put("6127",pi.getXsdno());
                        }else {
                            billno = pi.getXsdno()+"|"+billno;
                            Markmap.put("6127",billno);
                        }
                        pcgCntml++;
                        pcgCntMap.put("6127",pcgCntml);
                    }else {
                        throw new CustDeptNotFoundException(errCode2,errMsg2);
                    }
                }
            }
            //将有值的Markmap存入List
            for (Map.Entry<String,String> entry : Markmap.entrySet()) {
                if (entry.getValue() != null) {
                    Custdeptlist icaDto = new Custdeptlist();
                    icaDto.setYyseq(ica.getYyseq());
                    icaDto.setQhdd(ica.getQhdd());
                    icaDto.setPkgcnt(pcgCntMap.get(entry.getKey()));
                    icaDto.setMarket(entry.getKey());
                    icaDto.setGwkh(gwkh);
                    icaDto.setSeqidC(ica.getSeq_no());
                    icaDto.setXsdno(entry.getValue());
                    icaDto.setTelNum(telphno);
                    icadList.add(icaDto);

                    //将客人的邮寄信息写入日志
                    logger.info("顾客："+gwkh+"电话："+telphno+"邮寄的提货单"+entry.getValue());
                }
            }
        } catch (Exception e) {
            logger.error(new ExceptionPrintMessage().errorTrackSpace(e));
            logger.error("寄存信息写入异常");
            throw new CustDeptNotFoundException(errCode3,errMsg3);
        }
            int ret = clDao.insert(icadList);
            if (ret != icadList.size()) {
                logger.error("List中的数据没有正确写入顾客地址列表");
                throw new CustDeptNotFoundException(errCode4,errMsg4);
            }

        return true;
    }
}
