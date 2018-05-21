package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/1/6
 * @desc 往期项目
 */
public class HistoryItemEntity implements Serializable {
    /**
     * id : 291
     * project_name : 爱米定期1月期第20151126期
     * amount : 204
     * buylastdate : null
     * annual : 9
     * qty : 3
     * status : 7
     */

    private String id;
    private String project_name;
    private String amount;
    private String buylastdate;
    private String annual;
    private int qty;
    private int status;

    public void setId(String id) {
        this.id = id;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setBuylastdate(String buylastdate) {
        this.buylastdate = buylastdate;
    }

    public void setAnnual(String annual) {
        this.annual = annual;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getAmount() {
        return amount;
    }

    public String getBuylastdate() {
        return buylastdate;
    }

    public String getAnnual() {
        return annual;
    }

    public int getQty() {
        return qty;
    }

    public int getStatus() {
        return status;
    }
}
