package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * <p>类说明</p>
 *
 * @author lingkai  星期三 2019/5/8
 * @version :
 * @name :
 */
public class SmartInvestRecordEntity implements Serializable {

    /**
     * money : 500.00
     * created : 2019-04-29 14:59:03.0
     * mobile : 156****5655
     */

    private String money;
    private String created;
    private String mobile;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
