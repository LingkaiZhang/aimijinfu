package com.yuanin.aimifinance.entity;

import java.io.Serializable;

public class RegisterVerifyMobileEntity implements Serializable{

    /**
     * errorCode : RF0001
     */

    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
