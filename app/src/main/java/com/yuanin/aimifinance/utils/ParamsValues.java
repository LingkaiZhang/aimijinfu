package com.yuanin.aimifinance.utils;

/**
 * @author huangxin
 * @date 2015/10/23
 * @desc 值静态变量
 */
public class ParamsValues {
    /**
     * net网络配置
     */
    //appid
    public final static String APP_ID = "20013396";
    //key
    public final static String KEY = "5bbf31b975954ffb90a19e69ef274c0f";
    //正式环境

   // public final static String NET_URL = "https://shapi.aimilicai.com/";

  //  public final static String NET_URL = "http://csapi.yuanin.com/";
    //key
    public final static String VERSION_CODE = "6.0.0";
    //测试
    public final static String NET_URL = "http://javadjshcsp2p.yuanin.com/";
    public final static String NET_URL_WEIXIN = "http://javadjshwechat.yuanin.com/";
    public final static String TEL = "400-666-2082";
    /**
     * 网络请求模块参数
     */
    //用户模块
    public final static String MODULE_USER = "user";
    //安全中心
    public final static String MODULE_SAFE = "safe";
    //资金
    public final static String MODULE_FUND = "fund";
    //商品
    public final static String MODULE_PRODUCT = "product";
    //短信
    public final static String MODULE_SMS = "sms";
    //其它
    public final static String MODULE_OTHER = "other";
    //投资
    public final static String MODULE_INVEST = "invest";

    /**
     * 网络请求方法参数
     */
    //登录
    public final static String MOTHED_LOGIN = "login";
    //发送短信
    public final static String MOTHED_SEND = "send";
    public final static String SEND_REGISTER = "register";
    public final static String SEND_FIND_PASSWORD = "forgetpwd";
    public final static String SEND_CHANGE_MOBILE = "change_mobile";
    //注册
    public final static String MOTHED_REGISTER = "register";
    //找回密码
    public final static String MOTHED_FIND_PWD = "forgetpassword";
    //开户
    public final static String MOTHED_PERSONAL_REGISTER = "personal_register";
    //充值
    public final static String MOTHED_HK_RECHARGE = "haikou_recharge";
    //激活
    public final static String MOTHED_ACTIVATE_UESR = "activate_user";
    //绑定银行卡
    public final static String MOTHED_SAVE_BANK_CARD = "savebankcard";

    //修改登录密码
    public final static String MOTHED_UPDATE_PASSWORD = "updatepassword";
    //获取我的账户信息(new)
    public final static String MOTHED_USER_ACCOUNT_NEW = "useraccount_new";

    //首页滚动栏+公告new
    public final static String MOTHED_BANNER_NOTICE_NEW = "bannernotice_new";
    //获取理财商品列表信息(新)
    public final static String MOTHED_GET_NEW_PRODUCT = "getnewproductlist";
    //获取首页商品信息
    public final static String MOTHED_GET_INDEX_PRODUCT = "indexproductlist";
    //获取个人实名信息
    public final static String MOTHED_GET_CERTIFIED = "getcertified";
    //购买理财商品
    public final static String MOTHED_BUY_PRODUCT = "buyinvestproduct";
    //关于我们
    public final static String MOTHED_ABOUT = "about";
    //资金流水新
    public final static String MOTHED_FUND_LOG_NEW = "fundlog_new";
    //解绑HK银行卡
    public final static String MOTHED_UNBINDING_HK_BANK_CARD = "unbindinghaikoubankcard";
    //修改银行预留手机号
    public final static String MOTHED_CHANGE_BANK_MOBILE = "changebankmobile";
    //修改交易密码
    public final static String MOTHED_CHANGE_HK_PWD = "changehaikoupassword";
    //错误日志提交
    public final static String MOTHED_ERROR_MSG = "errormsg";
    //账户总资产
    public final static String TOTAL_ACCOUNT_AMOUNT = "totalaccountamount";
    //账户可用余额
    public final static String TOTAL_USER_ACCOUNT_AMOUNT = "useraccountamount";
    //账户累计收益
    public final static String GET_USER_ACCUMULATED_INCOME = "getuseraccumulatedincome";
    //我的资产
    public final static String GET_INVEST_PROJECT_LIST = "getinvestprojectlist";
    //我的红包
    public final static String GET_GIFT_PRODUCT_LIST = "getgiftproductlist";
    //项目详情
    public final static String GET_PRODUCT_DETAIL = "getproductdetailbytype";
    //获取购买商品详情
    public final static String GET_BUY_PRODUCT_DETAIL = "getbuyproductdetail";
    //投资记录
    public final static String GET_PRODUCT_INVSET_LOG = "getproductinvestlog";
    //还款计划
    public final static String GET_REPAY_PLAN = "get_repay_plan";
    //往期项目
    public final static String PERIOD_PROJECT = "periodproject";
    //使用体验金
    public final static String USE_EXPERIENCE_GOLD = "useexperiencegold";
    //修改绑定手机
    public final static String CHANGE_REGISTER_MOBILE = "changeregistermobile";
    //反馈意见
    public final static String FEEDBACK = "feedback";
    //邀请好友图片
    public final static String GET_SHARE_INVITE_FRIENDS = "getshareinvitefriends";
    //邀请好友列表
    public final static String GET_INVITE_FRIENDS_LIST = "getinvitefriendslist";
    //公告列表
    public final static String MOTHED_NOTICE_LIST = "noticelist";
    //获取手续费和次数
    public final static String MOTHED_AIMI_CASH_FEE = "aimi_cash_fee";
    //HK提现
    public final static String MOTHED_HK_CASH = "haikou_cash";
    //获取海口银行卡
    public final static String MOTHED_GET_HK_BANK = "get_haikou_bank";

    //订单详情
    public final static String MOTHED_GET_INVEST_DETAIL = "getinvestdetail";
    //获取充值绑定银行卡信息
    public final static String MOTHED_HK_BANK_AMOUNT = "haikou_bank_amount";

    //检查版本更新
    public final static String MOTHED_VERSION = "version";
    //分享红包状态
    public final static String MOTHED_GET_SHARE_RED_STATUS = "getshareredstatus";
    //新分享红包
    public final static String MOTHED_GET_SHARE_RED_INFO_NEW = "getshareredinfo_new";
    //获取红包分享信息
    public final static String MOTHED_GET_SHARE_RED_INFO = "getshareredinfo";
    //请求进入广告页
    public final static String MOTHED_INTRO = "intro";
    //自动投资列表
    public final static String MOTHED_AUTO_LIST = "auto_list";
    //新增编辑自动投资
    public final static String MOTHED_AUTO_SAVE = "auto_save";
    //开启关闭删除自动投资
    public final static String MOTHED_AUTO_DELETE = "auto_delete";
    //自动投资记录
    public final static String MOTHED_AUTO_LOG_LIST = "auto_log_list";

    //TODO 6.0.0 新版本接口
    //获取首页商品信息(6.0.0)
    public final static String MOTHED_NEW_INDEX_PRODUCT = "newIndexproductlist";
    //获取理财商品列表信息(新)
    public final static String MOTHED_GET_PRODUCT_LIST = "getproductlist";
    //回款日历
    public final static String MOTHED_USER_REPAY_CALENDER = "repayCalender";
    //订单详情
    public final static String MOTHED_GET_NEW_INVEST_DETAIL = "getNewInvestdetail";
    //活动信息
    public final static String MOTHED_GET_ACTIVITY_INFO = "getActivityInfo";
    //获取手机图型验证码
    public final static String MODULE_CAPTCHA= "captcha";
    public final static String MOTHED_GET_CAPTCHA = "getCaptcha";
    //登录注册手机号验证
    public final static String MOTHED_REGISTER_VERIFY_MOBILE= "registerverifymobile";
    //注册验证码验证
    public final static String MOTHED_REGISTER_VERIFY_CODE= "registerverifycode";
    //新的注册
    public final static String MOTHED_REGISTER_NEW = "registerNew";
    //我的资产(新)
    public final static String GET_NEW_INVEST_PROJECT_LIST = "getNewInvestprojectlist";
    //找回密码验证短信验证码
    public final static String MOTHED_SMS_VERIFICATION = "smsVerification";
    //找回密码(新)
    public final static String MOTHED_UPDATE_PASSWORD_NEW = "updatepasswordNew";
    //登录接口(新)
    public final static String MOTHED_VIP_LOGIN_NEW = "viplogin";
    //我的消息
    public final static String MOTHED_MY_MESSAGE = "mymessage";


    /**
     * 导航页share values
     */
    public final static String GUIDE_VALUE = "GuideShare6";

    /**
     * 关于我们
     */
    //帮助中心
    public final static String HELP = "help";
    //注册协议
    public final static String REGISTER_PROTOCOL = "register_protocol";
    //购买协议
    public final static String BUY_PROTOCOL = "buy_protocol";
    //自动投资协议
    public final static String AUTO_INVEST_PROTOCOL = "auto_invest_protocol";
    //安全保障
    public final static String SAFE = "safe";
    //红包规则
    public final static String RULE = "rule";
    //添加银行卡
    public final static String ADD_BANK = "add_bank";
    //公告详情
    public final static String NOTICE_DETAIL = "notice_detail";
    //分享红包
    public final static String SHARE_RED_PACKET = "share_red_packet";
    //平台数据
    public final static String DATA_REPORT = "data_report";
    //分享新红包
    public final static String SHARE_NEW_RED_PACKET = "share_new_red_packet";
    //调查问卷
    public final static String QUESTION_NAIRE = "question_naire";
    //借款合同范本
    public final static String MODEL_LOAN_CONTRACT = "model_loan_contract";
    //出借风险提示及禁止性行为说明书
    public final static String LOAN_RISK_STATEMENT = "Loan_risk_statement";
    //我要借款
    public final static String MY_BORROW = "my_borrow";
    //银行存管
    public final static String BANK_DEPOSITORY = "bank_depository";
}
