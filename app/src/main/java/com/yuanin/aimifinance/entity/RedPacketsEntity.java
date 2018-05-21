package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * 红包
 */
public class RedPacketsEntity implements Serializable {
    private String id;
    private String name;
    private int amount;
    private int min_invest_amount;
    private String startdate;
    private String expirydate;
    private String usetime;
    private int status;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMin_invest_amount() {
        return min_invest_amount;
    }

    public void setMin_invest_amount(int min_invest_amount) {
        this.min_invest_amount = min_invest_amount;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public String getUsetime() {
        return usetime;
    }

    public void setUsetime(String usetime) {
        this.usetime = usetime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
