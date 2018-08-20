package com.yuanin.aimifinance.activity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.StaticMembers;
import com.yuanin.aimifinance.view.ObservableWebView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by huangxin on 2018/3/19.
 */

public class WebViewHtmlActivity extends BaseActivity {

    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.wvHref)
    private ObservableWebView wvHref;
    @ViewInject(R.id.btn_risk_indication)
    private Button btn_risk_indication;

    private String type;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_html);
        x.view().inject(this);
        type = getIntent().getStringExtra(ParamsKeys.TYPE);
        userid = getIntent().getStringExtra(ParamsKeys.USER_ID);
        if (type.equals(ParamsValues.MY_BORROW)) {
            initTopBar(getString(R.string.I_want_borrow), toptitleView, true);
            String url = ParamsValues.NET_URL_WEIXIN + "borrow_index.html";
            initWebViewTitle(url);
        } else if (type.equals(ParamsValues.LOAN_RISK_STATEMENT)) {
            initTopBar(getString(R.string.loan_risk_instruction), toptitleView, true);
            String url = ParamsValues.NET_URL_WEIXIN + "instruction.html";
            setView();
            initWebView(url);
        } else if (type.equals(ParamsValues.DEBT_ASSIGNMENT) ) {
            initTopBar("债权转让", toptitleView, true);
            String url = ParamsValues.NET_URL_WEIXIN + "creditor_assets.html?userid=" + StaticMembers.USER_ID +"&mobile=" + AppUtils.rsaEncode(this,StaticMembers.MOBILE);
           // String url = "http://javadjshwechat.yuanin.com/itemsparticularups1.html?myid=19459&buy=0&type=%E8%BF%98%E6%AC%BE%E4%B8%AD";
            initWebViewTitle(url);

        }

    }

    private void setView() {
        btn_risk_indication.setVisibility(View.VISIBLE);
        CountTime  mc = new CountTime(10000, 1000);//第一个参数总计时时间，第二个是间隔时间，单位都是毫秒值
        mc.start();
        btn_risk_indication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(WebViewHtmlActivity.this, WebViewActivity.class);
                intent4.putExtra(ParamsKeys.TYPE, ParamsValues.QUESTION_NAIRE);
                intent4.putExtra(ParamsKeys.USER_ID, StaticMembers.USER_ID);
                startActivity(intent4);
            }
        });
    }

    private void initWebViewTitle(String url) {
            WebSettings settings = wvHref.getSettings();
            settings.setSupportZoom(true);          //支持缩放
            settings.setBuiltInZoomControls(true);  //启用内置缩放装置
            settings.setJavaScriptEnabled(true);    //启用JS脚本
            settings.setDomStorageEnabled(true);
            wvHref.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedSslError(WebView view,
                                               SslErrorHandler handler, SslError error) {
                    //handler.cancel(); 默认的处理方式，WebView变成空白页
                    handler.proceed(); // 接受证书
                }

                //当点击链接时,希望覆盖而不是打开新窗口
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);  //加载新的url
                    return true;    //返回true,代表事件已处理,事件流到此终止
                }


            });

            //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
            wvHref.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && wvHref.canGoBack()) {
                            wvHref.goBack();   //后退
                            return true;    //已处理
                        }
                    }
                    return false;
                }
            });
            wvHref.setWebChromeClient(new WebChromeClient() {
                //当WebView进度改变时更新窗口进度
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                }

                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    initTopBar(title, toptitleView, true);
                }
            });
            wvHref.loadUrl(url);
            //http://csapi.yuanin.com/html/showasset.php?productid=
        }

    private void initWebView(String url) {
        WebSettings settings = wvHref.getSettings();
        settings.setSupportZoom(true);          //支持缩放
        settings.setBuiltInZoomControls(true);  //启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        settings.setDomStorageEnabled(true);
        wvHref.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                //handler.cancel(); 默认的处理方式，WebView变成空白页
                handler.proceed(); // 接受证书
            }

            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);  //加载新的url
                return true;    //返回true,代表事件已处理,事件流到此终止
            }


        });

        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        wvHref.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && wvHref.canGoBack()) {
                        wvHref.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
        wvHref.setWebChromeClient(new WebChromeClient() {
            //当WebView进度改变时更新窗口进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                initTopBar(title, toptitleView, true);
            }
        });
        wvHref.loadUrl(url);
        //http://csapi.yuanin.com/html/showasset.php?productid=
    }

    public class CountTime extends CountDownTimer {

        public CountTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {   ////开始计时调用的函数
            btn_risk_indication.setText("阅读并同意(" + millisUntilFinished / 1000 + "秒)"); //其中millisUntilFinished已经是倒计时的时间了
            btn_risk_indication.setEnabled(false);
        }
        @Override
        public void onFinish() {    //完成计时调用的函数
            btn_risk_indication.setText("点击进行风险测评");
            btn_risk_indication.setEnabled(true);
            btn_risk_indication.setBackground(getResources().getDrawable(R.color.theme_color));
        }
    }
}
