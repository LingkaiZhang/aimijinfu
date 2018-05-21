package com.yuanin.aimifinance.entity;

/**
 * @author huangxin
 * @date 2015/10/27
 * @desc 实名认证信息类
 */
public class CertificationEntity {
    private String certifier;
    private String idcardno;

    public void setCertifier(String certifier) {
        this.certifier = certifier;
    }

    public void setIdcardno(String idcardno) {
        this.idcardno = idcardno;
    }

    public String getCertifier() {
        return certifier;
    }

    public String getIdcardno() {
        return idcardno;
    }
}
