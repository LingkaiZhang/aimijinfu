package com.yuanin.aimifinance.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.yuanin.aimifinance.inter.IScrollViewListener;

/**
 * @author huangxin
 * @date 2016/9/26
 * @desc
 */

public class ObservableScrollView extends ScrollView {
    private IScrollViewListener scrollViewListener = null;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(IScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

}