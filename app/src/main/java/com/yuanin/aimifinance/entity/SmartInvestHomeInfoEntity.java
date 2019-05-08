package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * <p>类说明</p>
 *
 * @author lingkai  星期一 2019/5/6
 * @version :
 * @name :
 */
public class SmartInvestHomeInfoEntity implements Serializable {

    /**
     * min_interest : 7
     * max_interest : 11
     * surplusAmount : 1000000
     */

    private String min_interest;
    private String max_interest;
    private String surplusAmount;

    public String getMin_interest() {
        return min_interest;
    }

    public void setMin_interest(String min_interest) {
        this.min_interest = min_interest;
    }

    public String getMax_interest() {
        return max_interest;
    }

    public void setMax_interest(String max_interest) {
        this.max_interest = max_interest;
    }

    public String getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(String surplusAmount) {
        this.surplusAmount = surplusAmount;
    }
}
