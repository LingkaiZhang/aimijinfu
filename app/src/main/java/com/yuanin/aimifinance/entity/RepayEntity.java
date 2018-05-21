package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/9/23
 * @desc
 */

public class RepayEntity implements Serializable {

    /**
     * periodqty : 1
     * paymentdate : 0
     * paymentprice : 1007500
     * status : 待还款
     * color : #fdc36b
     */

    private String periodqty;
    private String paymentdate;
    private String paymentprice;
    private String status;
    private String color;

    public String getPeriodqty() {
        return periodqty;
    }

    public void setPeriodqty(String periodqty) {
        this.periodqty = periodqty;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getPaymentprice() {
        return paymentprice;
    }

    public void setPaymentprice(String paymentprice) {
        this.paymentprice = paymentprice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
