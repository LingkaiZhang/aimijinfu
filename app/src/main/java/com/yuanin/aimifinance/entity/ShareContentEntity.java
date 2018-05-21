package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2015/11/4
 * @desc
 */
public class ShareContentEntity implements Serializable {
    private String title;// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
    private String titleUrl; //titleUrl是标题的网络链接，仅在人人网和QQ空间使用
    private String text;  // text是分享文本，所有平台都需要这个字段
    private String imagePath;// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    private String imageUrl; //网络图片的url：所有平台
    private String url; // url仅在微信（包括好友和朋友圈）中使用
    private String comment; // comment是我对这条分享的评论，仅在人人网和QQ空间使用
    private String site;// site是分享此内容的网站名称，仅在QQ空间使用

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    private String siteUrl; // siteUrl是分享此内容的网站地址，仅在QQ空间使用

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitleUrl() {
        return titleUrl;
    }

    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
    }


}
