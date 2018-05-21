package com.yuanin.aimifinance.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangxin
 * @date 2016/1/15
 * @desc
 */
public class BuyProductEntity implements Serializable {
    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getAnnual() {
        return annual;
    }

    public void setAnnual(String annual) {
        this.annual = annual;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getEachamount() {
        return eachamount;
    }

    public void setEachamount(String eachamount) {
        this.eachamount = eachamount;
    }

    public int getIsbuy() {
        return isbuy;
    }

    public void setIsbuy(int isbuy) {
        this.isbuy = isbuy;
    }

    public double getMinbuyvote() {
        return minbuyvote;
    }

    public void setMinbuyvote(double minbuyvote) {
        this.minbuyvote = minbuyvote;
    }

    public double getMaxbuyvote() {
        return maxbuyvote;
    }

    public void setMaxbuyvote(double maxbuyvote) {
        this.maxbuyvote = maxbuyvote;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public int getIs_bind_bankcard() {
        return is_bind_bankcard;
    }

    public void setIs_bind_bankcard(int is_bind_bankcard) {
        this.is_bind_bankcard = is_bind_bankcard;
    }

    public int getIs_new() {
        return is_new;
    }

    public void setIs_new(int is_new) {
        this.is_new = is_new;
    }

    public int getIsdeposit() {
        return isdeposit;
    }

    public void setIsdeposit(int isdeposit) {
        this.isdeposit = isdeposit;
    }

    public int getIs_activate_hkaccount() {
        return is_activate_hkaccount;
    }

    public void setIs_activate_hkaccount(int is_activate_hkaccount) {
        this.is_activate_hkaccount = is_activate_hkaccount;
    }

    public List<RedPacketsEntity> getList() {
        return list;
    }

    public void setList(List<RedPacketsEntity> list) {
        this.list = list;
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


    private String project_name;
    private String annual;
    private String term;
    private String unit;
    private double amount;
    private String eachamount;
    private int isbuy;
    private double minbuyvote;
    private double maxbuyvote;
    private double balance;
    private double interest;
    private int is_bind_bankcard;
    private int is_new;
    private int isdeposit;
    private int is_activate_hkaccount;
    private List<RedPacketsEntity> list;

    private String extannual;
    private String organnual;

}
