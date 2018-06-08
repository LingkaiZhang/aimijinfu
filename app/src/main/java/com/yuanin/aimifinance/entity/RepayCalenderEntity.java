package com.yuanin.aimifinance.entity;

import java.io.Serializable;

public class RepayCalenderEntity implements Serializable {


    /**
     * date : 2018-06-07
     * receiving : 0.00
     * newCapital : 230500.00
     * received : 0.00
     */

    private String date;
    private String receiving;
    private String newCapital;
    private String received;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReceiving() {
        return receiving;
    }

    public void setReceiving(String receiving) {
        this.receiving = receiving;
    }

    public String getNewCapital() {
        return newCapital;
    }

    public void setNewCapital(String newCapital) {
        this.newCapital = newCapital;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }
}
