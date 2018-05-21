package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2015/10/28
 * @desc 商品详情
 */
public class ProductDetailEntity implements Serializable {
    private String buyamount;
    private int percentage;
    private String term;
    private String unit;
    private String repay_method;
    private double minbuyvote;
    private double maxbuyvote;
    private String guaranteecompany;
    private int isbuy;
    private String id;
    private String project_name;
    private String amount;
    private String annual;
    private String guarantee_method;
    private String eachamount;
    private String managerate;
    private String joinrate;
    private String categoryid;
    private String buylastdate;
    private String statusname;
    private String collateral;
    private String debtstype;
    private String buylasttime;
    private String full_con;
    private String invest_con;
    private String repay_con;
    private String guarantee_con;
    private String debtstype_con;
    private String buystartdate;
    private String buystarttime;//发布时间
    private String manbiao_time;//满标时间
    private String interestdate;//起息时间
    private String expire_time;//结束时间
    private int step;

    private String extannual;
    private String organnual;
    private String safegrade;



    private String recruitmentperiod;

    public String getRecruitmentperiod() {
        return recruitmentperiod;
    }

    public void setRecruitmentperiod(String recruitmentperiod) {
        this.recruitmentperiod = recruitmentperiod;
    }

    public String getSafegrade() {
        return safegrade;
    }

    public void setSafegrade(String safegrade) {
        this.safegrade = safegrade;
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

    public String getBuystarttime() {
        return buystarttime;
    }

    public void setBuystarttime(String buystarttime) {
        this.buystarttime = buystarttime;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getDebtstype_con() {
        return debtstype_con;
    }

    public void setDebtstype_con(String debtstype_con) {
        this.debtstype_con = debtstype_con;
    }

    public String getManbiao_time() {
        return manbiao_time;
    }

    public void setManbiao_time(String manbiao_time) {
        this.manbiao_time = manbiao_time;
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


    public String getBuylasttime() {
        return buylasttime;
    }

    public void setBuylasttime(String buylasttime) {
        this.buylasttime = buylasttime;
    }

    public String getFull_con() {
        return full_con;
    }

    public void setFull_con(String full_con) {
        this.full_con = full_con;
    }

    public String getInvest_con() {
        return invest_con;
    }

    public void setInvest_con(String invest_con) {
        this.invest_con = invest_con;
    }

    public String getRepay_con() {
        return repay_con;
    }

    public void setRepay_con(String repay_con) {
        this.repay_con = repay_con;
    }

    public String getGuarantee_con() {
        return guarantee_con;
    }

    public void setGuarantee_con(String guarantee_con) {
        this.guarantee_con = guarantee_con;
    }

    public String getBuyamount() {
        return buyamount;
    }

    public void setBuyamount(String buyamount) {
        this.buyamount = buyamount;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
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

    public String getRepay_method() {
        return repay_method;
    }

    public void setRepay_method(String repay_method) {
        this.repay_method = repay_method;
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

    public String getGuaranteecompany() {
        return guaranteecompany;
    }

    public void setGuaranteecompany(String guaranteecompany) {
        this.guaranteecompany = guaranteecompany;
    }

    public int getIsbuy() {
        return isbuy;
    }

    public void setIsbuy(int isbuy) {
        this.isbuy = isbuy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
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

    public String getGuarantee_method() {
        return guarantee_method;
    }

    public void setGuarantee_method(String guarantee_method) {
        this.guarantee_method = guarantee_method;
    }

    public String getEachamount() {
        return eachamount;
    }

    public void setEachamount(String eachamount) {
        this.eachamount = eachamount;
    }

    public String getManagerate() {
        return managerate;
    }

    public void setManagerate(String managerate) {
        this.managerate = managerate;
    }

    public String getJoinrate() {
        return joinrate;
    }

    public void setJoinrate(String joinrate) {
        this.joinrate = joinrate;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getBuystartdate() {
        return buystartdate;
    }

    public void setBuystartdate(String buystartdate) {
        this.buystartdate = buystartdate;
    }

    public String getBuylastdate() {
        return buylastdate;
    }

    public void setBuylastdate(String buylastdate) {
        this.buylastdate = buylastdate;
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    public String getCollateral() {
        return collateral;
    }

    public void setCollateral(String collateral) {
        this.collateral = collateral;
    }

    public String getDebtstype() {
        return debtstype;
    }

    public void setDebtstype(String debtstype) {
        this.debtstype = debtstype;
    }
}
