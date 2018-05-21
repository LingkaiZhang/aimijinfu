package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/1/14
 * @desc
 */
public class InvestRecordEntity implements Serializable {

    /**
     * mobile : 183******65
     * amount : 400
     * created : 2015-12-25
     */

    private String mobile;
    private String amount;
    private String created;

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAmount() {
        return amount;
    }

    public String getCreated() {
        return created;
    }
}
