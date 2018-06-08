package com.yuanin.aimifinance.entity;

public class ActivityInfoEntity {

    /**
     * start_time : 2018-01-30
     * end_time : 2018-02-28
     * show_page : index
     * id : 1
     * title : 新年红包
     * weights : 0
     * url_route : http://www.baidu.com
     * cover_img : https://shres.aimilicai.com/assets/2017/active/newyear/pchbbanner.png
     */

    private String start_time;
    private String end_time;
    private String show_page;
    private int id;
    private String title;
    private int weights;
    private String url_route;
    private String cover_img;

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getShow_page() {
        return show_page;
    }

    public void setShow_page(String show_page) {
        this.show_page = show_page;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWeights() {
        return weights;
    }

    public void setWeights(int weights) {
        this.weights = weights;
    }

    public String getUrl_route() {
        return url_route;
    }

    public void setUrl_route(String url_route) {
        this.url_route = url_route;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }
}
