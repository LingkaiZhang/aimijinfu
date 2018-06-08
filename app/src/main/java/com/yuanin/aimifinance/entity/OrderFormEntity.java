package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/7/14
 * @desc
 */
public class OrderFormEntity implements Serializable {
    private String project_name;
    private String borrow_status_id;
    private String amount;
    private String annual;
    private String annual_new;
    private double interest_total;
    private String term;
    private String repay_method;
    private String investdate;
    private String interestdate;
    private String expire_time;
    private double capital_wait;
    private double interest_wait;
    private double capital_yes;
    private double interest_yes;
    private int is_red;
    private int red_type;
    //是否提前还款 0 否; 1 是;
    private String is_prerepayment;

    //电子合同链接
    private String ele_contact_link;
    //待收奖励
    private double wait_interest;
    //已收奖励
    private double yes_interest;

    public double getWait_interest() {
        return wait_interest;
    }

    public void setWait_interest(double wait_interest) {
        this.wait_interest = wait_interest;
    }

    public double getYes_interest() {
        return yes_interest;
    }

    public void setYes_interest(double yes_interest) {
        this.yes_interest = yes_interest;
    }

    public String getEle_contact_link() {
        return ele_contact_link;
    }

    public void setEle_contact_link(String ele_contact_link) {
        this.ele_contact_link = ele_contact_link;
    }

    public String getIs_prerepayment() {
        return is_prerepayment;
    }

    public void setIs_prerepayment(String is_prerepayment) {
        this.is_prerepayment = is_prerepayment;
    }


    public int getRed_type() {
        return red_type;
    }

    public void setRed_type(int red_type) {
        this.red_type = red_type;
    }

    public String getAnnual_new() {
        return annual_new;
    }

    public void setAnnual_new(String annual_new) {
        this.annual_new = annual_new;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getBorrow_status_id() {
        return borrow_status_id;
    }

    public void setBorrow_status_id(String borrow_status_id) {
        this.borrow_status_id = borrow_status_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAnnual() {
        return annual;
    }

    public void setAnnual(String annual) {
        this.annual = annual;
    }

    public double getInterest_total() {
        return interest_total;
    }

    public void setInterest_total(double interest_total) {
        this.interest_total = interest_total;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getRepay_method() {
        return repay_method;
    }

    public void setRepay_method(String repay_method) {
        this.repay_method = repay_method;
    }

    public String getInvestdate() {
        return investdate;
    }

    public void setInvestdate(String investdate) {
        this.investdate = investdate;
    }

    public String getInterestdate() {
        return interestdate;
    }

    public void setInterestdate(String interestdate) {
        this.interestdate = interestdate;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public double getCapital_wait() {
        return capital_wait;
    }

    public void setCapital_wait(double capital_wait) {
        this.capital_wait = capital_wait;
    }

    public double getInterest_wait() {
        return interest_wait;
    }

    public void setInterest_wait(double interest_wait) {
        this.interest_wait = interest_wait;
    }

    public double getCapital_yes() {
        return capital_yes;
    }

    public void setCapital_yes(double capital_yes) {
        this.capital_yes = capital_yes;
    }

    public double getInterest_yes() {
        return interest_yes;
    }

    public void setInterest_yes(double interest_yes) {
        this.interest_yes = interest_yes;
    }

    public int getIs_red() {
        return is_red;
    }

    public void setIs_red(int is_red) {
        this.is_red = is_red;
    }
}
