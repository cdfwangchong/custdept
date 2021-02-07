package com.cdfg.custdept.pojo.until;

import java.math.BigDecimal;
import java.util.Date;

public class CustAddrlistEntity {
    public String getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(String seq_no) {
        this.seq_no = seq_no;
    }


    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }


    public Date getYysj() {
        return yysj;
    }

    public void setYysj(Date yysj) {
        this.yysj = yysj;
    }

    public String getQhdd() {
        return qhdd;
    }

    public void setQhdd(String qhdd) {
        this.qhdd = qhdd;
    }

    public String getXsdno() {
        return xsdno;
    }

    public void setXsdno(String xsdno) {
        this.xsdno = xsdno;
    }

    public BigDecimal getYyseq() {
        return yyseq;
    }

    public void setYyseq(BigDecimal yyseq) {
        this.yyseq = yyseq;
    }

    private String seq_no;

    private Date yysj;

    private String qhdd;

    private String xsdno;

    private BigDecimal yyseq;

    private String market;



}
