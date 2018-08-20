
package com.yuanin.aimifinance.fragment;


import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseFragment;
import com.yuanin.aimifinance.entity.DebtProductDetailEntity;
import com.yuanin.aimifinance.entity.ProductDetailEntity;
import com.yuanin.aimifinance.inter.IScrollViewListener;
import com.yuanin.aimifinance.utils.LogUtils;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.view.ObservableScrollView;
import com.yuanin.aimifinance.view.ObservableWebView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 项目详情
 */
public class DebtProductIntroduceFragment extends BaseFragment {
    @ViewInject(R.id.tvGuarantee)
    private TextView tvGuarantee;
    @ViewInject(R.id.tvGuaranteeCon)
    private TextView tvGuaranteeCon;
    @ViewInject(R.id.tvDebtstype)
    private TextView tvDebtstype;
    @ViewInject(R.id.tvDebtstypeCon)
    private TextView tvDebtstypeCon;
    @ViewInject(R.id.tvRepay)
    private TextView tvRepay;
    @ViewInject(R.id.tvRepayCon)
    private TextView tvRepayCon;

    @ViewInject(R.id.innerscrollview)
    private ObservableScrollView innerscrollview;

    @ViewInject(R.id.wvHref)
    private ObservableWebView wvHref;

    private DebtProductDetailEntity productDetailEntity;
    public static boolean isTop = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_introduce, container, false);
        x.view().inject(this, view);
       /* tvGuarantee.setText(productDetailEntity.getGuarantee_method());
        tvRepay.setText(productDetailEntity.getRepay_method());
        tvDebtstype.setText(productDetailEntity.getDebtstype());
        tvGuaranteeCon.setText(productDetailEntity.getGuarantee_con());
        tvRepayCon.setText(productDetailEntity.getRepay_con());
        tvDebtstypeCon.setText(productDetailEntity.getDebtstype_con());*/
        innerscrollview.setScrollViewListener(new IScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (innerscrollview.getScrollY() == 0) {
                    isTop = true;
                } else {
                    isTop = false;
                }
            }
        });

        initWebView();
        return view;
    }

    private void initWebView() {
        if (productDetailEntity != null) {
            WebSettings settings = wvHref.getSettings();
            settings.setSupportZoom(true);          //支持缩放
            settings.setBuiltInZoomControls(true);  //启用内置缩放装置
            settings.setJavaScriptEnabled(true);    //启用JS脚本
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
                }
            });
            //TODO
            wvHref.loadUrl(ParamsValues.NET_URL_WEIXIN + "creditor_itmesParticularAndior.html");
            LogUtils.d("lingkai",ParamsValues.NET_URL_WEIXIN + "creditor_itmesParticularAndior.html" );
            //http://csapi.yuanin.com/html/showasset.php?productid=
        }
    }

    public void setDatas(DebtProductDetailEntity productDetailEntity) {
        this.productDetailEntity = productDetailEntity;
    }
}

