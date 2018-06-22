package com.yuanin.aimifinance.utils;

import android.os.Environment;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.entity.ShareGridEntity;

import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/16
 * @time 13:51
 * @desc 静态变量
 */
public class StaticMembers {
    /**
     * 显示格式化日期
     */
    public final static String DATE_FORMAT_STR = "yyyy年MM月dd日 HH:mm";

    //*/ 手势密码点的状态
    public static final int POINT_STATE_NORMAL = 0; // 正常状态

    public static final int POINT_STATE_SELECTED = 1; // 按下状态

    public static final int POINT_STATE_WRONG = 2; // 错误状态


    //屏幕宽高
    public static int SCREEN_WIDTH; // 按下状态

    public static int SCREEN_HEIGHT; // 错误状态
    //*/

    /**
     * 手势密码界面间距
     */
    public static final int GESTURE_WIDTH_GAP = 108;
    public static final int GESTURE_HEIGHT_GAP = 138;
    /**
     * 每页显示数量
     */
    public static final int PAGE_SIZE = 15;

    //是否储存登录信息
    public static boolean IS_NEED_LOGIN = true;
    //是否设置了手势密码
    public static boolean IS_NEED_GUSTURE_PWD = false;
    //是否使用指纹登录
    public static boolean IS_NEED_FINGER_PWD = false;
    //是否需要进入导航页
    public static boolean IS_NEED_GUDIE = false;
    //登录用户userid
    public static String USER_ID;
    //登录用户电话号码
    public static String MOBILE;
    //登录rsa加密后的用户电话号码
    public static String RSA_MOBILE;
    //登录的token值
    public static String LOGIN_TOKEN;
    //激活状态
    public static int HK_STATUS = -1;
    //问卷调查状态
    public static int QUESTION_NAIRE_STATUS = -1;
    //银行卡状态
    public static int BANK_CARD_STATUS = 0;
    //账户总资产
    public static String TOTAL_MONEY = "0.00";
    //是否可以购买新手标 0 不可以1可以
    public static int IS_ABLE_BUY_NEW_PRODUCT = 0;

    //分享list
    public final static String[] SHARE_TEXTS = {"发送给朋友", "分享到朋友圈", "发送给好友", "分享到QQ空间"};
    public final static int[] SHARE_ICONS = {R.drawable.selector_share_wechat, R.drawable.selector_share_wechat_moments, R.drawable.selector_share_qq, R.drawable.selector_share_qzone};
    public static List<ShareGridEntity> SHARE_LIST;

    //时间计时
    public final static long TIME_TOTAL = 61000;
    public final static long TIME_PER = 1000;//ms

    //状态栏高度
    public static int STATUS_HEIGHT;

    //是否显示往期项目
    public static boolean isShowLastItem;
    //缓存对象
    public static ACache aCache;

    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DOWN_PATH = "aimidownload";

    public static String GESTURE_TIP = "简单出借，随行随享";

    //MineFragment是否展现账户总资产;
    public static boolean IS_SHOW_BALANCE = true;
}
