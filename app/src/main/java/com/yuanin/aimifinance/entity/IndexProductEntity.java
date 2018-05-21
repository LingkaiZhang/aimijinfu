package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/4/19
 * @desc
 */
public class IndexProductEntity implements Serializable {

    /**
     * isbuy : 0
     * statusname : 已流标
     * repay_method : 先息后本
     * guarantee_method : 无担保
     * amount : 900
     * term : 3
     * annual : 18
     * unit : 个月
     * id : 1466
     * title : 新手推荐
     * isnew : 1
     * type : 2
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
    private int isnew;
    private int type;
    private int is_red;

    private String extannual;
    private String organnual;

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

    public int getIs_red() {
        return is_red;
    }

    public void setIs_red(int is_red) {
        this.is_red = is_red;
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

    public int getIsnew() {
        return isnew;
    }

    public void setIsnew(int isnew) {
        this.isnew = isnew;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
