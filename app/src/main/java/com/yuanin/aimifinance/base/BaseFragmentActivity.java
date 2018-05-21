package com.yuanin.aimifinance.base;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.entity.TopBarInfo;
import com.yuanin.aimifinance.utils.AppManager;
import com.yuanin.aimifinance.utils.TopBarUtil;


public abstract class BaseFragmentActivity extends FragmentActivity implements OnPageChangeListener {
    private TextView titleTextView;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
//        PushAgent.getInstance(this).onAppStart();
        if (animation == null) {
            animation = AnimationUtils.loadAnimation(this, R.anim.top_title_enter);
        }
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }

    @Override
    public void onPageScrolled(int position, float arg1, int arg2) {

    }

    public abstract void onPageSelected(int position);

    /**
     * 给TopBar赋值
     *
     * @param name ：TopBar中间的名称
     * @param view ：TopBar的View
     */
    protected void initTopBar(String name, View view, boolean hasBack) {
        TopBarInfo topBarInfo = new TopBarInfo();
        topBarInfo.setTxtStr(name);
        titleTextView = (TextView) view.findViewById(R.id.tv_title);
        if (hasBack) {
            topBarInfo.setLeftBtnListener(new TopBarUtil().new AlwaysUseListener(this));
        }
        TopBarUtil.setTopBarInfo(view, topBarInfo);
    }

    protected void initTopBarWithRightText(String name, View view, View.OnClickListener rightLis, String bottomStr) {
        TopBarInfo topBarInfo = new TopBarInfo();
        topBarInfo.setTxtStr(name);
        titleTextView = (TextView) view.findViewById(R.id.tv_title);
        topBarInfo.setLeftBtnListener(new TopBarUtil().new AlwaysUseListener(this));
        topBarInfo.setRightClickListener(rightLis);
        topBarInfo.setBottomStr(bottomStr);
        TopBarUtil.setTopBarInfoWithRightText(view, topBarInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (titleTextView != null) {
            titleTextView.startAnimation(animation);
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
