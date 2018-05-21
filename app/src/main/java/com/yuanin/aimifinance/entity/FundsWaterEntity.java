package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * 资金流水
 */
public class FundsWaterEntity implements Serializable {

    private String type;
    private String created;
    private String money;
    private String status;

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    private int style;


    public void setType(String type) {
        this.type = type;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public String getCreated() {
        return created;
    }

    public String getMoney() {
        return money;
    }

    public String getStatus() {
        return status;
    }
}
