package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/1/20
 * @desc
 */
public class InviteFriendEntity implements Serializable {

    /**
     * mobile : 156******66
     * created : 2016-01-19
     * amount : 0
     */

    private String mobile;
    private String created;
    private String amount;

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCreated() {
        return created;
    }

    public String getAmount() {
        return amount;
    }
}
