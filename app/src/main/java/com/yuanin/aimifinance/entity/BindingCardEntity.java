package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/7/18
 * @desc
 */
public class BindingCardEntity implements Serializable {

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getSinglepay() {
        return singlepay;
    }

    public void setSinglepay(String singlepay) {
        this.singlepay = singlepay;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getDaymaxpay() {
        return daymaxpay;
    }

    public void setDaymaxpay(String daymaxpay) {
        this.daymaxpay = daymaxpay;
    }

    public String getMin_recharge() {
        return min_recharge;
    }

    public void setMin_recharge(String min_recharge) {
        this.min_recharge = min_recharge;
    }

    private String full_name;
    private String singlepay;
    private String logo;
    private String card_no;
    private String daymaxpay;
    private String min_recharge;

}
