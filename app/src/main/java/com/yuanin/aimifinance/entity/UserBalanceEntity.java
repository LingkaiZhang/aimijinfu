package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/1/13
 * @desc
 */
public class UserBalanceEntity implements Serializable {
    private double balance;
    private double withdraw_amount;
    private double appoint;
    private double all_recharge_amount;
    private double all_withdraw_amount;

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setWithdraw_amount(double withdraw_amount) {
        this.withdraw_amount = withdraw_amount;
    }

    public void setAppoint(double appoint) {
        this.appoint = appoint;
    }

    public void setAll_recharge_amount(double all_recharge_amount) {
        this.all_recharge_amount = all_recharge_amount;
    }

    public void setAll_withdraw_amount(double all_withdraw_amount) {
        this.all_withdraw_amount = all_withdraw_amount;
    }

    public double getBalance() {
        return balance;
    }

    public double getWithdraw_amount() {
        return withdraw_amount;
    }

    public double getAppoint() {
        return appoint;
    }

    public double getAll_recharge_amount() {
        return all_recharge_amount;
    }

    public double getAll_withdraw_amount() {
        return all_withdraw_amount;
    }
}
