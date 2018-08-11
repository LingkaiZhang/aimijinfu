package com.yuanin.aimifinance.entity;

public class DebtProductDetailEntity {

    /**
     * borrowTransferId : 1002
     * borrowAmountId : 1001
     * borrowAmountName : 转-爱米定期3月C1711015
     * annual : 10
     * days : 78
     * status : 已转让
     * discountRate : 0.05
     * dueCapital : 1000
     * repayMethod : 先息后本
     * dueAmount : 1100
     * discount : 200
     * payAmount : 800
     * surplusTime : 1533893787
     * isBuy : 0
     */

    private String borrowTransferId;
    private String borrowAmountId;
    private String borrowAmountName;
    private String annual;
    private String days;
    private String status;
    private String discountRate;
    private String dueCapital;
    private String repayMethod;
    private String dueAmount;
    private String discount;
    private String payAmount;
    private String surplusTime;
    private String isBuy;

    public String getBorrowTransferId() {
        return borrowTransferId;
    }

    public void setBorrowTransferId(String borrowTransferId) {
        this.borrowTransferId = borrowTransferId;
    }

    public String getBorrowAmountId() {
        return borrowAmountId;
    }

    public void setBorrowAmountId(String borrowAmountId) {
        this.borrowAmountId = borrowAmountId;
    }

    public String getBorrowAmountName() {
        return borrowAmountName;
    }

    public void setBorrowAmountName(String borrowAmountName) {
        this.borrowAmountName = borrowAmountName;
    }

    public String getAnnual() {
        return annual;
    }

    public void setAnnual(String annual) {
        this.annual = annual;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }

    public String getDueCapital() {
        return dueCapital;
    }

    public void setDueCapital(String dueCapital) {
        this.dueCapital = dueCapital;
    }

    public String getRepayMethod() {
        return repayMethod;
    }

    public void setRepayMethod(String repayMethod) {
        this.repayMethod = repayMethod;
    }

    public String getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(String dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getSurplusTime() {
        return surplusTime;
    }

    public void setSurplusTime(String surplusTime) {
        this.surplusTime = surplusTime;
    }

    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }
}
