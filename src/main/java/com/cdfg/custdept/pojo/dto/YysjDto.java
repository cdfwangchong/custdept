package com.cdfg.custdept.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class YysjDto {
    private String open_id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date yysj;

    private String qhdd;

    private String gwkh;

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public String getQhdd() {
        return qhdd;
    }

    public void setQhdd(String qhdd) {
        this.qhdd = qhdd;
    }

    public String getGwkh() {
        return gwkh;
    }

    public void setGwkh(String gwkh) {
        this.gwkh = gwkh;
    }

    public Date getYysj() {
        return yysj;
    }

    public void setYysj(Date yysj) {
        this.yysj = yysj;
    }

}
