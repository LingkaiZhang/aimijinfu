package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2017/3/6
 * @desc
 */
public class EventMessage implements Serializable {

    public final static int UPDATE_INDEX_LOGIN = 1;
    public final static int REFRESH_MINE = 2;
    public final static int REFRESH_FINANCE_PRODUCT = 3;
    public final static int HOMEPAGE_JUMP_TAB = 4;
    public final static int FINISH_PWD_ACTIVITY = 5;
    public final static int FINISH_BANK_ACTIVITY = 6;
    public final static int REFRESH_INDEX = 8;
    public final static int UPDATE_INDEX_TOTAL = 9;
    private int type;
    private Object object;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
