package com.yuanin.aimifinance.inter;

import com.yuanin.aimifinance.view.ObservableScrollView;

/**
 * @author huangxin
 * @date 2016/9/26
 * @desc
 */

public interface IScrollViewListener {

    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

}
