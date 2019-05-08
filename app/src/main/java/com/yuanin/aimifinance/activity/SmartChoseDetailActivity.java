package com.yuanin.aimifinance.activity;

import android.content.Context;
        import android.content.Intent;
        import android.graphics.Matrix;
        import android.graphics.drawable.Animatable;
        import android.graphics.drawable.BitmapDrawable;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.view.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
        import android.view.Gravity;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
        import android.widget.CheckBox;
import android.widget.EditText;
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
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.entity.BuyProductEntity;
import com.yuanin.aimifinance.entity.BuySuccessEntity;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.entity.ProductDetailEntity;
import com.yuanin.aimifinance.entity.RedPacketsEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.entity.SmartInvestDetailsEntity;
import com.yuanin.aimifinance.entity.TabIndicatorEntity;
        import com.yuanin.aimifinance.fragment.AssetsFragment;
        import com.yuanin.aimifinance.fragment.InvestRecordFragment;
        import com.yuanin.aimifinance.fragment.ProductIntroduceFragment;
        import com.yuanin.aimifinance.fragment.RepayPlanFragment;
import com.yuanin.aimifinance.fragment.SmartChoseExplainFragment;
import com.yuanin.aimifinance.fragment.SmartInvestProductListFragment;
import com.yuanin.aimifinance.fragment.SmartInvestRecordFragment;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
        import com.yuanin.aimifinance.inter.INotifyCallBack;
        import com.yuanin.aimifinance.inter.ISlideCallback;
        import com.yuanin.aimifinance.service.ProductTimerCount;
        import com.yuanin.aimifinance.utils.AppUtils;
        import com.yuanin.aimifinance.utils.FmtMicrometer;
        import com.yuanin.aimifinance.utils.NetUtils;
        import com.yuanin.aimifinance.utils.ParamsKeys;
        import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.RSAUtils;
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
    @ViewInject(R.id.etShare)
    private EditText etShare;
    @ViewInject(R.id.ivRight)
    private ImageView ivRight;
    @ViewInject(R.id.tvRedPackets)
    private TextView tvRedPackets;
    @ViewInject(R.id.tvRedPacketsEarnMoney)
    private TextView tvRedPacketsEarnMoney;
    @ViewInject(R.id.cbChoose)
    private CheckBox cbChoose;

    @ViewInject(R.id.tvSurplusAmount)
    private TextView tvSurplusAmount;
    @ViewInject(R.id.tvBalance)
    private TextView tvBalance;




    private RedPacketsEntity redPacketsEntity;
    private boolean isAnimationEnd = true;
    private List<RedPacketsEntity> redPacketsList;
    private BuyProductEntity buyProductEntity;
    private boolean isAgree = true;

    private List<TextView> textViews;
    public static int fragmentPosition = 0;
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private SmartInvestRecordFragment investRecordFragment;
    private SmartChoseExplainFragment assetsFragment;
    private SmartInvestProductListFragment repayPlanFragment;
    private Context context = SmartChoseDetailActivity.this;
    private SmartInvestDetailsEntity smartInvestDetails;
    private View popView;
    private GeneralDialog generalDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_chose_detail);
        popView = getLayoutInflater().inflate(R.layout.popupwindow_hk_register_new, null, false);
        x.view().inject(this);
        if (textViews == null) {
            textViews = new ArrayList<>();
        }
        initTopBarWithRightText("项目详情", toptitleView, v -> {
        }, "");
        textViews.add(tvQuestion);
        textViews.add(tvRecord);
        textViews.add(tvPlan);
        initView();
        initListener();
        InitImageView();
        initViewPager();
        requestData();
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

    private void requestData() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_SMART_INVEST);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_SMART_INVEST_BUY_INFO);
            obj.put(ParamsKeys.UID, StaticMembers.USER_ID);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<SmartInvestDetailsEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, new IHttpRequestCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Object object) {
                ReturnResultEntity<SmartInvestDetailsEntity> smartInvestDetailsEntity = (ReturnResultEntity<SmartInvestDetailsEntity>) object;
                if (smartInvestDetailsEntity.isSuccess(context)) {
                    if (smartInvestDetailsEntity.isNotNull()) {
                        smartInvestDetails = smartInvestDetailsEntity.getData().get(0);
                        tvBalance.setText(smartInvestDetailsEntity.getData().get(0).getBalance());
                        tvSurplusAmount.setText(smartInvestDetailsEntity.getData().get(0).getSurplusAmount());
                    }
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void initView() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        requestData();
    }

    private void initListener() {
        etShare.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.toString().substring(0, 1).equals("0")) {
                        etShare.setText("");
                    } else {
                       /* double fee = (Double.valueOf(s.toString()));
                        double earnMoney = AppUtils.multiply(fee, buyProductEntity.getInterest());
                        double trueEarnMoney = AppUtils.divide(earnMoney, 100);
                        if (buyProductEntity.getIs_new() != 1 && redPacketsList != null && redPacketsList.size() > 0) {
                            chooseRedpacket(fee);
                        }*/
                    }
                } else {

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbChoose.setOnCheckedChangeListener((buttonView, isChecked) -> isAgree = isChecked);
    }

    private void chooseRedpacket(double fee) {
        redPacketsEntity = null;
        for (int i = 0; i < redPacketsList.size(); i++) {
            if (fee >= redPacketsList.get(i).getMin_invest_amount()) {
                redPacketsEntity = redPacketsList.get(i);
                break;
            }
        }
        if (redPacketsEntity == null) {
            if (isAnimationEnd) {
                tvRedPacketsEarnMoney.setVisibility(View.GONE);
                ivRight.setImageResource(R.mipmap.buy_right_arrow);
                tvRedPackets.setText("");
            }
        } else {
            setRedPacketData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                if (data != null) {
                    redPacketsEntity = (RedPacketsEntity) data.getExtras().getSerializable("entity");
                    setRedPacketData();
                }
                break;
        }
    }

    private void setRedPacketData() {
        tvRedPackets.setText(redPacketsEntity.getName());
        ivRight.setImageResource(R.mipmap.redpacket_close);
        tvRedPacketsEarnMoney.setVisibility(View.VISIBLE);
        tvRedPacketsEarnMoney.setText("+" + redPacketsEntity.getAmount());
    }

    // 点击事件
    @Event(value = {
            R.id.tvRecord, R.id.tvQuestion, R.id.tvPlan, R.id.tvBuy, R.id.btnRefresh,
            R.id.btnCheckNetwork, R.id.rlRedPackets, R.id.ivRight,
            R.id.tvSmartChoseProtocol, R.id.tvLoanContract, R.id.tvRiskStatement, R.id.tvAssignmentAgreement})
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

            //选择红包
            case R.id.rlRedPackets:
                String money2 = "";
                if (etShare.getText().toString().trim().length() == 0 || Integer.parseInt(etShare.getText().toString().trim()) < 1) {
                    money2 = "0";
                } else {
                    money2 = etShare.getText().toString().trim();
                }
                Intent intent4 = new Intent(this, ChooseRedpacketActivity.class);
                intent4.putExtra("money", money2);
                startActivityForResult(intent4, 1);
                break;
            //删除红包
            case R.id.ivRight:
                if (redPacketsEntity != null) {
                    if (isAnimationEnd) {
                        redPacketsEntity = null;
                        tvRedPacketsEarnMoney.setVisibility(View.GONE);
                        ivRight.setImageResource(R.mipmap.buy_right_arrow);
                        tvRedPackets.setText("");
                    }
                } else {
                    String money3 = "";
                    if (etShare.getText().toString().trim().length() == 0 || Integer.parseInt(etShare.getText().toString().trim()) < 1) {
                        money3 = "0";
                    } else {
                        money3 = etShare.getText().toString().trim();
                    }
                    Intent intent5 = new Intent(this, ChooseRedpacketActivity.class);
                    intent5.putExtra("money", money3);
                    startActivityForResult(intent5, 1);
                }
                break;
            case R.id.tvBuy:
                final String buyqty = etShare.getText().toString().trim();
                if (smartInvestDetails.getIs_activate_hkaccount() == 1) {
                    if (smartInvestDetails.getIs_bind_bankcard() == 0) {
                        AppUtils.showToast(this, "请先绑定银行卡");
                        startActivity(new Intent(context, AddBankCardActivity.class));
                        return;
                    }
                } else {
                    PopupWindow mPop = AppUtils.createHKPop(popView, context);
                    mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                    return;
                }
                if (buyqty.length() == 0 || Integer.parseInt(buyqty) < 1) {
                    AppUtils.showToast(this, getResources().getString(R.string.buy_regular_input_right_share));
                    return;
                }
                if (Integer.parseInt(buyqty) % Integer.parseInt("100") != 0) {
                    AppUtils.showToast(this, "请按" + "100" + "元的整数倍出借");
                    return;
                }
                if (Integer.parseInt(buyqty) > Double.parseDouble(smartInvestDetails.getSurplusAmount())) {
                    AppUtils.showToast(this, getResources().getString(R.string.buy_regular_input__share_too_big));
                    return;
                }
                if (redPacketsEntity != null) {
                    if (Integer.parseInt(buyqty) < redPacketsEntity.getMin_invest_amount()) {
                        AppUtils.showToast(this, getResources().getString(R.string.buy_regular_use_redpacket));
                        return;
                    }
                }
                if (!isAgree) {
                    AppUtils.showToast(this, getResources().getString(R.string.buy_regular_agree));
                    return;
                }
                if (Integer.parseInt(buyqty) > Double.parseDouble(smartInvestDetails.getBalance())) {
                    AppUtils.showToast(this, getResources().getString(R.string.buy_regular_balance_not));
                    double money = Integer.parseInt(buyqty) - Double.parseDouble(smartInvestDetails.getBalance());
                    Intent intent = new Intent(context, PayInputMoneyActivity.class);
                    intent.putExtra("money", String.valueOf(money));
                    startActivity(intent);
                } else {
                    String tips;
                    if (redPacketsEntity != null) {
                        tips = "您确定要出借" + buyqty + "元并使用" + redPacketsEntity.getAmount() + "元红包吗？";
                    } else {
                        tips = "您确定要出借" + buyqty + "元吗？";
                    }
                    generalDialog = new GeneralDialog(context, false, "确认支付",
                            tips, "取消", "确定", v -> generalDialog.dismiss(), v -> {
                        generalDialog.dismiss();

                        /*JSONObject obj = AppUtils.getPublicJsonObject(true);
                        try {
                            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_PRODUCT);
                            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_BUY_PRODUCT);
                            obj.put(ParamsKeys.BUY_QTY, buyqty);
                            if (redPacketsEntity == null) {
                                obj.put(ParamsKeys.GIFT_ID, "0");
                            } else {
                                obj.put(ParamsKeys.GIFT_ID, redPacketsEntity.getId());
                            }
                            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                            obj.put(ParamsKeys.TOKEN, token);
                            obj.remove(ParamsKeys.KEY);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Type mType = new TypeToken<ReturnResultEntity<BuySuccessEntity>>() {
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

                                    }

                                    @Override
                                    public void onFailure() {
                                        AppUtils.showConectFail(context);
                                    }
                                }
                        );*/
                    });
                }
                break;
            case R.id.btnRefresh:
                //requestData();
                break;
            case R.id.btnCheckNetwork:
                AppUtils.checkNetwork(context);
                break;
            //智投协议
            case R.id.tvSmartChoseProtocol:

                break;
            //出借合同
            case R.id.tvLoanContract:
                Intent intent1 = new Intent(this, WebViewActivity.class);
                intent1.putExtra(ParamsKeys.TYPE,ParamsValues.MODEL_LOAN_CONTRACT);
                startActivity(intent1);
                break;
            //风险提示
            case R.id.tvRiskStatement:
                Intent intent3 = new Intent(this, WebViewHtmlActivity.class);
                intent3.putExtra(ParamsKeys.TYPE, ParamsValues.LOAN_RISK_STATEMENT);
                startActivity(intent3);
                break;
            //债转协议
            case R.id.tvAssignmentAgreement:

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
        assetsFragment = new SmartChoseExplainFragment();
        fragmentList.add(assetsFragment);
        investRecordFragment = new SmartInvestRecordFragment();
        fragmentList.add(investRecordFragment);
        repayPlanFragment = new SmartInvestProductListFragment();
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

