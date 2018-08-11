package com.yuanin.aimifinance.entity;

public class DebtFinanceProductEntity {
    /**
     * borrowTransferId : 1002
     * borrowAmountId : 1001
     * annual : 10
     * borrowAmountName : 转-爱米定期3月C1711015
     * days : 78
     * status : 已转让
     * discountRate : 0.05
     * dueCapital : 9780
     * repayMethod : 先息后本
     * isbuy : 1
     */


    private String borrowTransferId;
    private String borrowAmountId;
    private String annual;
    private String borrowAmountName;
    private String days;
    private String status;
    private String discountRate;
    private String dueCapital;
    private String repayMethod;
    private String isbuy;

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

    public String getAnnual() {
        return annual;
    }

    public void setAnnual(String annual) {
        this.annual = annual;
    }

    public String getBorrowAmountName() {
        return borrowAmountName;
    }

    public void setBorrowAmountName(String borrowAmountName) {
        this.borrowAmountName = borrowAmountName;
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

    public String getIsbuy() {
        return isbuy;
    }

    public void setIsbuy(String isbuy) {
        this.isbuy = isbuy;
    }
}
