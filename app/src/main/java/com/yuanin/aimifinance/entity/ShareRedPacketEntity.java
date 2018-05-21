package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/7/29
 * @desc
 */
public class ShareRedPacketEntity implements Serializable {
    private int status;
    private String red_html;
    private String shareurlurl;
    private String sharetitle;
    private String sharedescript;
    private String img;
    private String sharelogo;

    public String getShareurlurl() {
        return shareurlurl;
    }

    public void setShareurlurl(String shareurlurl) {
        this.shareurlurl = shareurlurl;
    }

    public String getSharetitle() {
        return sharetitle;
    }

    public void setSharetitle(String sharetitle) {
        this.sharetitle = sharetitle;
    }

    public String getSharedescript() {
        return sharedescript;
    }

    public void setSharedescript(String sharedescript) {
        this.sharedescript = sharedescript;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSharelogo() {
        return sharelogo;
    }

    public void setSharelogo(String sharelogo) {
        this.sharelogo = sharelogo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRed_html() {
        return red_html;
    }

    public void setRed_html(String red_html) {
        this.red_html = red_html;
    }
}
