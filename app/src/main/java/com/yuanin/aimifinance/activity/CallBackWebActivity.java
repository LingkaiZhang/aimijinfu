package com.yuanin.aimifinance.activity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.StaticMembers;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class CallBackWebActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.llMain)
    private LinearLayout llMain;

    private String baseUrl;
    private AgentWeb mAgentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_back_web);
        x.view().inject(this);
        String requestUrl = getIntent().getStringExtra("url");
        baseUrl = getIntent().getStringExtra("baseUrl");
        initTopBar("加载中...", toptitleView, true);
        mAgentWeb = AgentWeb.with(this)//传入Activity
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
                .go(requestUrl);
        mAgentWeb.getJsInterfaceHolder().addJavaObject("jsObj", getHtmlObject());
    }

    private Object getHtmlObject() {
        Object insertObj = new Object() {
            @JavascriptInterface
            public void HtmlcallJava() {
                startActivityForResult(new Intent(CallBackWebActivity.this, LoginRegisterActivity.class), 12);
                Toast.makeText(CallBackWebActivity.this, "请您先登录用户", Toast.LENGTH_SHORT).show();
            }

            @JavascriptInterface
            public void HtmlcallJava2(final String param) {

            }
        };
        return insertObj;
    }

    @Override    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && !StaticMembers.IS_NEED_LOGIN) {
            StringBuffer sb = new StringBuffer();
            sb.append(baseUrl);
            sb.append("?appid=" + ParamsValues.APP_ID);
            sb.append("&uid=" + StaticMembers.USER_ID);
            sb.append("&token=" + AppUtils.getMd5Value(StaticMembers.USER_ID + StaticMembers.MOBILE));
            mAgentWeb.getLoader().loadUrl(sb.toString());
        }
    }
}
