package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * Created by huangxin on 2015/10/21.
 * 我的米定期
 */
public class MyInvestEntity implements Serializable {

    /**
     * id : 291
     * project_name : 爱米定期1月期第20151126期
     * expire_time : 1970-01-01
     * amount : 200
     * interest : 1.5
     * created : 2015-11-26
     */

    private String id;
    private String invest_id;
    private String project_name;
    private String expire_time;
    private String amount;
    private String interest;
    private String created;
    private String days_num;
    private int status;

    public String getInvest_id() {
        return invest_id;
    }

    public void setInvest_id(String invest_id) {
        this.invest_id = invest_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDays_num() {
        return days_num;
    }

    public void setDays_num(String days_num) {
        this.days_num = days_num;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public String getAmount() {
        return amount;
    }

    public String getInterest() {
        return interest;
    }

    public String getCreated() {
        return created;
    }
}
