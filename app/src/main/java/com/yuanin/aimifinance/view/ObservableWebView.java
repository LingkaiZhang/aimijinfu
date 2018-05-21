package com.yuanin.aimifinance.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.yuanin.aimifinance.inter.IWebViewListener;

/**
 * @author huangxin
 * @date 2016/9/26
 * @desc
 */

public class ObservableWebView extends WebView {
    private IWebViewListener webViewListener = null;

    public ObservableWebView(Context context) {
        super(context);
    }

    public ObservableWebView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setWebViewListener(IWebViewListener webViewListener) {
        this.webViewListener = webViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (webViewListener != null) {
            webViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

}
