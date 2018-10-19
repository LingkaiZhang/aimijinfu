package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.drawable.Animatable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
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
import com.yuanin.aimifinance.dialog.DebtConfirmPayDialog;
import com.yuanin.aimifinance.entity.BuyDebtSuccessEntity;


import com.yuanin.aimifinance.entity.DebtProductDetailEntity;
import com.yuanin.aimifinance.entity.EventMessage;


import com.yuanin.aimifinance.entity.ProductDetailEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.entity.TabIndicatorEntity;


import com.yuanin.aimifinance.fragment.DebtAssetsFragment;
import com.yuanin.aimifinance.fragment.DebtInvestRecordFragment;
import com.yuanin.aimifinance.fragment.DebtProductIntroduceFragment;
import com.yuanin.aimifinance.fragment.DebtRepayPlanFragment;


import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.inter.INotifyCallBack;
import com.yuanin.aimifinance.inter.ISlideCallback;
import com.yuanin.aimifinance.service.ProductTimerCount;
import com.yuanin.aimifinance.utils.AppUtils;


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

import de.greenrobot.event.EventBus;

public class DebtProductDetailActivity extends BaseFragmentActivity implements ISlideCallback {
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

    @ViewInject(R.id.tvDiscountCoefficient)
    private TextView tvDiscountCoefficient;
    @ViewInject(R.id.tvLeaveDay)
    private TextView tvLeaveDay;
    @ViewInject(R.id.tv_creditor_value)
    private TextView tv_creditor_value;
    @ViewInject(R.id.tv_transfer_capital)
    private TextView tv_transfer_capital;
    @ViewInject(R.id.tv_pay_capital)
    private TextView tv_pay_capital;
    @ViewInject(R.id.tv_recept_service_charge)
    private TextView tv_recept_service_charge;


    private String entityID;
    private List<TextView> textViews;
    public static int fragmentPosition = 0;
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private DebtProductIntroduceFragment debtProductIntroduceFragment;
    private DebtInvestRecordFragment debtInvestRecordFragment;
    private DebtAssetsFragment debtAssetsFragment;
    private DebtRepayPlanFragment debtRepayPlanFragment;
    private Context context = DebtProductDetailActivity.this;
    private DebtProductDetailEntity debtProductDetailEntity;
    private View popView;
    private DebtConfirmPayDialog debtConfirmPayDialog;
    private String productName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_product_detail);
        popView = getLayoutInflater().inflate(R.layout.popupwindow_hk_register_new, null, false);
        x.view().inject(this);
        if (textViews == null) {
            textViews = new ArrayList<TextView>();
        }

        entityID = getIntent().getStringExtra("entityID");
        productName = getIntent().getStringExtra("productName");

        initTopBarWithRightText(productName, toptitleView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DebtProductDetailActivity.this, HistoryItemActivity.class);
                intent2.putExtra("entityID", entityID);
                startActivity(intent2);
            }
        }, "");
        textViews.add(tvProductDetail);
        textViews.add(tvQuestion);
        //textViews.add(tvRecord);
        textViews.add(tvPlan);
        InitImageView();
        initViewPager();
        requestData();
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

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    private void requestData() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_DEBT);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_GET_BUY_ENTRANSFER_Detail);
            obj.put(ParamsKeys.BORROW_TRANSFER_ID, entityID);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<DebtProductDetailEntity>>() {
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
                ReturnResultEntity<DebtProductDetailEntity> entity = (ReturnResultEntity<DebtProductDetailEntity>) object;
                if (entity.isSuccess(context)) {
                    if (entity.isNotNull()) {
                        debtProductDetailEntity = entity.getData().get(0);
                        setUpView(debtProductDetailEntity);
                        debtProductIntroduceFragment.setDatas(debtProductDetailEntity);
                       // debtInvestRecordFragment.setDatas(debtProductDetailEntity);
                        debtRepayPlanFragment.setDatas(debtProductDetailEntity);
                        debtAssetsFragment.setDatas(debtProductDetailEntity);
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

    private void setUpView(DebtProductDetailEntity productDetailEntity) {
        tvRate.setText(productDetailEntity.getAnnual());
        tvDiscountCoefficient.setText(productDetailEntity.getDiscountRate());
        tvLeaveMoney.setText(productDetailEntity.getDueCapital());
        tvLeaveDay.setText(productDetailEntity.getDays());
        tv_creditor_value.setText(productDetailEntity.getDueAmount() + "元");
        tvProductType.setText(productDetailEntity.getDiscount() + "元");
        tv_transfer_capital.setText(productDetailEntity.getDueCapital() + "元");
        tv_pay_capital.setText(productDetailEntity.getPayAmount() + "元");
        tvRepay.setText(productDetailEntity.getRepayMethod());
        tv_recept_service_charge.setText(productDetailEntity.getBuyFee() + "元");


        //倒计时功能
        ProductTimerCount productTimerCount = new ProductTimerCount(Long.parseLong(productDetailEntity.getSurplusTime()) * 1000, 1000, tvDate, new INotifyCallBack() {
            @Override
            public void onNotify() {
                requestData();
            }
        });
        productTimerCount.start();

    }

    private void initViewPager() {
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setOffscreenPageLimit(3);
        // 包含3个fragment界面
        List<TabIndicatorEntity> list = ViewPagerUtils.getTabIndicator(3);
        // 3个fragment界面封装
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        debtProductIntroduceFragment = new DebtProductIntroduceFragment();
        fragmentList.add(debtProductIntroduceFragment);
        debtAssetsFragment = new DebtAssetsFragment();
        fragmentList.add(debtAssetsFragment);
        //debtInvestRecordFragment = new DebtInvestRecordFragment();
        //fragmentList.add(debtInvestRecordFragment);
        debtRepayPlanFragment = new DebtRepayPlanFragment();
        fragmentList.add(debtRepayPlanFragment);
        // 设置ViewPager适配器
        ViewPagerFragmentAdapter viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), list, fragmentList);
        mViewPager.setAdapter(viewPagerFragmentAdapter);
        mViewPager.setCurrentItem(fragmentPosition, false);
        ViewPagerUtils.changeTextViewStyle_FINANCE(this, fragmentPosition, textViews);
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
        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragmentPosition = 0;
        DebtProductIntroduceFragment.isTop = true;
    }

    // 点击事件
    @Event(value = {R.id.tvProductDetail,
            R.id.tvRecord, R.id.tvQuestion, R.id.tvPlan, R.id.tvBuy, R.id.btnRefresh, R.id.tvModel_loan_contract,
            R.id.tvInstruction, R.id.btnCheckNetwork, R.id.tv_project_detail, R.id.tvAssignmentAgreement })
    private void onViewClicked(View view) {
        switch (view.getId()) {
            case  R.id.tvAssignmentAgreement:
                Intent intent5 = new Intent(this, WebViewActivity.class);
                intent5.putExtra(ParamsKeys.TYPE,ParamsValues.DEBT_ASSIGNMENT_MODE);
                startActivity(intent5);
                 break;
            case R.id.tv_project_detail:
                Intent intent = new Intent(this, FinanceProductDetailActivity.class);
                intent.putExtra("entityID",debtProductDetailEntity.getBorrowAmountId());
                startActivity(intent);
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
                } else {
                    debtConfirmPayDialog = new DebtConfirmPayDialog(this, false, "确认支付",
                            debtProductDetailEntity.getBalance(), debtProductDetailEntity.getDueCapital(),debtProductDetailEntity.getBuyFee(),
                            debtProductDetailEntity.getPayAmount(),debtProductDetailEntity.getWillInterest(), "取消", "确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            debtConfirmPayDialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Double.valueOf(debtProductDetailEntity.getBalance()) < Double.valueOf(debtProductDetailEntity.getPayAmount())) {
                                AppUtils.showMiddleToast(DebtProductDetailActivity.this,getResources().getString(R.string.buy_regular_balance_not));
                                startActivity(new Intent(DebtProductDetailActivity.this, PayInputMoneyActivity.class));
                            } else {
                                //购买债转标的
                                requestDebtProductBuy();
                            }
                            debtConfirmPayDialog.dismiss();
                        }
                    });
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

    private void requestDebtProductBuy() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_DEBT);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_BUY_TRANSFER_BORROW);
            obj.put(ParamsKeys.BORROW_TRANSFER_ID, debtProductDetailEntity.getBorrowTransferId());
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<BuyDebtSuccessEntity>>() {
        }.getType();
        NetUtils.request(context, obj, mType, new IHttpRequestCallBack() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onSuccess(Object object) {
                        ReturnResultEntity<BuyDebtSuccessEntity> entity = (ReturnResultEntity<BuyDebtSuccessEntity>) object;
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                BuyDebtSuccessEntity buySuccessEntity = entity.getData().get(0);
                                //购买成功
                                Intent intent = new Intent(context, BuySuccessNewActivity.class);
                                intent.putExtra("transferOrderId", buySuccessEntity.getTransferOrderId());
                                startActivity(intent);
                                this.onFinish();
                            }

                            //刷新个人中心数据
                            EventMessage eventMessage = new EventMessage();
                            eventMessage.setType(EventMessage.REFRESH_MINE);
                            EventBus.getDefault().post(eventMessage);
                            //刷新商品界面数据
                            EventMessage eventMessage2 = new EventMessage();
                            eventMessage2.setType(EventMessage.REFRESH_FINANCE_PRODUCT);
                            EventBus.getDefault().post(eventMessage2);
                            DebtProductDetailActivity.this.finish();
                        } else {
                            AppUtils.showToast(context, entity.getRemark());
                        }
                    }

                    @Override
                    public void onFailure() {
                        AppUtils.showConectFail(context);
                    }
                }
        );
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
