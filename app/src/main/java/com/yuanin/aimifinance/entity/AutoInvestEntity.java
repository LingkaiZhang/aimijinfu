package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/5/3
 * @desc 自动投资
 */
public class AutoInvestEntity implements Serializable {

    /**
     * id : 2
     * remark : 我的自动投资方案
     * uid : 20000243
     * amount : 0
     * all_amount : 1
     * min_annual : 0
     * max_annual : 0
     * min_unit : 0
     * max_unit : 6
     * is_red : 1
     * created : 2017-02-08 18:03:42
     * status : 0
     * update_time : 2017-02-08 18:03:42
     */

    private String id;
    private String remark;
    private String uid;
    private String amount;
    private String all_amount;
    private String min_annual;
    private String max_annual;
    private String min_unit;
    private String max_unit;
    private String is_red;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAll_amount() {
        return all_amount;
    }

    public void setAll_amount(String all_amount) {
        this.all_amount = all_amount;
    }

    public String getMin_annual() {
        return min_annual;
    }

    public void setMin_annual(String min_annual) {
        this.min_annual = min_annual;
    }

    public String getMax_annual() {
        return max_annual;
    }

    public void setMax_annual(String max_annual) {
        this.max_annual = max_annual;
    }

    public String getMin_unit() {
        return min_unit;
    }

    public void setMin_unit(String min_unit) {
        this.min_unit = min_unit;
    }

    public String getMax_unit() {
        return max_unit;
    }

    public void setMax_unit(String max_unit) {
        this.max_unit = max_unit;
    }

    public String getIs_red() {
        return is_red;
    }

    public void setIs_red(String is_red) {
        this.is_red = is_red;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
