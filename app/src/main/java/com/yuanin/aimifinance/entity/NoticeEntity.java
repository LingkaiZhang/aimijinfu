package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/4/20
 * @desc
 */
public class NoticeEntity implements Serializable {

    /**
     * id : 116
     * title : 关于新浪支付2015年12月20日00:00-06:00进行系统维护的公告1
     * issucedate : 2015-12-04 16:36:00
     */

    private String id;
    private String title;
    private String issucedate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssucedate() {
        return issucedate;
    }

    public void setIssucedate(String issucedate) {
        this.issucedate = issucedate;
    }
}
