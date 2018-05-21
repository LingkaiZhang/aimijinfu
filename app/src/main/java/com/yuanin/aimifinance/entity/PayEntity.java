package com.yuanin.aimifinance.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangxin
 * @date 2016/7/18
 * @desc
 */
public class PayEntity implements Serializable {

    private String balance;
    private List<BindingCardEntity> bank;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<BindingCardEntity> getBank() {
        return bank;
    }

    public void setBank(List<BindingCardEntity> bank) {
        this.bank = bank;
    }
}
