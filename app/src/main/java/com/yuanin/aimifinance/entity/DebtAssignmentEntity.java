package com.yuanin.aimifinance.entity;

import java.io.Serializable;

public class DebtAssignmentEntity implements Serializable {

    /**
     * borrowAmountId : 1001
     * orderId : 2001
     * borrowTransferId : 3001
     * borrowAmountName : 转-爱米定期3月C1711015
     * status : 审核中
     * dueCapital : 10000.00
     * dueInterest : 100.00
     * discountRate : 0.05
     * dueAmount : 10100.00
     * discount : 100
     * fee : 220
     * amount : 9780
     * created : 2018-08-09 17:32:15
     */

    private String borrowAmountId;
    private String orderId;
    private String borrowTransferId;
    private String borrowAmountName;
    private String status;
    private String dueCapital;
    private String dueInterest;
    private String discountRate;
    private String dueAmount;
    private String discount;
    private String fee;
    private String amount;
    private String created;

    public String getBorrowAmountId() {
        return borrowAmountId;
    }

    public void setBorrowAmountId(String borrowAmountId) {
        this.borrowAmountId = borrowAmountId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBorrowTransferId() {
        return borrowTransferId;
    }

    public void setBorrowTransferId(String borrowTransferId) {
        this.borrowTransferId = borrowTransferId;
    }

    public String getBorrowAmountName() {
        return borrowAmountName;
    }

    public void setBorrowAmountName(String borrowAmountName) {
        this.borrowAmountName = borrowAmountName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDueCapital() {
        return dueCapital;
    }

    public void setDueCapital(String dueCapital) {
        this.dueCapital = dueCapital;
    }

    public String getDueInterest() {
        return dueInterest;
    }

    public void setDueInterest(String dueInterest) {
        this.dueInterest = dueInterest;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
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

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
