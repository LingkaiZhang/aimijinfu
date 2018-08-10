package com.yuanin.aimifinance.utils;

/**
 * @author huangxin
 * @date 2015/10/23
 * @desc key静态变量
 */
public class ParamsKeys {
    /**
     * 网络请求key值
     */
    public final static String MODULE = "module";
    public final static String MOTHED = "mothed";
    public final static String TOKEN = "token";
    public final static String KEY = "key";
    public final static String APPID = "appid";

    //登录
    public final static String MOBILE = "mobile";
    public final static String PASSWORD = "password";


    //注册
    public final static String TYPE = "type";
    public final static String VERIFYCODE = "verifycode";
    public final static String IMAGECODE = "imageCode";
    public final static String SMSCODE = "smsCode";


    //开户
    public final static String USER_ID = "userid";
    public final static String REAL_NAME = "realname";
    public final static String ID_NUMBER = "idnumber";
    public final static String CARD_NO = "card_no";
    public final static String CARD_MOBILE = "card_mobile";

    //修改登录密码
    public final static String OLD_PASSWORD = "oldpassword";
    public final static String NEW_PASSWORD = "newpassword";

    //购买商品
    public final static String PRODUCT_ID = "productid";
    public final static String BUY_QTY = "buyqty";
    public final static String GIFT_ID = "giftid";

    public final static String INVEST_ID = "invest_id";
    //我的商品列表
    public final static String PAGE_QTY = "pageqty";
    public final static String CURRENT_PAGE = "currentpage";

    //资金流水
    public final static String TIME_TYPE = "timetype";
    public final static String FUND_TYPE = "fundtype";
    public final static String STATUS = "status";

    //充值
    public final static String AMOUNT = "amount";

    //异常
    public final static String RESULT = "result";
    public final static String ERROR = "error";
    public final static String PARAMS = "params";

    //修改绑定手机
    public final static String NEW_MOBILE = "newmobile";

    //反馈意见
    public final static String MESSAGE = "message";

    public static final String VERSION_NUM = "version_num";
    public static final String LOGIN_TOKEN = "login_token";


    public final static String ID = "id";

    //
    /**
     * 手势密码SharedPreferences变量名
     */
    public static final String GESTURE_SHARE_FILE_NAME = "GesturePwdFile";
    public static final String GESTURE_SHARE_DATA_NAME = "GesturePwdData";
    public static final String GESTURE_FLAG_MODIFY = "GestureModify";
    public static final String GESTURE_FLAG_EDIT = "GestureEdit";
    public static final String GESTURE_FLAG = "GestureFlag";
    public static final String GESTURE_FLAG_FIRST_EDIT = "GestureFirstEdit";

    /**
     * 指纹SharedPreferences变量名
     */
    public static final String FINGER_PWD_SHARE_FILE_NAME = "FingerPwdFile";
    public static final String FINGER_PWD_SHARE_DATA_NAME = "FingerPwdData";

    /**
     * 导航页SharedPreferences判断变量名
     */
    public static final String GUIDE_FILE = "Guide_File2";
    public static final String GUIDE_KEY = "Guide_Key2";

    /**
     * 登录SharedPreferences判断变量名
     */
    public static final String LOGIN_FILE = "Login_File";
    public static final String LOGIN_KEY_MOBILE = "Login_Key_Mobile";
    public static final String LOGIN_KEY_USERID = "Login_Key_Userid";
    public static final String LOGIN_KEY_TOKEN = "Login_Key_Token";
    public static final String LOGIN_KEY_TOTAL = "Login_Key_Total";
    public static final String LOGIN_MBOILE_FILE = "Login_Mobile_File";
    public static final String LOGIN_MBOILE = "Login_Mobile";

    public static final String AD_FILE = "Ad_File";
    public static final String AD_CODE = "Ad_Code";
    public static final String AD_COUNT = "Ad_Count";

    public static final String TIPS_FILE = "Tips_File";
    public static final String TIPS_GESTURE = "Tips_Gesture";
    /**
     * bundle传递key
     */
    public static final String REFRESH_HOME_PAGE = "REFRESH_HOME_PAGE";
    //    public static final String REFRESH_INDEX = "REFRESH_INDEX";
    public final static String SINA_PAY_WHERE = "sina_pay_where";

    /**
     * 缓存key
     */
    public static final String BANNER_ENTITY = "bannner_entity";
    public static final String INDEX__ENTITY = "index_entity";
    public static final String PRODUCT_ENTITY = "product_entity";
    public static final String PRODUCT_ENTITY_DEBT = "product_entity_debt";
    public static final String MINE_INFO = "mine_info";

    /*
     * 个人主页保存信息
     */
    public static final String IS_SHOW_BALANCE = "is_show_balance";
    public static final String MINE_FILE = "mine_file";
    public static final String USER_IS_OPEN_ACCOUNT = "user_is_open_account";
    public static final String USER_INFO_FILE = "user_info_file";

    public static final String SMS_VERIFY_CODE = "verifycode";
    public static final String USER_IS_ABLE_BUY_NEW_PRODUCT = "buyNewAble";

    /*
     * 提现
     * */
    public static final String CASH_TYPE ="cash_type";
}
