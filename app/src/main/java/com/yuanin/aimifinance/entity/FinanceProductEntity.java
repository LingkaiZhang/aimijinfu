package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/4/19
 * @desc
 */
public class FinanceProductEntity implements Serializable {

    /**
     * isbuy : 1
     * statusname : 立即出借
     * repay_method : 先息后本
     * guarantee_method : 平台担保
     * amount : 4000
     * term : 6
     * annual : 11
     * unit : 个月
     * id : 1598
     * title : 委托test01
     * type : 3
     */

    private int isbuy;
    private String statusname;
    private String repay_method;
    private String guarantee_method;
    private String amount;
    private String term;
    private String annual;
    private String unit;
    private String id;
    private String title;
    private int type;
    private int style;


    private String borrowTransferId;
    private String borrowAmountId;
    private String borrowAmountName;
    private String days;
    private String status;
    private String discountRate;
    private String dueCapital;
    private String repayMethod;


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




    private String extannual;
    private String organnual;

    private String typename;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getExtannual() {
        return extannual;
    }

    public void setExtannual(String extannual) {
        this.extannual = extannual;
    }

    public String getOrgannual() {
        return organnual;
    }

    public void setOrgannual(String organnual) {
        this.organnual = organnual;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getIsbuy() {
        return isbuy;
    }

    public void setIsbuy(int isbuy) {
        this.isbuy = isbuy;
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    public String getRepay_method() {
        return repay_method;
    }

    public void setRepay_method(String repay_method) {
        this.repay_method = repay_method;
    }

    public String getGuarantee_method() {
        return guarantee_method;
    }

    public void setGuarantee_method(String guarantee_method) {
        this.guarantee_method = guarantee_method;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getAnnual() {
        return annual;
    }

    public void setAnnual(String annual) {
        this.annual = annual;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
