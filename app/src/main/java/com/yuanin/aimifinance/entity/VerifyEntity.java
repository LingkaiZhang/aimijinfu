package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2017/3/28
 * @desc
 */

public class VerifyEntity implements Serializable {
    public String phone;
    public int where;

    public VerifyEntity(String phone, int where) {
        this.phone = phone;
        this.where = where;
    }
}
