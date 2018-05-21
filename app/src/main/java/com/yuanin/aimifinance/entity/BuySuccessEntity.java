package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/7/20
 * @desc
 */
public class BuySuccessEntity implements Serializable {
    private String productName;
    private String buyMoney;
    private String redPacket;
    private int is_red;
    private int red_type;
    private String banner;
    private String repay_method;
    private String banner_url;
    private String invest_id;


    public int getRed_type() {
        return red_type;
    }

    public void setRed_type(int red_type) {
        this.red_type = red_type;
    }

    public String getInvest_id() {
        return invest_id;
    }

    public void setInvest_id(String invest_id) {
        this.invest_id = invest_id;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBuyMoney() {
        return buyMoney;
    }

    public void setBuyMoney(String buyMoney) {
        this.buyMoney = buyMoney;
    }

    public String getRedPacket() {
        return redPacket;
    }

    public void setRedPacket(String redPacket) {
        this.redPacket = redPacket;
    }

    public int getIs_red() {
        return is_red;
    }

    public void setIs_red(int is_red) {
        this.is_red = is_red;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getRepay_method() {
        return repay_method;
    }

    public void setRepay_method(String repay_method) {
        this.repay_method = repay_method;
    }
}
