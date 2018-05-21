package com.yuanin.aimifinance.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 资金流水
 */
public class FundsWaterMapEntity implements Serializable {
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<FundsWaterEntity> getList() {
        return list;
    }

    public void setList(List<FundsWaterEntity> list) {
        this.list = list;
    }

    private String year;
    private List<FundsWaterEntity> list;

}
