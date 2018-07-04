package com.yuanin.aimifinance.activity;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.ViewPagerFragmentAdapter;
import com.yuanin.aimifinance.base.BaseFragmentActivity;
import com.yuanin.aimifinance.entity.TabIndicatorEntity;
import com.yuanin.aimifinance.fragment.RedPacketsFragment;
import com.yuanin.aimifinance.fragment.TryMoneyFragment;
import com.yuanin.aimifinance.utils.ViewPagerUtils;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class RedPacketsActivity extends BaseFragmentActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvPackets)
    private TextView tvPackets;
    @ViewInject(R.id.tvTry)
    private TextView tvTry;
    @ViewInject(R.id.mViewPager)
    private ViewPager mViewPager;
    @ViewInject(R.id.cursor)
    private ImageView cursor;
    @ViewInject(R.id.llMain)
    public LinearLayout llMain;

    private List<TextView> textViews;
    private int fragmentPosition = 0;
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_packets);
        x.view().inject(this);
        initTopBar("我的红包", toptitleView, true);
        if (textViews == null) {
            textViews = new ArrayList<TextView>();
        }
        textViews.add(tvPackets);
        textViews.add(tvTry);
        InitImageView();
        initViewPager();
    }

    // 点击事件
    @Event(value = {R.id.tvPackets, R.id.tvTry})
    private void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvPackets:
                fragmentPosition = 0;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 0, textViews);
                break;
            case R.id.tvTry:
                fragmentPosition = 1;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 1, textViews);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        ViewPagerUtils.changeTextViewStyle_FINANCE(this, position, textViews);
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        bmpW = screenW / 2;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cursor.getLayoutParams();
        lp.width = bmpW;
        offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    private void initViewPager() {
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setOffscreenPageLimit(2);
        // 包含3个fragment界面
        List<TabIndicatorEntity> list = ViewPagerUtils.getTabIndicator(2);
        // 3个fragment界面封装
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new RedPacketsFragment());
        fragmentList.add(new TryMoneyFragment());
        // 设置ViewPager适配器
        ViewPagerFragmentAdapter viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), list, fragmentList);
        mViewPager.setAdapter(viewPagerFragmentAdapter);
        mViewPager.setCurrentItem(fragmentPosition, false);
        ViewPagerUtils.changeTextViewStyle_FINANCE(RedPacketsActivity.this, fragmentPosition, textViews);
    }


    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    }
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
            ViewPagerUtils.changeTextViewStyle_FINANCE(RedPacketsActivity.this, arg0, textViews);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
