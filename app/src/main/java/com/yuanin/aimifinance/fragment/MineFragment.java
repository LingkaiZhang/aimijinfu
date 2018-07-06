package com.yuanin.aimifinance.fragment;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.activity.AboutOurActivity;
import com.yuanin.aimifinance.activity.AccountBalanceActivity;
import com.yuanin.aimifinance.activity.AddBankCardActivity;
import com.yuanin.aimifinance.activity.AddUpEarningsActivity;
import com.yuanin.aimifinance.activity.AutoInvestActivity;
import com.yuanin.aimifinance.activity.CalendarViewActivity;
import com.yuanin.aimifinance.activity.CallBackWebActivity;
import com.yuanin.aimifinance.activity.FundsWaterActivity;
import com.yuanin.aimifinance.activity.GetVerifyCodeActivity;
import com.yuanin.aimifinance.activity.HomePageActivity;
import com.yuanin.aimifinance.activity.LoginActivity;
import com.yuanin.aimifinance.activity.LoginRegisterActivity;
import com.yuanin.aimifinance.activity.MessageCenterActivity;
import com.yuanin.aimifinance.activity.MyInvestActivity;
import com.yuanin.aimifinance.activity.PayInputMoneyActivity;
import com.yuanin.aimifinance.activity.PersonalSettingsActivity;
import com.yuanin.aimifinance.activity.RedPacketsActivity;
import com.yuanin.aimifinance.activity.TotalMoneyActivity;
import com.yuanin.aimifinance.activity.WebViewActivity;
import com.yuanin.aimifinance.activity.WebViewHtmlActivity;
import com.yuanin.aimifinance.activity.WithdrawActivity;
import com.yuanin.aimifinance.base.BaseFragment;
import com.yuanin.aimifinance.entity.ActivityInfoEntity;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.entity.UserAccountEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.LogUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.SharedPreferenceUtils;
import com.yuanin.aimifinance.utils.StaticMembers;
import com.yuanin.aimifinance.view.XMineScrollView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 我的
 */
public class MineFragment extends BaseFragment implements XMineScrollView.IXScrollViewListener {
    @ViewInject(R.id.refresh_root)
    private XMineScrollView mPullDownScrollView;
    @ViewInject(R.id.tvBalance)
    private TextView tvBalance;
    @ViewInject(R.id.tvEarnMoney)
    private TextView tvEarnMoney;
    @ViewInject(R.id.tvTotalMoney)
    private TextView tvTotalMoney;
    @ViewInject(R.id.tvRemind)
    private TextView tvRemind;
    @ViewInject(R.id.viewSpace)
    private View viewSpace;
    @ViewInject(R.id.rlLogin)
    private LinearLayout rlLogin;
    @ViewInject(R.id.rlNoLogin)
    private RelativeLayout rlNoLogin;
    @ViewInject(R.id.rlMine)
    private RelativeLayout rlMine;
    @ViewInject(R.id.llMain)
    private LinearLayout llMain;
    @ViewInject(R.id.pbLoading)
    private ProgressBar pbLoading;
    @ViewInject(R.id.tvRegularMoney)
    private TextView tvRegularMoney;
    @ViewInject(R.id.tvLittleItemMoney)
    private TextView tvLittleItemMoney;
    @ViewInject(R.id.tvRedTip)
    private TextView tvRedTip;

    @ViewInject(R.id.isShowBalance)
    private ImageView isShowBalance;

    @ViewInject(R.id.sdv_wonderful_activity1)
    private SimpleDraweeView sdvWonderFulActivity1;
    @ViewInject(R.id.sdv_wonderful_activity2)
    private SimpleDraweeView sdvWonderFulActivity2;
    @ViewInject(R.id.ivMessage)
    private ImageView ivMessage;
    @ViewInject(R.id.tvUserName)
    private TextView tvUserName;

    private List<UserAccountEntity> mList;
    private View mainView;
    private View popView;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean hasLoadedOnce = false;
    private boolean hadShowPop = false;
    private boolean hadShowPop2 = false;
    private View popViewQuestion;
    private List<ActivityInfoEntity> mActivityList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pull_scrollview_mine, container, false);
        mainView = inflater.inflate(R.layout.fragment_mine, container, false);
        popView = inflater.inflate(R.layout.popupwindow_hk_register_new, container, false);
        popViewQuestion = inflater.inflate(R.layout.popupwindow_question_naire, container, false);
        x.view().inject(this, view);
        x.view().inject(this, mainView);
//        //透明状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) rlMine.getLayoutParams();
//            lp1.setMargins(0, StaticMembers.STATUS_HEIGHT, 0, 0);
//            rlMine.setLayoutParams(lp1);
//        }
        //注册EventBus
        EventBus.getDefault().register(this);
        initScroll();
        //TODO 活动信息
       // requestActivityInformation();
        return view;
    }

    @Event(value = {R.id.tvLogin, R.id.tvRegister, R.id.btn_login_register})
    private void noLoginClicked(View v) {
        switch (v.getId()) {
            case R.id.tvRegister:
                Intent intent = new Intent(getActivity(), GetVerifyCodeActivity.class);
                intent.putExtra("where", 2);
                startActivity(intent);
                break;
            case R.id.tvLogin:
                startActivity(new Intent(getActivity(), LoginRegisterActivity.class));
                break;
            case R.id.btn_login_register:
                startActivity(new Intent(getActivity(), LoginRegisterActivity.class));
                break;
        }
    }

    @Event(value = { R.id.isShowBalance,R.id.ivMine, R.id.rlEarnMoney, R.id.rlBalance, R.id.tvTotalMoneyTitle, R.id.tvTotalMoney, R.id.rlRegular,
            R.id.rlLittleItem, R.id.btnWithdraw, R.id.btnPay, R.id.rlRedPackets, R.id.btnRefresh,R.id.llLoanCalendar,
            R.id.rlFundsWater, R.id.llAboutWe, R.id.ivMessage})
    private void loginClicked(View v) {
        if (StaticMembers.IS_NEED_LOGIN) {
            startActivity(new Intent(getActivity(), LoginRegisterActivity.class));
        } else {
            switch (v.getId()) {
                case R.id.isShowBalance:
                    String  money = AppUtils.getFromSharedPreferences(getActivity(), ParamsKeys.LOGIN_FILE, ParamsKeys.LOGIN_KEY_TOTAL);
                    ReturnResultEntity<UserAccountEntity> entity = (ReturnResultEntity<UserAccountEntity>) AppUtils.fail2SetData(ParamsKeys.MINE_INFO);
                    if (StaticMembers.IS_SHOW_BALANCE){
                        setMineDataHide(entity);
                        StaticMembers.IS_SHOW_BALANCE = false;
                    }else{
                        setMineDataShow(entity);
                        StaticMembers.IS_SHOW_BALANCE = true;
                    }

                    //向sp中保存状态信息
                    SharedPreferenceUtils.save2SharedPreferences(getActivity(),ParamsKeys.MINE_FILE,ParamsKeys.IS_SHOW_BALANCE,StaticMembers.IS_SHOW_BALANCE);

                    break;
                //个人中心
                case R.id.ivMine:
                   // if (mList != null && mList.size() > 0) {
                        Intent intent2 = new Intent(getActivity(), PersonalSettingsActivity.class);
                        startActivity(intent2);
                   // }
                    break;
                //消息中心
                case R.id.ivMessage:
                    Intent intent1 = new Intent(getActivity(), MessageCenterActivity.class);
                    startActivity(intent1);
                    break;
                //累计收益
                case R.id.rlEarnMoney:
                    startActivity(new Intent(getActivity(), AddUpEarningsActivity.class));
                    break;
                //余额
                case R.id.rlBalance:
                    startActivity(new Intent(getActivity(), AccountBalanceActivity.class));
                    break;
                //账户总资产
                case R.id.tvTotalMoneyTitle:
                case R.id.tvTotalMoney:
                    Intent intent = new Intent(getActivity(), TotalMoneyActivity.class);
                    startActivity(intent);
                    break;
                //充值
                case R.id.btnPay:
                    if (mList != null && mList.size() > 0) {
                        if (StaticMembers.HK_STATUS == 1) {
                            if (StaticMembers.BANK_CARD_STATUS == 0) {
                                startActivity(new Intent(getActivity(), AddBankCardActivity.class));
                            } else {
                                startActivity(new Intent(getActivity(), PayInputMoneyActivity.class));
                            }
                        } else {
                            PopupWindow mPop = AppUtils.createHKPop(popView, getActivity());
                            mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                        }
                    }
                    break;
                //提现
                case R.id.btnWithdraw:
                    if (mList != null && mList.size() > 0) {
                        if (StaticMembers.HK_STATUS == 1) {
                            if (StaticMembers.BANK_CARD_STATUS == 0) {
                                startActivity(new Intent(getActivity(), AddBankCardActivity.class));
                            } else {
                                startActivity(new Intent(getActivity(), WithdrawActivity.class));
                            }
                        } else {
                            PopupWindow mPop = AppUtils.createHKPop(popView, getActivity());
                            mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                        }
                    }
                    break;
                //定期资产
                case R.id.rlRegular:
                    if (tvRegularMoney.getText().equals(getString(R.string.Immediately_lend))) {
                        //通知homepage跳到产品
                        EventMessage eventMessage = new EventMessage();
                        eventMessage.setType(EventMessage.HOMEPAGE_JUMP_TAB);
                        eventMessage.setObject(1);
                        EventBus.getDefault().post(eventMessage);
                    } else {
                        startActivity(new Intent(getActivity(), MyInvestActivity.class));
                    }
                    break;
                //资金流水
                case R.id.rlFundsWater:
                    startActivity(new Intent(getActivity(), FundsWaterActivity.class));
                    break;
                //回款日历
                case R.id.llLoanCalendar:
                    startActivity(new Intent(getActivity(), CalendarViewActivity.class));
                    break;
               /* //调查问卷
                case R.id.rlQuestionnaire:
                    Intent intent4 = new Intent(getActivity(), WebViewActivity.class);
                    intent4.putExtra(ParamsKeys.TYPE, ParamsValues.QUESTION_NAIRE);
                    intent4.putExtra(ParamsKeys.USER_ID,StaticMembers.USER_ID);
                    startActivity(intent4);
                    break;*/
                //红包
                case R.id.rlRedPackets:
                    startActivity(new Intent(getActivity(), RedPacketsActivity.class));
                    break;
                /*//自动投资
                case R.id.llEntrust:
                    if (StaticMembers.HK_STATUS == 1) {
                        startActivity(new Intent(getActivity(), AutoInvestActivity.class));
                    } else {
                        PopupWindow mPop1 = AppUtils.createHKPop(popView, getActivity());
                        mPop1.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                    }
                    break;*/
                //邀请好友
               /* case R.id.rlInviteFriend:
                    Intent intent3 = new Intent(getActivity(), WebViewActivity.class);
                    intent3.putExtra(ParamsKeys.TYPE, ParamsValues.RULE);
                    startActivity(intent3);
                    break;*/
                case R.id.btnRefresh:
                    requestDatas();
                    break;
            }
        }
    }

    @Event(value = {R.id.llAboutWe, R.id.rlMyBorrow, R.id.sdv_wonderful_activity1, R.id.sdv_wonderful_activity2})
    private void loginClicked2(View v) {
        switch (v.getId()) {
            //我要借款
            case R.id.rlMyBorrow:
                Intent intent = new Intent(getActivity(), WebViewHtmlActivity.class);
                intent.putExtra(ParamsKeys.TYPE,ParamsValues.MY_BORROW);
                startActivity(intent);
                break;
            //关于我们
            case R.id.llAboutWe:
                startActivity(new Intent(getActivity(), AboutOurActivity.class));
                break;
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onResume() {
        super.onResume();

        if (StaticMembers.IS_NEED_LOGIN) {
            rlNoLogin.setVisibility(View.VISIBLE);
            rlLogin.setVisibility(View.GONE);
            rlMine.setVisibility(View.GONE);
            viewSpace.setVisibility(View.VISIBLE);
            tvRemind.setVisibility(View.GONE);
            mPullDownScrollView.setPullRefreshEnable(false);
            clearInfo();
        } else {
            rlNoLogin.setVisibility(View.GONE);
            rlLogin.setVisibility(View.VISIBLE);
            rlMine.setVisibility(View.VISIBLE);
            mPullDownScrollView.setPullRefreshEnable(true);
            if (isVisible) {
                refreshData();
            }
        }
    }

    private void requestActivityInformation() {
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_FUND);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_GET_ACTIVITY_INFO);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mTpye = new TypeToken<ReturnResultEntity<ActivityInfoEntity>>() {
        }.getType();
        NetUtils.request(obj, mTpye, 1000 * 15, new IHttpRequestCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Object object) {
                ReturnResultEntity<ActivityInfoEntity> entity = (ReturnResultEntity<ActivityInfoEntity>) object;
                if (entity.isSuccess(getActivity())) {
                    if (entity.isNotNull()) {
                        setActivityInfo(entity);
                    }
                } else {
                    AppUtils.showToast(getActivity(), entity.getRemark());
                }
            }

            @Override
            public void onFailure() {

            }
        });

    }

    private void setActivityInfo(final ReturnResultEntity<ActivityInfoEntity> entity) {
        sdvWonderFulActivity1.setImageURI(Uri.parse(entity.getData().get(0).getCover_img()));
        sdvWonderFulActivity2.setImageURI(Uri.parse(entity.getData().get(1).getCover_img()));
        sdvWonderFulActivity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CallBackWebActivity.class);
                intent.putExtra("url",entity.getData().get(0).getUrl_route() );
                startActivity(intent);
            }
        });
        sdvWonderFulActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CallBackWebActivity.class);
                intent.putExtra("url", entity.getData().get(1).getUrl_route());
                startActivity(intent);
            }
        });
    }

    @SuppressLint("WrongConstant")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (StaticMembers.IS_NEED_LOGIN) {
                rlNoLogin.setVisibility(View.VISIBLE);
                rlLogin.setVisibility(View.GONE);
                rlMine.setVisibility(View.GONE);
                viewSpace.setVisibility(View.VISIBLE);
                tvRemind.setVisibility(View.GONE);
                mPullDownScrollView.setPullRefreshEnable(false);
                clearInfo();
                //TODO 活动信息
                requestActivityInformation();
            } else {
                rlNoLogin.setVisibility(View.GONE);
                rlLogin.setVisibility(View.VISIBLE);
                rlMine.setVisibility(View.VISIBLE);
                mPullDownScrollView.setPullRefreshEnable(true);
                refreshData();
                //TODO 活动信息
                requestActivityInformation();
            }
        }
    }


    //来源于登录成功和购买商品成功刷新页面数据
    public void onEventMainThread(EventMessage eventMessage) {
        if (eventMessage != null) {
            if (eventMessage.getType() == EventMessage.REFRESH_MINE) {
                hasLoadedOnce = false;
                requestDatas();
            }
        }
    }

    private void clearInfo() {
        StaticMembers.MOBILE = "";
        StaticMembers.USER_ID = "";
        StaticMembers.LOGIN_TOKEN = "";
        tvBalance.setText("0.00");
        tvEarnMoney.setText("0.00");
        tvTotalMoney.setText("0.00");
        tvRegularMoney.setText(getString(R.string.mine_invest_word));
        tvRegularMoney.setTextColor(Color.GRAY);
        tvLittleItemMoney.setText(getString(R.string.mine_invest_word));
        tvRedTip.setText(getString(R.string.mine_redtip_word));
        tvRedTip.setTextColor(Color.GRAY);
    }

    @SuppressLint("WrongConstant")
    private void setPersonalInfo(final ReturnResultEntity<UserAccountEntity> entity) {
        AppUtils.save2SharedPreferences(getActivity(), ParamsKeys.LOGIN_FILE, ParamsKeys.LOGIN_KEY_TOTAL, entity.getData().get(0).getAmount());
        //刷新首页总资产
        EventMessage eventMessage = new EventMessage();
        eventMessage.setType(EventMessage.UPDATE_INDEX_TOTAL);
        eventMessage.setObject(entity.getData().get(0).getAmount());
        EventBus.getDefault().post(eventMessage);
        tvRedTip.setVisibility(View.VISIBLE);
        tvRedTip.setText(entity.getData().get(0).getRed_num() + "个红包");
        tvRedTip.setTextColor(getResources().getColor(R.color.theme_color));
        if (Float.parseFloat(entity.getData().get(0).getAmount()) != 0) {
            ValueAnimator animator = ValueAnimator.ofFloat(0, Float.parseFloat(entity.getData().get(0).getAmount()));
            animator.setDuration(0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float num = (Float) animation.getAnimatedValue();
                    DecimalFormat fnum = new DecimalFormat("##.00");
                    String money = fnum.format(num);
                    if (StaticMembers.IS_SHOW_BALANCE){
                        setMineDataShow(entity);
                    }else{
                        setMineDataHide(entity);
                    }

                }
            });
            animator.setInterpolator(new LinearInterpolator());
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setTotalBalanceShow(entity);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    setTotalBalanceShow(entity);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            //lingkai
            setTotalBalanceShow(entity);
        }
    }
    //lingkai  展示总金额
    private void setTotalBalanceShow(ReturnResultEntity<UserAccountEntity> entity) {
        //获取保存的信息
        boolean IS_SHOW_BALANCE = SharedPreferenceUtils.getFromSharedPreferences(getActivity(), ParamsKeys.MINE_FILE, ParamsKeys.IS_SHOW_BALANCE);
        StaticMembers.IS_SHOW_BALANCE = IS_SHOW_BALANCE;
        if (StaticMembers.IS_SHOW_BALANCE) {
            setMineDataShow(entity);
        } else {
            setMineDataHide(entity);
        }
    }
    //设置我的数据展现
    private void setMineDataShow(ReturnResultEntity<UserAccountEntity> entity) {
        tvTotalMoney.setText(entity.getData().get(0).getAmount());
        tvEarnMoney.setText(entity.getData().get(0).getInterest());
        tvBalance.setText(entity.getData().get(0).getBalance());
        tvUserName.setText(entity.getData().get(0).getNameTitle());


        if (entity.getData().get(0).getDeposit().equals("-1")) {
            tvRegularMoney.setText(getString(R.string.mine_invest_word));
        } else {
            tvRegularMoney.setText(Double.valueOf(entity.getData().get(0).getDeposit()) + Double.valueOf(entity.getData().get(0).getEnjoy()) + "元");
            tvRegularMoney.setTextColor(getResources().getColor(R.color.theme_color));
        }
        if (entity.getData().get(0).getEnjoy().equals("-1")) {
            tvLittleItemMoney.setText(getString(R.string.mine_invest_word));
        } else {
            tvLittleItemMoney.setText(entity.getData().get(0).getEnjoy() + "元");
        }
        //设置图片
        Drawable showIcon = getResources().getDrawable(R.mipmap.mine_invest_open);
        isShowBalance.setImageDrawable(showIcon);
    }
    //设置我的数据隐藏
    private void setMineDataHide(ReturnResultEntity<UserAccountEntity> entity) {
        tvTotalMoney.setText("*****");
        tvEarnMoney.setText("*****");
        tvBalance.setText("*****");

        if (entity.getData().get(0).getDeposit().equals("-1")) {
            tvRegularMoney.setText(getString(R.string.mine_invest_word));
        } else {
            tvRegularMoney.setText("*****");
        }
        if (entity.getData().get(0).getEnjoy().equals("-1")) {
            tvLittleItemMoney.setText(getString(R.string.mine_invest_word));
        } else {
            tvLittleItemMoney.setText("*****");
        }
        //设置图片
        Drawable hideIcon = getResources().getDrawable(R.mipmap.mine_invest_closed);
        isShowBalance.setImageDrawable(hideIcon);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    //初始化下拉刷新控件
    private void initScroll() {
        mPullDownScrollView.setIXScrollViewListener(this);
        mPullDownScrollView.setView(mainView);
        if (llMain.getHeight() >= mPullDownScrollView.getHeight()) {
            mPullDownScrollView.setIsNeedLan(false);
        } else {
            mPullDownScrollView.setIsNeedLan(true);
        }
    }

    private void requestDatas() {
        if (StaticMembers.IS_NEED_LOGIN || hasLoadedOnce) {
            return;
        }
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_FUND);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_USER_ACCOUNT_NEW);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<UserAccountEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, 1000 * 15, new IHttpRequestCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                pbLoading.setVisibility(View.GONE);
                ivMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(Object object) {
                ReturnResultEntity<UserAccountEntity> entity = (ReturnResultEntity<UserAccountEntity>) object;
                if (entity.isSuccess(getActivity())) {
                    if (entity.isNotNull()) {
                        hasLoadedOnce = true;
                        StaticMembers.aCache.put(ParamsKeys.MINE_INFO, entity);
                        mList = entity.getData();
                        setUserState(entity);
                        setPersonalInfo(entity);
                    } else {
                        entity = (ReturnResultEntity<UserAccountEntity>) AppUtils.fail2SetData(ParamsKeys.MINE_INFO);
                        if (entity != null) {
                            mList = entity.getData();
                            setUserState(entity);
                            setPersonalInfo(entity);
                        }
                        AppUtils.showRequestFail(getActivity());
                    }

                  /*  if (StaticMembers.QUESTION_NAIRE_STATUS == 0) {
                        PopupWindow mPop = AppUtils.createQNPop(popViewQuestion, getActivity());
                        mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                    }*/

                    if(hadShowPop){
                        hadShowPop = !hadShowPop;
                    } else {
                        if(StaticMembers.HK_STATUS == 1){
                            if (StaticMembers.QUESTION_NAIRE_STATUS == 0) {
                                if (hadShowPop2) {
                                    hadShowPop2 = !hadShowPop2;
                                } else {
                                    PopupWindow mPop = AppUtils.createQNPop(popViewQuestion, getActivity());
                                    mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                                    hadShowPop2 = !hadShowPop2;
                                }
                            }
                        } else if (StaticMembers.QUESTION_NAIRE_STATUS == 0){
                            if (hadShowPop2){
                                hadShowPop2 = !hadShowPop2;
                            }else{
                                PopupWindow mPop = AppUtils.createQNPop(popViewQuestion, getActivity());
                                mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                                hadShowPop2 = !hadShowPop2;
                            }
                        } else {
                            PopupWindow mPop = AppUtils.createHKPop(popView, getActivity());
                            mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                            hadShowPop = !hadShowPop;
                        }
                    }
                } else {
                    AppUtils.showToast(getActivity(), entity.getRemark());
                    entity = (ReturnResultEntity<UserAccountEntity>) AppUtils.fail2SetData(ParamsKeys.MINE_INFO);
                    if (entity != null) {
                        mList = entity.getData();
                        setUserState(entity);
                        setPersonalInfo(entity);
                    }
                }
            }

            @Override
            public void onFailure() {
                ReturnResultEntity<UserAccountEntity> entity = (ReturnResultEntity<UserAccountEntity>) AppUtils.fail2SetData(ParamsKeys.MINE_INFO);
                if (entity != null) {
                    mList = entity.getData();
                    setUserState(entity);
                    setPersonalInfo(entity);
                }
                AppUtils.showConectFail2(getActivity());
            }
        });

    }

    private void setUserState(ReturnResultEntity<UserAccountEntity> entity) {
        StaticMembers.HK_STATUS = entity.getData().get(0).getIs_activate_hkaccount();
        StaticMembers.BANK_CARD_STATUS = entity.getData().get(0).getIs_bind_bankcard();
        StaticMembers.QUESTION_NAIRE_STATUS = entity.getData().get(0).getSurveyresult();
        if (String.valueOf(entity.getData().get(0).getBuyNewAble()) != null) {
            StaticMembers.IS_ABLE_BUY_NEW_PRODUCT = entity.getData().get(0).getBuyNewAble();
        }

        //保存用户状态信息
        AppUtils.save2SharedPreferences(getActivity(),ParamsKeys.USER_INFO_FILE,ParamsKeys.USER_IS_OPEN_ACCOUNT,
                String.valueOf(entity.getData().get(0).getIs_activate_hkaccount()));
        AppUtils.save2SharedPreferences(getActivity(),ParamsKeys.USER_INFO_FILE,ParamsKeys.USER_IS_ABLE_BUY_NEW_PRODUCT,
                String.valueOf(entity.getData().get(0).getBuyNewAble()));
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    private void refreshData() {
        mPullDownScrollView.stopRefresh();
        pbLoading.setVisibility(View.VISIBLE);
        ivMessage.setVisibility(View.GONE);
        hasLoadedOnce = false;
        requestDatas();
    }

    @Override
    public void onLoadMore() {

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            // remove掉保存的Fragment
            outState.remove(FRAGMENTS_TAG);
        }
    }
}
