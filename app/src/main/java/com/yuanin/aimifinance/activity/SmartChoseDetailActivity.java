package com.yuanin.aimifinance.activity;

        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Matrix;
        import android.graphics.drawable.Animatable;
        import android.graphics.drawable.BitmapDrawable;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.view.ViewPager;

        import android.util.DisplayMetrics;
        import android.view.Gravity;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.animation.Animation;
        import android.view.animation.TranslateAnimation;
        import android.widget.CheckBox;
        import android.widget.FrameLayout;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.PopupWindow;
        import android.widget.RatingBar;
        import android.widget.TextView;

        import com.google.gson.reflect.TypeToken;
        import com.yuanin.aimifinance.R;
        import com.yuanin.aimifinance.adapter.ViewPagerFragmentAdapter;
        import com.yuanin.aimifinance.base.BaseFragmentActivity;
        import com.yuanin.aimifinance.entity.ProductDetailEntity;
        import com.yuanin.aimifinance.entity.ReturnResultEntity;
        import com.yuanin.aimifinance.entity.TabIndicatorEntity;
        import com.yuanin.aimifinance.fragment.AssetsFragment;
        import com.yuanin.aimifinance.fragment.InvestRecordFragment;
        import com.yuanin.aimifinance.fragment.ProductIntroduceFragment;
        import com.yuanin.aimifinance.fragment.RepayPlanFragment;
        import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
        import com.yuanin.aimifinance.inter.INotifyCallBack;
        import com.yuanin.aimifinance.inter.ISlideCallback;
        import com.yuanin.aimifinance.service.ProductTimerCount;
        import com.yuanin.aimifinance.utils.AppUtils;
        import com.yuanin.aimifinance.utils.FmtMicrometer;
        import com.yuanin.aimifinance.utils.NetUtils;
        import com.yuanin.aimifinance.utils.ParamsKeys;
        import com.yuanin.aimifinance.utils.ParamsValues;
        import com.yuanin.aimifinance.utils.StaticMembers;
        import com.yuanin.aimifinance.utils.ViewPagerUtils;
        import com.yuanin.aimifinance.view.SlideDetailsLayout;

        import org.json.JSONException;
        import org.json.JSONObject;
        import org.xutils.view.annotation.Event;
        import org.xutils.view.annotation.ViewInject;
        import org.xutils.x;

        import java.lang.reflect.Type;
        import java.util.ArrayList;
        import java.util.List;

public class SmartChoseDetailActivity extends BaseFragmentActivity implements ISlideCallback {
    @ViewInject(R.id.llMain)
    private View llMain;
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvRecord)
    private TextView tvRecord;
    @ViewInject(R.id.tvQuestion)
    private TextView tvQuestion;
    @ViewInject(R.id.tvPlan)
    private TextView tvPlan;

    @ViewInject(R.id.vpProductDetail)
    private ViewPager mViewPager;
    @ViewInject(R.id.cursor)
    private ImageView cursor;
    @ViewInject(R.id.imgview_right)
    private TextView imgview_right;
    @ViewInject(R.id.slidedetails)
    private SlideDetailsLayout mSlideDetailsLayout;
    @ViewInject(R.id.ivBottom)
    private ImageView ivBottom;
    @ViewInject(R.id.tvBottom)
    private TextView tvBottom;



    private List<TextView> textViews;
    public static int fragmentPosition = 0;
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private InvestRecordFragment investRecordFragment;
    private AssetsFragment assetsFragment;
    private RepayPlanFragment repayPlanFragment;
    private Context context = SmartChoseDetailActivity.this;
    private View popView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_chose_detail);
        popView = getLayoutInflater().inflate(R.layout.popupwindow_hk_register_new, null, false);
        x.view().inject(this);
        if (textViews == null) {
            textViews = new ArrayList<TextView>();
        }
        initTopBarWithRightText("项目详情", toptitleView, v -> {
        }, "");
        textViews.add(tvQuestion);
        textViews.add(tvRecord);
        textViews.add(tvPlan);
        InitImageView();
        initViewPager();
        //requestData();
        if (StaticMembers.isShowLastItem) {
            imgview_right.setVisibility(View.VISIBLE);
        } else {
            imgview_right.setVisibility(View.GONE);
        }
        mSlideDetailsLayout.setOnSlideDetailsListener(status -> {
            if (status.equals(SlideDetailsLayout.Status.OPEN)) {
                tvBottom.setText("下拉拖动返回");
                ivBottom.setImageResource(R.mipmap.product_detail_down_arrow);
            } else if (status.equals(SlideDetailsLayout.Status.CLOSE)) {
                tvBottom.setText("上划加载更多详情");
                ivBottom.setImageResource(R.mipmap.product_detail_up_arrow);
            }
        });
    }


    // 点击事件
    @Event(value = {
            R.id.tvRecord, R.id.tvQuestion, R.id.tvPlan, R.id.tvBuy, R.id.btnRefresh,
            R.id.btnCheckNetwork })
    private void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvQuestion:
                fragmentPosition = 0;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 0, textViews);
                break;
            case R.id.tvRecord:
                fragmentPosition = 1;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 1, textViews);
                break;
            case R.id.tvPlan:
                fragmentPosition = 2;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 2, textViews);
                break;
//            case R.id.ivCalc:
//                if (productDetailEntity != null) {
//                    Intent intent3 = new Intent(this, CalculatorActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("productDetailEntity", productDetailEntity);
//                    intent3.putExtras(bundle);
//                    startActivity(intent3);
//                }
//                break;
            case R.id.tvBuy:
                if (StaticMembers.IS_NEED_LOGIN) {
                    startActivity(new Intent(this, LoginRegisterActivity.class));
                } else if (StaticMembers.HK_STATUS != 1) {
                    PopupWindow mPop = AppUtils.createHKPop(popView, context);
                    mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                }else {
                    //TODO 点击了立即出借按钮
                    AppUtils.showToast(this, "点击了立即出借按钮");
                }
                break;
            case R.id.btnRefresh:
                //requestData();
                break;
            case R.id.btnCheckNetwork:
                AppUtils.checkNetwork(context);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {

    }


    /**
     * 初始化动画
     */
    private void InitImageView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        bmpW = screenW / 3;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cursor.getLayoutParams();
        lp.width = bmpW;
        offset = (screenW / 3- bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    private void initViewPager() {
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setOffscreenPageLimit(3);
        // 包含3个fragment界面
        List<TabIndicatorEntity> list = ViewPagerUtils.getTabIndicator(3);
        // 3个fragment界面封装
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        assetsFragment = new AssetsFragment();
        fragmentList.add(assetsFragment);
        investRecordFragment = new InvestRecordFragment();
        fragmentList.add(investRecordFragment);
        repayPlanFragment = new RepayPlanFragment();
        fragmentList.add(repayPlanFragment);
        // 设置ViewPager适配器
        ViewPagerFragmentAdapter viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), list, fragmentList);
        mViewPager.setAdapter(viewPagerFragmentAdapter);
        mViewPager.setCurrentItem(fragmentPosition, false);
        ViewPagerUtils.changeTextViewStyle_FINANCE(this, fragmentPosition, textViews);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回键关闭pop
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragmentPosition = 0;
        ProductIntroduceFragment.isTop = true;
    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量
        //int three = one * 3;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }

                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
            fragmentPosition = arg0;
            ViewPagerUtils.changeTextViewStyle_FINANCE(context, arg0, textViews);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Override
    public void openDetails(boolean smooth) {
        mSlideDetailsLayout.smoothOpen(smooth);
    }

    @Override
    public void closeDetails(boolean smooth) {
        mSlideDetailsLayout.smoothClose(smooth);
    }
}

