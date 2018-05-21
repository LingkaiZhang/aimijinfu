package com.yuanin.aimifinance.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangxin
 * @date 2017/1/17
 * @desc
 */

public class AdvertisementEntity implements Serializable {

    /**
     * src : http://res.aimilicai.com/assets/new/api/picc0630.png
     * href : http://api.yuanin.com/picc0519.html
     * code : 20170117
     */

    private String src;
    private String href;
    private String code;
    private List<GestureTipEntity> list;

    public List<GestureTipEntity> getList() {
        return list;
    }

    public void setList(List<GestureTipEntity> list) {
        this.list = list;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
