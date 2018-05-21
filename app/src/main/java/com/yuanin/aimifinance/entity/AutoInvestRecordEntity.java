package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2017/2/10
 * @desc
 */

public class AutoInvestRecordEntity implements Serializable {

    /**
     * created : 2017-02-10 16:22:47
     * remark : 我的自动投资方案
     * amount : 1000
     * project_name : 20170210定期三月测试
     */

    private String created;
    private String remark;
    private String amount;
    private String project_name;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }
}
