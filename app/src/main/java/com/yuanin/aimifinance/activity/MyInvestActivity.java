package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.PopDownRecycleViewAdapter;
import com.yuanin.aimifinance.adapter.ViewPagerFragmentAdapter;
import com.yuanin.aimifinance.base.BaseFragmentActivity;
import com.yuanin.aimifinance.entity.TabIndicatorEntity;
import com.yuanin.aimifinance.fragment.MyInvestCollectFragment;
import com.yuanin.aimifinance.fragment.MyInvestDoingFragment;
import com.yuanin.aimifinance.fragment.MyInvestFailFragment;
import com.yuanin.aimifinance.fragment.MyInvestFinishFragment;
import com.yuanin.aimifinance.utils.StaticMembers;
import com.yuanin.aimifinance.utils.ViewPagerUtils;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MyInvestActivity extends BaseFragmentActivity {
    @ViewInject(R.id.ivArrow)
    private ImageView ivArrow;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.rlTitle)
    private RelativeLayout rlTitle;
    @ViewInject(R.id.llParent)
    private LinearLayout llParent;
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

    public static int type = 0;
    private final String[] titles = {"爱米定期", "爱米优选"};
    private View popTitleView;
    private PopupWindow titlePop;
    private PopDownRecycleViewAdapter mTitleAdp;
    private List<String> mTitlePopList;
    private RecyclerView lvTitle;
    private List<Fragment> fragmentList;
    private List<TextView> textViews;
    private int fragmentPosition = 0;
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private Animation animation;
    private Context context = MyInvestActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invest);
        popTitleView = getLayoutInflater().inflate(R.layout.popupwindow_down_list_select, null);
        lvTitle = (RecyclerView) popTitleView.findViewById(R.id.lvDownList);
        lvTitle.setLayoutManager(new GridLayoutManager(this, 2));
        x.view().inject(this);
        if (textViews == null) {
            textViews = new ArrayList<TextView>();
        }
        textViews.add(tvDoing);
        textViews.add(tvCollecting);
        textViews.add(tvFail);
        textViews.add(tvFinish);
        InitImageView();
        initViewPager();
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            tv_title.setText(titles[0]);
        } else {
            tv_title.setText(titles[1]);
        }
        initTitlePopData();
    }

    @Event(value = {R.id.btnRefresh, R.id.imgview_back, R.id.tvDoing, R.id.tvCollecting, R.id.tvFail, R.id.tvFinish, R.id.llTitle})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tvDoing:
                fragmentPosition = 0;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 0, textViews);
                break;
            case R.id.tvCollecting:
                fragmentPosition = 1;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 1, textViews);
                break;
            case R.id.tvFail:
                fragmentPosition = 2;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 2, textViews);
                break;
            case R.id.tvFinish:
                fragmentPosition = 3;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 3, textViews);
                break;
            case R.id.imgview_back:
                this.finish();
                break;
            case R.id.llTitle:
                showTypePop();
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
        fragmentList.add(new MyInvestDoingFragment());
        fragmentList.add(new MyInvestCollectFragment());
        fragmentList.add(new MyInvestFailFragment());
        fragmentList.add(new MyInvestFinishFragment());
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
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private void initTitlePopData() {
        if (mTitlePopList == null) {
            mTitlePopList = new ArrayList<String>();
            mTitlePopList.add(titles[0]);
            mTitlePopList.add(titles[1]);
            mTitleAdp = new PopDownRecycleViewAdapter(mTitlePopList, this);
            mTitleAdp.setCurrentSelect(type);
            lvTitle.setAdapter(mTitleAdp);
        }
        titlePop = new PopupWindow(popTitleView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        titlePop.setAnimationStyle(R.style.PopupWindowCenterAnimation);
        titlePop.setBackgroundDrawable(new BitmapDrawable());
        titlePop.setFocusable(true);
        titlePop.setOutsideTouchable(true);
        mTitleAdp.setOnItemClickListener(new PopDownRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (type != position) {
                    type = position;
                    mTitleAdp.setCurrentSelect(type);
                    mTitleAdp.notifyDataSetChanged();
                    tv_title.setText(titles[position]);
                    switch (fragmentPosition) {
                        case 0:
                            ((MyInvestDoingFragment) fragmentList.get(0)).refreshData();
                            ((MyInvestCollectFragment) fragmentList.get(1)).resetData();
                            ((MyInvestFailFragment) fragmentList.get(2)).resetData();
                            ((MyInvestFinishFragment) fragmentList.get(3)).resetData();
                            break;
                        case 1:
                            ((MyInvestDoingFragment) fragmentList.get(0)).resetData();
                            ((MyInvestCollectFragment) fragmentList.get(1)).refreshData();
                            ((MyInvestFailFragment) fragmentList.get(2)).resetData();
                            ((MyInvestFinishFragment) fragmentList.get(3)).resetData();
                            break;
                        case 2:
                            ((MyInvestDoingFragment) fragmentList.get(0)).resetData();
                            ((MyInvestCollectFragment) fragmentList.get(1)).resetData();
                            ((MyInvestFailFragment) fragmentList.get(2)).refreshData();
                            ((MyInvestFinishFragment) fragmentList.get(3)).resetData();
                            break;
                        case 3:
                            ((MyInvestDoingFragment) fragmentList.get(0)).resetData();
                            ((MyInvestCollectFragment) fragmentList.get(1)).resetData();
                            ((MyInvestFailFragment) fragmentList.get(2)).resetData();
                            ((MyInvestFinishFragment) fragmentList.get(3)).refreshData();
                            break;
                    }
                }
                titlePop.dismiss();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        titlePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Animation animation = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(500);
                animation.setFillAfter(true);
                ivArrow.startAnimation(animation);
            }
        });
    }

    private void showTypePop() {
        if (titlePop.isShowing()) {
            titlePop.dismiss();
        } else {
            if (animation == null) {
                animation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(500);
                animation.setFillAfter(true);
            }
            ivArrow.startAnimation(animation);
            if (Build.VERSION.SDK_INT >= 24) {
                Rect visibleFrame  = new Rect();
                rlTitle.getGlobalVisibleRect(visibleFrame);
                int height = rlTitle.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                titlePop.setHeight(height);
                titlePop.showAsDropDown(rlTitle);
                /*int top = rlTitle.getHeight() + StaticMembers.STATUS_HEIGHT;
                titlePop.showAtLocation(llParent, Gravity.NO_GRAVITY, 0, top);*/
            } else {
                titlePop.showAsDropDown(rlTitle);
            }
        }
    }
}
