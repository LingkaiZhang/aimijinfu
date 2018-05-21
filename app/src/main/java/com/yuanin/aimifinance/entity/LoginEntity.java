package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2015/10/16
 * @time 18:01
 * @desc 登录
 */
public class LoginEntity implements Serializable {

    private String userid;
    private String mobile;
    private String login_token;

    public String getLogin_token() {
        return login_token;
    }

    public void setLogin_token(String login_token) {
        this.login_token = login_token;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserid() {
        return userid;
    }

    public String getMobile() {
        return mobile;
    }
}
