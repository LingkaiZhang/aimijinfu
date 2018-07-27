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

public class FinanceProductDetailActivity extends BaseFragmentActivity implements ISlideCallback {
    @ViewInject(R.id.llMain)
    private View llMain;
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvProductDetail)
    private TextView tvProductDetail;
    @ViewInject(R.id.tvRecord)
    private TextView tvRecord;
    @ViewInject(R.id.tvQuestion)
    private TextView tvQuestion;
    @ViewInject(R.id.tvPlan)
    private TextView tvPlan;
    //    @ViewInject(R.id.tvJoinMoney)
//    private TextView tvJoinMoney;
    @ViewInject(R.id.tvTitle)
    private TextView tvTitle;
    @ViewInject(R.id.tvTotalMoney)
    private TextView tvTotalMoney;
    @ViewInject(R.id.tvRate)
    private TextView tvRate;
    @ViewInject(R.id.tvTime)
    private TextView tvTime;
    @ViewInject(R.id.tvUnit)
    private TextView tvUnit;
    @ViewInject(R.id.tvLeaveMoney)
    private TextView tvLeaveMoney;
    @ViewInject(R.id.tvBuy)
    private TextView tvBuy;
    @ViewInject(R.id.vpProductDetail)
    private ViewPager mViewPager;
    @ViewInject(R.id.cursor)
    private ImageView cursor;
    //    @ViewInject(R.id.pbJoin)
//    private ProgressBar pbJoin;
    @ViewInject(R.id.imgview_right)
    private TextView imgview_right;
    @ViewInject(R.id.svMain)
    private FrameLayout svMain;
    @ViewInject(R.id.tvNoContent)
    private TextView tvNoContent;
    @ViewInject(R.id.llNoNet)
    private LinearLayout llNoNet;
    @ViewInject(R.id.viewRemind)
    private View viewRemind;
    @ViewInject(R.id.viewLoading)
    private View viewLoading;
    @ViewInject(R.id.slidedetails)
    private SlideDetailsLayout mSlideDetailsLayout;
    @ViewInject(R.id.ivBottom)
    private ImageView ivBottom;
    @ViewInject(R.id.tvBottom)
    private TextView tvBottom;
    @ViewInject(R.id.tvDate)
    private TextView tvDate;
    @ViewInject(R.id.tvSingle)
    private TextView tvSingle;
    @ViewInject(R.id.tvRepay)
    private TextView tvRepay;
    @ViewInject(R.id.imageLoading)
    private ImageView loadingImage;
    @ViewInject(R.id.tvNoNet)
    private TextView tvNoNet;
    @ViewInject(R.id.tvCompany)
    private TextView tvCompany;
    @ViewInject(R.id.tvEarn)
    private TextView tvEarn;

    @ViewInject(R.id.tvStartDate)
    private TextView tvStartDate;
    @ViewInject(R.id.tvManDate)
    private TextView tvManDate;

    @ViewInject(R.id.tvQixiDate)
    private TextView tvQixiDate;
    @ViewInject(R.id.tvEndDate)
    private TextView tvEndDate;
    @ViewInject(R.id.ivProgress)
    private ImageView ivProgress;
    @ViewInject(R.id.llLeaveTime)
    private LinearLayout llLeaveTime;

    @ViewInject(R.id.interestRates)
    private TextView interestRates;
    @ViewInject(R.id.cbChoose)
    private CheckBox cbChoose;
    @ViewInject(R.id.tvInstruction)
    private TextView tvInstruction;
    @ViewInject(R.id.ratingStart)
    private RatingBar ratingStart;
    @ViewInject(R.id.tvRecruitmentPeriod)
    private TextView tvRecruitmentPeriod;
    @ViewInject(R.id.tv_product_name)
    private TextView tvProductName;
    @ViewInject(R.id.tv_product_type)
    private TextView tvProductType;


    private List<TextView> textViews;
    public static int fragmentPosition = 0;
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private String entityID;
    private ProductIntroduceFragment productIntroduceFragment;
    private InvestRecordFragment investRecordFragment;
    private AssetsFragment assetsFragment;
    private RepayPlanFragment repayPlanFragment;
    private ProductDetailEntity productDetailEntity;
    private Context context = FinanceProductDetailActivity.this;
    private View popuSafetyLevel;
    private PopupWindow slPop;
    private View popView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_product_detail);
        popView = getLayoutInflater().inflate(R.layout.popupwindow_hk_register_new, null, false);
        x.view().inject(this);
        if (textViews == null) {
            textViews = new ArrayList<TextView>();
        }
        entityID = getIntent().getStringExtra("entityID");
        initTopBarWithRightText("项目详情", toptitleView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(FinanceProductDetailActivity.this, HistoryItemActivity.class);
                intent2.putExtra("entityID", entityID);
                startActivity(intent2);
            }
        }, "");
        textViews.add(tvProductDetail);
        textViews.add(tvQuestion);
        textViews.add(tvRecord);
        textViews.add(tvPlan);
        InitImageView();
        initViewPager();
        requestData();
        if (StaticMembers.isShowLastItem) {
            imgview_right.setVisibility(View.VISIBLE);
        } else {
            imgview_right.setVisibility(View.GONE);
        }
        mSlideDetailsLayout.setOnSlideDetailsListener(new SlideDetailsLayout.OnSlideDetailsListener() {
            @Override
            public void onStatucChanged(SlideDetailsLayout.Status status) {
                if (status.equals(SlideDetailsLayout.Status.OPEN)) {
                    tvBottom.setText("下拉拖动返回");
                    ivBottom.setImageResource(R.mipmap.product_detail_down_arrow);
                } else if (status.equals(SlideDetailsLayout.Status.CLOSE)) {
                    tvBottom.setText("上划加载更多详情");
                    ivBottom.setImageResource(R.mipmap.product_detail_up_arrow);
                }
            }
        });
    }


    // 点击事件
    @Event(value = {R.id.tvProductDetail,
            R.id.tvRecord, R.id.tvQuestion, R.id.tvPlan, R.id.tvBuy, R.id.btnRefresh, R.id.tvModel_loan_contract,
            R.id.tvInstruction, R.id.iv_safety_level, R.id.btnCheckNetwork })
    private void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_safety_level:
                showPopupWindow();
                break;
            case R.id.tvInstruction:
                Intent intent2 = new Intent(this, WebViewHtmlActivity.class);
                startActivity(intent2);
                break;
            case R.id.tvModel_loan_contract:
                Intent intent1 = new Intent(this, WebViewActivity.class);
                intent1.putExtra(ParamsKeys.TYPE,ParamsValues.MODEL_LOAN_CONTRACT);
                startActivity(intent1);
                break;
            case R.id.tvProductDetail:
                fragmentPosition = 0;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 0, textViews);
                break;
            case R.id.tvQuestion:
                fragmentPosition = 1;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 1, textViews);
                break;
            case R.id.tvRecord:
                fragmentPosition = 2;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 2, textViews);
                break;
            case R.id.tvPlan:
                fragmentPosition = 3;
                mViewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.changeTextViewStyle_FINANCE(this, 3, textViews);
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
                    Intent intent = new Intent(this, BuyRegularActivity.class);
                    intent.putExtra("entityID", entityID);
                    startActivity(intent);
                }
                break;
            case R.id.btnRefresh:
                requestData();
                break;
            case R.id.btnCheckNetwork:
                AppUtils.checkNetwork(context);
                break;
            default:
                break;
        }
    }

    private void showPopupWindow() {
        popuSafetyLevel = LayoutInflater.from(this).inflate(R.layout.popupwindow_safety_level, null);
        slPop = AppUtils.createSLPop(popuSafetyLevel, context);
        slPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
    }

    private void requestData() {
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_PRODUCT);
            obj.put(ParamsKeys.MOTHED, ParamsValues.GET_PRODUCT_DETAIL);
            obj.put(ParamsKeys.PRODUCT_ID, entityID);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<ProductDetailEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, new IHttpRequestCallBack() {
            @Override
            public void onStart() {
                ((Animatable) loadingImage.getDrawable()).start();
                viewRemind.setVisibility(View.GONE);
                viewLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                if (AppUtils.isNetworkConnected(context)) {
                    tvNoNet.setText(context.getResources().getString(R.string.connect_fail));
                } else {
                    tvNoNet.setText(context.getResources().getString(R.string.network_fail));
                }
                ((Animatable) loadingImage.getDrawable()).stop();
                viewRemind.setVisibility(View.VISIBLE);
                viewLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(Object object) {
                ReturnResultEntity<ProductDetailEntity> entity = (ReturnResultEntity<ProductDetailEntity>) object;
                if (entity.isSuccess(context)) {
                    if (entity.isNotNull()) {
                        productDetailEntity = entity.getData().get(0);
                        setUpView(productDetailEntity);
                        productIntroduceFragment.setDatas(productDetailEntity);
                        investRecordFragment.setDatas(productDetailEntity);
                        repayPlanFragment.setDatas(productDetailEntity);
                        assetsFragment.setDatas(productDetailEntity);
                        svMain.setVisibility(View.VISIBLE);
                        tvNoContent.setVisibility(View.GONE);
                        llNoNet.setVisibility(View.GONE);
                    } else {
                        svMain.setVisibility(View.GONE);
                        tvNoContent.setVisibility(View.VISIBLE);
                        llNoNet.setVisibility(View.GONE);
                    }
                } else {
                    svMain.setVisibility(View.GONE);
                    tvNoContent.setVisibility(View.GONE);
                    llNoNet.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure() {
                svMain.setVisibility(View.GONE);
                tvNoContent.setVisibility(View.GONE);
                llNoNet.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setUpView(ProductDetailEntity productDetailEntity) {
        tvStartDate.setText(productDetailEntity.getBuystarttime());
        tvManDate.setText(productDetailEntity.getManbiao_time());
        tvQixiDate.setText(productDetailEntity.getInterestdate());
        tvEndDate.setText(productDetailEntity.getExpire_time());

        switch (productDetailEntity.getStep()) {
            case 0:
                //此方法更节省内存 防止OOM
                ivProgress.setBackgroundDrawable(new BitmapDrawable(getResources(),
                        AppUtils.getBitmap(this, R.mipmap.product_detail_item0)));
                break;
            case 1:
                //此方法更节省内存 防止OOM
                ivProgress.setBackgroundDrawable(new BitmapDrawable(getResources(),
                        AppUtils.getBitmap(this, R.mipmap.product_detail_item1)));
                break;
            case 2:
                //此方法更节省内存 防止OOM
                ivProgress.setBackgroundDrawable(new BitmapDrawable(getResources(),
                        AppUtils.getBitmap(this, R.mipmap.product_detail_item2)));
                break;
            case 3:
                //此方法更节省内存 防止OOM
                ivProgress.setBackgroundDrawable(new BitmapDrawable(getResources(),
                        AppUtils.getBitmap(this, R.mipmap.product_detail_item3)));
                break;
            case 4:
                //此方法更节省内存 防止OOM
                ivProgress.setBackgroundDrawable(new BitmapDrawable(getResources(),
                        AppUtils.getBitmap(this, R.mipmap.product_detail_item4)));
                break;
        }

        int repay_method = 0;
        if (productDetailEntity.getRepay_method().equals("先息后本")) {
            repay_method = 1;
        } else if (productDetailEntity.getRepay_method().equals("等额本息")) {
            repay_method = 2;
        } else if (productDetailEntity.getRepay_method().equals("到期还本付息")) {
            repay_method = 3;
        }
        double earn = AppUtils.calcInterest(10000.00, Double.parseDouble(productDetailEntity.getAnnual()), Integer.parseInt(productDetailEntity.getTerm()), productDetailEntity.getUnit(), repay_method);
        tvEarn.setText(earn + "");
       // tvTitle.setText(productDetailEntity.getProject_name());
        tvTitle.setText("");
        tvTotalMoney.setText(FmtMicrometer.format6(productDetailEntity.getAmount()));
 //       tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(productDetailEntity.getAnnual())));
        tvTime.setText(productDetailEntity.getTerm());
        tvUnit.setText("期限(" + productDetailEntity.getUnit() + ")");
        tvLeaveMoney.setText(FmtMicrometer.format6(productDetailEntity.getBuyamount()));
        if (productDetailEntity.getIsbuy() == 1) {
            tvBuy.setBackgroundResource(R.drawable.selector_theme_button);
            tvBuy.setEnabled(true);
            if (Long.parseLong(productDetailEntity.getBuylasttime()) > 0) {
                ProductTimerCount productTimerCount = new ProductTimerCount(Long.parseLong(productDetailEntity.getBuylasttime()) * 1000, 1000, tvDate, new INotifyCallBack() {
                    @Override
                    public void onNotify() {
                        requestData();
                    }
                });
                productTimerCount.start();
            } else {
                llLeaveTime.setVisibility(View.GONE);
            }
        } else {
            tvBuy.setBackgroundResource(R.color.gray_cancel_click);
            tvBuy.setEnabled(false);
            llLeaveTime.setVisibility(View.GONE);
        }
        tvBuy.setText(productDetailEntity.getStatusname());
        tvSingle.setText(productDetailEntity.getEachamount());
        tvRepay.setText(productDetailEntity.getRepay_method());
        tvCompany.setText(productDetailEntity.getGuaranteecompany());
        tvProductName.setText(productDetailEntity.getProject_name().trim());
        tvProductType.setText(productDetailEntity.getDebtstype());

        if (productDetailEntity.getOrgannual() == null || productDetailEntity.getExtannual() == null) {
            tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(productDetailEntity.getAnnual())));
            interestRates.setVisibility(View.GONE);
        } else {
            if (Double.valueOf(productDetailEntity.getExtannual()) > 0 && Double.valueOf(productDetailEntity.getOrgannual()) > 0 ) {
                interestRates.setText("+" + FmtMicrometer.format6(productDetailEntity.getExtannual()) + "%");
                tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(productDetailEntity.getOrgannual())));
                interestRates.setVisibility(View.VISIBLE);
            } else {
                tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(productDetailEntity.getAnnual())));
                interestRates.setVisibility(View.GONE);
            }
        }
        if (productDetailEntity.getSafegrade() != null) {
            ratingStart.setRating(Float.parseFloat(productDetailEntity.getSafegrade()));
        } else {
            ratingStart.setRating((float) 4.5);
        }

        if (productDetailEntity.getRecruitmentperiod() != null) {
            tvRecruitmentPeriod.setText(productDetailEntity.getRecruitmentperiod() + "天");
        } else {
            tvRecruitmentPeriod.setText("6天");
        }

//        tvSafe.setText(productDetailEntity.getGuarantee_method());

//        if (productDetailEntity.getPercentage() > 5) {
//            ValueAnimator animator = ValueAnimator.ofInt(0, productDetailEntity.getPercentage());
//            animator.setDuration(2000);
//            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    int num = (Integer) animation.getAnimatedValue();
//                    pbJoin.setProgress(num);
//                    tvJoinMoney.setText(num + "%");
//                }
//            });
//            animator.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            });
//            animator.setInterpolator(new LinearInterpolator());
//            animator.start();
//        } else {
//            pbJoin.setProgress(productDetailEntity.getPercentage());
//            tvJoinMoney.setText(productDetailEntity.getPercentage() + "%");
//        }
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
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        productIntroduceFragment = new ProductIntroduceFragment();
        fragmentList.add(productIntroduceFragment);
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
            if (slPop != null && slPop.isShowing()) {
                slPop.dismiss();
                return true;
            }
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
        int three = one * 3;// 页卡1 -> 页卡3 偏移量

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
