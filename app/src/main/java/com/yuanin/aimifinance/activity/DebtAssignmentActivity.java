package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.ViewPagerFragmentAdapter;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.base.BaseFragmentActivity;
import com.yuanin.aimifinance.entity.TabIndicatorEntity;
import com.yuanin.aimifinance.fragment.DebtAbleAssignmentFragment;
import com.yuanin.aimifinance.fragment.DebtAduitAssignmentFragment;
import com.yuanin.aimifinance.fragment.DebtTransferedFragment;
import com.yuanin.aimifinance.fragment.DebtTransferingFragment;
import com.yuanin.aimifinance.fragment.MyInvestCollectFragment;
import com.yuanin.aimifinance.fragment.MyInvestDoingFragment;
import com.yuanin.aimifinance.fragment.MyInvestFailFragment;
import com.yuanin.aimifinance.fragment.MyInvestFinishFragment;
import com.yuanin.aimifinance.utils.ViewPagerUtils;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class DebtAssignmentActivity extends BaseFragmentActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvDoing)
    private TextView tvDoing;
    @ViewInject(R.id.tvCollecting)
    private TextView tvCollecting;
    @ViewInject(R.id.tvFail)
    private TextView tvFail;
    @ViewInject(R.id.tvFinish)
    private TextView tvFinish;
    @ViewInject(R.id.mViewPager)
    private ViewPager mViewPager;
    @ViewInject(R.id.cursor)
    private ImageView cursor;


    private List<TextView> textViews;
    private List<Fragment> fragmentList;
    private int fragmentPosition = 0;
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private Animation animation;
    private Context context = DebtAssignmentActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_assignment);
        x.view().inject(this);
        initTopBarWithRightText("债权转让", toptitleView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, "转让说明");

        if (textViews == null) {
            textViews = new ArrayList<TextView>();
        }
        textViews.add(tvCollecting);
        textViews.add(tvDoing);
        textViews.add(tvFinish);
        textViews.add(tvFail);

        InitImageView();
        initViewPager();
    }

    @Event(value = {R.id.btnRefresh, R.id.imgview_back, R.id.tvDoing, R.id.tvCollecting, R.id.tvFail, R.id.tvFinish, R.id.llTitle})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tvDoing:
                fragmentPosition = 1;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 1, textViews);
                break;
            case R.id.tvCollecting:
                fragmentPosition = 0;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 0, textViews);
                break;
            case R.id.tvFail:
                fragmentPosition = 3;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 3, textViews);
                break;
            case R.id.tvFinish:
                fragmentPosition = 2;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 2, textViews);
                break;
            case R.id.imgview_back:
                this.finish();
                break;
            case R.id.llTitle:
                //    showTypePop();
                break;
        }
    }


    @Override
    public void onPageSelected(int position) {
        ViewPagerUtils.changeTextViewStyle_FINANCE(this, position, textViews);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        bmpW = screenW / 4;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cursor.getLayoutParams();
        lp.width = bmpW;
        offset = (screenW / 4 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    private void initViewPager() {
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setOffscreenPageLimit(4);
        // 包含3个fragment界面
        List<TabIndicatorEntity> list = ViewPagerUtils.getTabIndicator(4);
        // 3个fragment界面封装
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new DebtAbleAssignmentFragment());
        fragmentList.add(new DebtAduitAssignmentFragment());
        fragmentList.add(new DebtTransferingFragment());
        fragmentList.add(new DebtTransferedFragment());

        // 设置ViewPager适配器
        ViewPagerFragmentAdapter viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), list, fragmentList);
        mViewPager.setAdapter(viewPagerFragmentAdapter);
        mViewPager.setCurrentItem(fragmentPosition, false);
        ViewPagerUtils.changeTextViewStyle_FINANCE(this, fragmentPosition, textViews);
    }


    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量
        int three = one * 3;// 页卡1 -> 页卡4 偏移量

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, 0, 0, 0);
                    }

                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, one, 0, 0);
                    }
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, two, 0, 0);
                    }
                    break;
                case 3:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, three, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, three, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, three, 0, 0);
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
            ViewPagerUtils.changeTextViewStyle_FINANCE(context, arg0, textViews);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }
}