package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * <p>类说明</p>
 *
 * @author lingkai  星期三 2019/5/8
 * @version :
 * @name :
 */
public class SmartInvestProductListEntity implements Serializable {

    /**
     * amount : 6000
     * term : 6
     * projectName : 爱米定期6月20151026期
     * borrowAmountId : 123456
     */

    private String amount;
    private String term;
    private String projectName;
    private String borrowAmountId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBorrowAmountId() {
        return borrowAmountId;
    }

    public void setBorrowAmountId(String borrowAmountId) {
        this.borrowAmountId = borrowAmountId;
    }
}
