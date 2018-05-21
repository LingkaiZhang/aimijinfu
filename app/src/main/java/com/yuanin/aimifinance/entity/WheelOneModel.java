package com.yuanin.aimifinance.entity;

import java.util.List;

public class WheelOneModel {
    private String name;
    private List<WheelTwoModel> wheelTwoList;

    public WheelOneModel() {
        super();
    }

    public WheelOneModel(String name, List<WheelTwoModel> wheelTwoList) {
        super();
        this.name = name;
        this.wheelTwoList = wheelTwoList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WheelTwoModel> getWheelTwoList() {
        return wheelTwoList;
    }

    public void setWheelTwoList(List<WheelTwoModel> wheelTwoList) {
        this.wheelTwoList = wheelTwoList;
    }

}
