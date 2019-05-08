package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * <p>类说明</p>
 *
 * @author lingkai  星期二 2019/5/7
 * @version :
 * @name :
 */
public class SmartInvestDetailsEntity implements Serializable {

    /**
     * balance : 11.0
     * surplusAmount : 1000000
     */

    private String balance;
    private String surplusAmount;
    private int is_activate_hkaccount;
    private int is_bind_bankcard;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(String surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public int getIs_activate_hkaccount() {
        return is_activate_hkaccount;
    }

    public void setIs_activate_hkaccount(int is_activate_hkaccount) {
        this.is_activate_hkaccount = is_activate_hkaccount;
    }

    public int getIs_bind_bankcard() {
        return is_bind_bankcard;
    }

    public void setIs_bind_bankcard(int is_bind_bankcard) {
        this.is_bind_bankcard = is_bind_bankcard;
    }

    @Override
    public String toString() {
        return "SmartInvestDetailsEntity{" +
                "balance='" + balance + '\'' +
                ", surplusAmount='" + surplusAmount + '\'' +
                ", is_activate_hkaccount=" + is_activate_hkaccount +
                ", is_bind_bankcard=" + is_bind_bankcard +
                '}';
    }
}
