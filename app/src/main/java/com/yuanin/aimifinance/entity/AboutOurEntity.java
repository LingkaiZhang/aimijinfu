package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2015/10/28
 * @desc 关于我们
 */
public class AboutOurEntity implements Serializable {
    private String telphone;
    private String website;
    private String weixin;
    private String email;
    private String companyname;
    private String address;

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelphone() {
        return telphone;
    }

    public String getWebsite() {
        return website;
    }

    public String getWeixin() {
        return weixin;
    }

    public String getEmail() {
        return email;
    }

    public String getCompanyname() {
        return companyname;
    }

    public String getAddress() {
        return address;
    }
}
