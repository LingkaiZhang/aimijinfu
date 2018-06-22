package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * 我的收益
 */
public class AddUpEarningsEntity implements Serializable {

    /**
     * project_name : 爱米定期3月期第20151125期
     * periodqty : 03
     * repay_time : 2015-11-27
     * interest : 1.67
     * status : 即将到期
     */

    private String project_name;
    private String periodqty;
    private String repay_time;
    private String interest;
    private int status;
    private String waitinterest;
    private String receivedinterest;
    private String term;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getWaitinterest() {
        return waitinterest;
    }

    public void setWaitinterest(String waitinterest) {
        this.waitinterest = waitinterest;
    }

    public String getReceivedinterest() {
        return receivedinterest;
    }

    public void setReceivedinterest(String receivedinterest) {
        this.receivedinterest = receivedinterest;
    }


    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setPeriodqty(String periodqty) {
        this.periodqty = periodqty;
    }

    public void setRepay_time(String repay_time) {
        this.repay_time = repay_time;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getPeriodqty() {
        return periodqty;
    }

    public String getRepay_time() {
        return repay_time;
    }

    public String getInterest() {
        return interest;
    }

    public int getStatus() {
        return status;
    }
}
