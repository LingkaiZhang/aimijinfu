package com.yuanin.aimifinance.entity;

import android.view.View.OnClickListener;

import java.io.Serializable;

/**
 *
 * */
public class TopBarInfo implements Serializable {
    private String txtStr; // 中间textview文字
    private OnClickListener leftBtnListener; // 左边按钮点击事件
    private OnClickListener rightClickListener;
    private OnClickListener middleClickListener;// 中间文字点击事件
    private int rightPicId;  //如果要更换图标的话 传入
    private int leftPicId;
    private int type; // text类型 0：简单类型 1：复杂类型
    private String topStr; // 复杂类型的上面文字
    private String bottomStr;// 复杂类型的下面文字

    public String getTxtStr() {
        return txtStr;
    }

    public void setTxtStr(String txtStr) {
        this.txtStr = txtStr;
    }

    public OnClickListener getLeftBtnListener() {
        return leftBtnListener;
    }

    public void setLeftBtnListener(OnClickListener leftBtnListener) {
        this.leftBtnListener = leftBtnListener;
    }

    public OnClickListener getRightClickListener() {
        return rightClickListener;
    }

    public void setRightClickListener(OnClickListener rightClickListener) {
        this.rightClickListener = rightClickListener;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTopStr() {
        return topStr;
    }

    public void setTopStr(String topStr) {
        this.topStr = topStr;
    }

    public String getBottomStr() {
        return bottomStr;
    }

    public void setBottomStr(String bottomStr) {
        this.bottomStr = bottomStr;
    }

    public OnClickListener getMiddleClickListener() {
        return middleClickListener;
    }

    public void setMiddleClickListener(OnClickListener middleClickListener) {
        this.middleClickListener = middleClickListener;
    }

    public int getRightPicId() {
        return rightPicId;
    }

    public void setRightPicId(int rightPicId) {
        this.rightPicId = rightPicId;
    }

    public int getLeftPicId() {
        return leftPicId;
    }

    public void setLeftPicId(int leftPicId) {
        this.leftPicId = leftPicId;
    }

}
