package com.yuanin.aimifinance.inter;


import com.yuanin.aimifinance.view.ObservableWebView;

/**
 * @author huangxin
 * @date 2016/9/26
 * @desc
 */

public interface IWebViewListener {

    void onScrollChanged(ObservableWebView scrollView, int x, int y, int oldx, int oldy);

}
