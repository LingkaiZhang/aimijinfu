package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2015/10/26
 * @desc 首页滚动栏
 */
public class BannerEntity implements Serializable {

    private String src;
    private String href;
    private int sort;

    public int getIs_red() {
        return is_red;
    }

    public void setIs_red(int is_red) {
        this.is_red = is_red;
    }

    private int is_red;

    public void setSrc(String src) {
        this.src = src;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getSrc() {
        return src;
    }

    public String getHref() {
        return href;
    }

    public int getSort() {
        return sort;
    }
}
