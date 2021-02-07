package com.cdfg.custdept.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class HoldByDateDto {

    public String getBillNO() {
        return billNO;
    }

    public void setBillNO(String billNO) {
        this.billNO = billNO;
    }

    public String getTmpCode() {
        return tmpCode;
    }

    public void setTmpCode(String tmpCode) {
        this.tmpCode = tmpCode;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public Date getLdrq() {
        return ldrq;
    }

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    public void setLdrq(Date ldrq) {
        this.ldrq = ldrq;
    }

    /**
     * 装箱单号
     */
    private String tmpCode;

    /**
     * 门店
     */
    private String market;

    /**
     * 离岛日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date ldrq;

    /**
     * 订单号
     */
    private String billNO;

}
