package com.yuanin.aimifinance.entity;

import java.io.Serializable;

/**
 * @author huangxin
 * @date 2016/1/14
 * @desc
 */
public class InviteFriendTopEntity implements Serializable {

    /**
     * shareurlurl : http://www.aimilicai.com/wxtg/?userid=7800&giftid=325&token=c6b611e4ffe6a8213964c4f1dd1289f2
     * sharetitle : 爱米金服，稳健高收益，理财无风险。
     * sharedescript : 爱米金服，您的理财首选！稳定高收益的互联网理财产品，最高年化收益可达12%！
     * img : http://api.yuanin.com/images/invite.png
     */

    private String shareurlurl;
    private String sharetitle;
    private String sharedescript;
    private String img;
    private String sharelogo;

    public String getSharelogo() {
        return sharelogo;
    }

    public void setSharelogo(String sharelogo) {
        this.sharelogo = sharelogo;
    }

    public void setShareurlurl(String shareurlurl) {
        this.shareurlurl = shareurlurl;
    }

    public void setSharetitle(String sharetitle) {
        this.sharetitle = sharetitle;
    }

    public void setSharedescript(String sharedescript) {
        this.sharedescript = sharedescript;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getShareurlurl() {
        return shareurlurl;
    }

    public String getSharetitle() {
        return sharetitle;
    }

    public String getSharedescript() {
        return sharedescript;
    }

    public String getImg() {
        return img;
    }

    @Override
    public String toString() {
        return "InviteFriendTopEntity{" +
                "shareurlurl='" + shareurlurl + '\'' +
                ", sharetitle='" + sharetitle + '\'' +
                ", sharedescript='" + sharedescript + '\'' +
                ", img='" + img + '\'' +
                ", sharelogo='" + sharelogo + '\'' +
                '}';
    }
}
