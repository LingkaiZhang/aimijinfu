package com.yuanin.aimifinance.activity;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.utils.AppUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import de.greenrobot.event.EventBus;


public class HKRegisterWebActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.llMain)
    private LinearLayout llMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hk_register);
        x.view().inject(this);
        String url = getIntent().getStringExtra("url");
        initTopBar("加载中...", toptitleView, true);
        if (url != null && url.length() > 0) {
            AgentWeb mAgentWeb = AgentWeb.with(this)//传入Activity
                    .setAgentWebParent(llMain, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                    .useDefaultIndicator()// 使用默认进度条
                    .defaultProgressBarColor() // 使用默认进度条颜色
                    .setWebViewClient(new WebViewClient() {
                        @Override
                        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                            handler.proceed(); // 接受证书
                        }
                    })
                    .setReceivedTitleCallback(new ChromeClientCallbackManager.ReceivedTitleCallback() {
                        @Override
                        public void onReceivedTitle(WebView view, String title) {
                            initTopBar(title, toptitleView, true);
                        }
                    }) //设置 Web 页面的 title 回调
                    .createAgentWeb()//
                    .ready()
                    .go(url);
            mAgentWeb.getJsInterfaceHolder().addJavaObject("jsObj", getHtmlObject());
        }
    }

    private Object getHtmlObject() {
        Object insertObj = new Object() {
            @JavascriptInterface
            public void HtmlcallJava() {
//                Toast.makeText(SinaPayActivity.this, "接受到调用 ", Toast.LENGTH_SHORT).show();
            }

            @JavascriptInterface
            public void HtmlcallJava2(String param) {
                if (param.equals("1")) {
                    //刷新个人中心
                    EventMessage eventMessage = new EventMessage();
                    eventMessage.setType(EventMessage.REFRESH_MINE);
                    EventBus.getDefault().post(eventMessage);
                    AppUtils.showToast(HKRegisterWebActivity.this, "操作成功");
                } else {
                    AppUtils.showToast(HKRegisterWebActivity.this, "操作失败，请重试");
                }
                HKRegisterWebActivity.this.finish();
            }
        };
        return insertObj;
    }
}
