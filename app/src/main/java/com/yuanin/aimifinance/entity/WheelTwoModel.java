package com.yuanin.aimifinance.entity;

import java.util.List;

public class WheelTwoModel {
    private String name;
    private List<WheelThreeModel> wheelThreeList;

    public WheelTwoModel() {
        super();
    }

    public WheelTwoModel(String name, List<WheelThreeModel> wheelThreeList) {
        super();
        this.name = name;
        this.wheelThreeList = wheelThreeList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WheelThreeModel> getWheelThreeList() {
        return wheelThreeList;
    }

    public void setWheelThreeList(List<WheelThreeModel> wheelThreeList) {
        this.wheelThreeList = wheelThreeList;
    }

}
