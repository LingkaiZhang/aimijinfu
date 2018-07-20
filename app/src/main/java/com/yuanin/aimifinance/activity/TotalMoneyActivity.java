package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.entity.TotalAccountEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.view.RingProgressBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * 账户总资产
 */
public class TotalMoneyActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvRegularInvest)
    private TextView tvRegularInvest;
    @ViewInject(R.id.tvRegularWaitEarn)
    private TextView tvRegularWaitEarn;
    @ViewInject(R.id.tvBalance)
    private TextView tvBalance;
    @ViewInject(R.id.tvIceBalance)
    private TextView tvIceBalance;
    @ViewInject(R.id.tvCrash)
    private TextView tvCrash;
    @ViewInject(R.id.svMain)
    private ScrollView svMain;
    @ViewInject(R.id.tvNoContent)
    private TextView tvNoContent;
    @ViewInject(R.id.llNoNet)
    private LinearLayout llNoNet;
    @ViewInject(R.id.viewRemind)
    private View viewRemind;
    @ViewInject(R.id.viewLoading)
    private View viewLoading;
    @ViewInject(R.id.imageLoading)
    private ImageView loadingImage;
    @ViewInject(R.id.tvNoNet)
    private TextView tvNoNet;
    @ViewInject(R.id.tvAlreadyBonus)
    private TextView tvAlreadyBonus;
    @ViewInject(R.id.tvWaitBonus)
    private TextView tvWaitBonus;
    @ViewInject(R.id.myProgress)
    private RingProgressBar myProgress;
    @ViewInject(R.id.tvWaitCapital)
    private TextView tvWaitCapital;
    @ViewInject(R.id.tvFinishMoney)
    private TextView tvFinishMoney;
    @ViewInject(R.id.tvFinishEarn)
    private TextView tvFinishEarn;


    private Context context = TotalMoneyActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_money);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.TotalMoney), toptitleView, true);
        requestData();
    }

    @Event(value = {R.id.llBalance, R.id.rlMyInvestItem, R.id.btnRefresh,
            R.id.llWaitPrincipal, R.id.llWaitEarn, R.id.llIceBalance, R.id.llFinishMoney,
            R.id.llFinishEarn, R.id.llAlreadyBonus, R.id.llAvailableBalance
    })
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //可用余额
            case R.id.llBalance:
                break;
            //我的资产
            case R.id.rlMyInvestItem:
                startActivity(new Intent(context, MyInvestActivity.class));
                break;
            case R.id.btnRefresh:
                requestData();
                break;
            //待收本金
            case R.id.llWaitPrincipal:
                Intent intent = new Intent(context, MyInvestActivity.class);
                intent.putExtra("fragmentPosition",1);
                startActivity(intent);
                break;
            //待收收益
            case R.id.llWaitEarn:
                Intent intent1 = new Intent(context, AddUpEarningsActivity.class);
                intent1.putExtra("fragmentPosition",0);
                startActivity(intent1);
                break;
            //冻结金额
            case R.id.llIceBalance:
                Intent intent2 = new Intent(context, MyInvestActivity.class);
                intent2.putExtra("fragmentPosition",0);
                startActivity(intent2);
                break;
            //已收本金
            case R.id.llFinishMoney:
                Intent intent3 = new Intent(context, MyInvestActivity.class);
                intent3.putExtra("fragmentPosition",2);
                startActivity(intent3);
                break;
            //已收收益
            case R.id.llFinishEarn:
                Intent intent4 = new Intent(context, AddUpEarningsActivity.class);
                intent4.putExtra("fragmentPosition",2);
                startActivity(intent4);
                break;
            //已收奖励
            case R.id.llAlreadyBonus:
                Intent intent5 = new Intent(context, FundsWaterActivity.class);
                intent5.putExtra("currentFundType",7);
                startActivity(intent5);
                break;
            //可用余额
            case R.id.llAvailableBalance:
                startActivity(new Intent(context, AccountBalanceActivity.class));
                break;
        }
    }

    private void requestData() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_FUND);
            obj.put(ParamsKeys.MOTHED, ParamsValues.TOTAL_ACCOUNT_AMOUNT);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<TotalAccountEntity>>() {
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
                        ReturnResultEntity<TotalAccountEntity> entity = (ReturnResultEntity<TotalAccountEntity>) object;
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                initViews(entity.getData().get(0));
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
                }
        );
    }

    private void initViews(final TotalAccountEntity entity) {
        tvRegularInvest.setText(String.valueOf(entity.getDeposit() + entity.getEnjoy()));
        tvRegularWaitEarn.setText(String.valueOf(entity.getWait_deposit_interest() + entity.getWait_enjoy_interest()));
        tvAlreadyBonus.setText(String.valueOf(entity.getReward_amount()));
        tvWaitBonus.setText(String.valueOf(entity.getWait_reward_amount()));

        tvBalance.setText(String.valueOf(entity.getBalance()));
        tvIceBalance.setText(String.valueOf(entity.getAppoint() - entity.getWithdraw_amount()));
        tvCrash.setText(String.valueOf(entity.getWithdraw_amount()));

        tvFinishMoney.setText(String.valueOf(entity.getAlreadyCapital()));
        tvFinishEarn.setText(String.valueOf(entity.getAlreadyInterest()));
        tvWaitCapital.setText(String.valueOf(entity.getWaitCapital()));


        //圆环显示
        new Thread(){
            @Override
            public void run() {
                super.run();
                double total = entity.getEnjoy() + entity.getBalance() + entity.getDeposit()
                        + entity.getAppoint() - entity.getWithdraw_amount();
                double keyong = entity.getBalance() + entity.getAppoint() - entity.getWithdraw_amount();
                double dongjie = entity.getAppoint() - entity.getWithdraw_amount();
                if (keyong > 0 && dongjie >= 0) {
                    int progress = (int) Math.rint(keyong*100/total);
                    int progress2 = (int) Math.rint(dongjie*100/total);
                    for(int i=0; i<= progress; i++){
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (i >= progress2) {
                            myProgress.setProgress(i,progress2);
                        } else {
                            myProgress.setProgress(i,i);
                        }

                        myProgress.setTextString(format3(total) + "");
                    }
                } else {
                    myProgress.setProgress(0,0);
                    myProgress.setTextString(String.valueOf(total));
                }

            }
        }.start();

    }

    //double数字去两位小数
    public static String format3(double value) {

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        /*
         * setMinimumFractionDigits设置成2
         *
         * 如果不这么做，那么当value的值是100.00的时候返回100
         *
         * 而不是100.00
         */
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        /*
         * 如果想输出的格式用逗号隔开，可以设置成true
         */
        nf.setGroupingUsed(false);
        return nf.format(value);
    }

}
