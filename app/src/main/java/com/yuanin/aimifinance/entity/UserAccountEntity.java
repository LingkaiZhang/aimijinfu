package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2015/10/26
 * @desc 我的账户信息
 */
public class UserAccountEntity implements Serializable {
    private String amount;
    private String balance;
    private String interest;
    private String deposit;
    private String enjoy;
    private int red_num;
    //是否已绑定银行卡
    private int is_bind_bankcard;
    //HK状态
    private int is_activate_hkaccount;

    private int surveyresult;

    public int getSurveyresult() {
        return surveyresult;
    }

    public void setSurveyresult(int surveyresult) {
        this.surveyresult = surveyresult;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getEnjoy() {
        return enjoy;
    }

    public void setEnjoy(String enjoy) {
        this.enjoy = enjoy;
    }

    public int getRed_num() {
        return red_num;
    }

    public void setRed_num(int red_num) {
        this.red_num = red_num;
    }

    public int getIs_bind_bankcard() {
        return is_bind_bankcard;
    }

    public void setIs_bind_bankcard(int is_bind_bankcard) {
        this.is_bind_bankcard = is_bind_bankcard;
    }

    public int getIs_activate_hkaccount() {
        return is_activate_hkaccount;
    }

    public void setIs_activate_hkaccount(int is_activate_hkaccount) {
        this.is_activate_hkaccount = is_activate_hkaccount;
    }
}
