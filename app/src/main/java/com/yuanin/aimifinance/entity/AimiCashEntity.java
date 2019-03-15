package com.yuanin.aimifinance.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangxin
 * @date 2016/7/8
 * @desc
 */
public class AimiCashEntity implements Serializable {
    private String qty;
    private String fee;
    private String normalFee;
    private String quickFee;
    private String balance;
    private List<BindingCardEntity> bank;
    private List<StrEntity> cue;
    
    public List<StrEntity> getCue() {
        return cue;
    }

    public void setCue(List<StrEntity> cue) {
        this.cue = cue;
    }


    public List<BindingCardEntity> getBank() {
        return bank;
    }

    public void setBank(List<BindingCardEntity> bank) {
        this.bank = bank;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getNormalFee() {
        return normalFee;
    }

    public void setNormalFee(String normalFee) {
        this.normalFee = normalFee;
    }

    public String getQuickFee() {
        return quickFee;
    }

    public void setQuickFee(String quickFee) {
        this.quickFee = quickFee;
    }
}
