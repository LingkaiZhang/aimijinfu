package com.yuanin.aimifinance.base;

import android.app.Application;
import android.util.DisplayMetrics;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cookie.store.PersistentCookieStore;
import com.lzy.okhttputils.model.HttpParams;
import com.yuanin.aimifinance.BuildConfig;
import com.yuanin.aimifinance.entity.ShareGridEntity;
import com.yuanin.aimifinance.utils.ACache;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.CrashHandler;
import com.yuanin.aimifinance.utils.LogUtils;
import com.yuanin.aimifinance.utils.StaticMembers;


import org.xutils.x;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;

/**
 * @author huangxin
 * @date 2015/10/30
 * @desc
 */
public class App extends Application {

    private static App instance;

    public App() {

    }

    /**
     * 单一实例
     */
    public static App getApp() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        Fresco.initialize(this);
        ShareSDK.initSDK(this);
        //友盟推送
       /* PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
                Log.e("hx", s);
                LogUtils.d("zlk","mydeviceToken = " + s);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("hx", s + "---" + s1);
                LogUtils.d("zlk","mydeviceToken =" + s1);
            }
        });

        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                switch (msg.builder_id) {
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }

            @Override
            public PendingIntent getClickPendingIntent(Context context, UMessage uMessage) {
                Intent intent = new Intent(context, GuidePageActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                return pendingIntent;
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
*/
        //LogCat日志工具
        if (BuildConfig.LOG_DEBUG) {
            LogUtils.isShowLog = true;
        } else {
            LogUtils.isShowLog = false;
        }

        if (false) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }
        init();
        initOkhttp();
        AppUtils.initBooleanData(this);
    }

    private void initOkhttp() {
        HttpParams params = new HttpParams();
        //        params.put("commonParamsKey1", "commonParamsValue1");     //所有的 params 都 支持 中文
        //        params.put("commonParamsKey2", "这里支持中文参数");

        //必须调用初始化
        OkHttpUtils.init(this);
        //以下都不是必须的，根据需要自行选择
        OkHttpUtils.getInstance()//
                .debug("OkHttpUtils")                                              //是否打开调试
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                 //全局的写入超时时间
                //                .setCookieStore(new MemoryCookieStore())                           //cookie使用内存缓存（app退出后，cookie消失）
                .setCookieStore(new PersistentCookieStore())                       //cookie持久化存储，如果cookie不过期，则一直有效
                //                .addCommonHeaders(headers)                                         //设置全局公共头
                .addCommonParams(params);
    }


    private void init() {
        if (StaticMembers.SHARE_LIST == null) {
            StaticMembers.SHARE_LIST = new ArrayList<ShareGridEntity>();
            for (int i = 0; i < StaticMembers.SHARE_TEXTS.length; i++) {
                ShareGridEntity entity = new ShareGridEntity();
                entity.setTextName(StaticMembers.SHARE_TEXTS[i]);
                entity.setIconId(StaticMembers.SHARE_ICONS[i]);
                StaticMembers.SHARE_LIST.add(entity);
            }
        }

        StaticMembers.aCache = ACache.get(this);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int statusBarHeight1 = (int) Math.ceil(25 * metrics.density);
        StaticMembers.STATUS_HEIGHT = AppUtils.getStatusHeight(this);
        StaticMembers.SCREEN_WIDTH = metrics.widthPixels;
        StaticMembers.SCREEN_HEIGHT = metrics.heightPixels;
    }
}
