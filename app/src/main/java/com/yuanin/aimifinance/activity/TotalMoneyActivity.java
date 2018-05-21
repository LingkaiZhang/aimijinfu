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

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;

/**
 * 账户总资产
 */
public class TotalMoneyActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvRegularInvest)
    private TextView tvRegularInvest;
    @ViewInject(R.id.tvRegularAlreadyEarn)
    private TextView tvRegularAlreadyEarn;
    @ViewInject(R.id.tvRegularWaitEarn)
    private TextView tvRegularWaitEarn;
    @ViewInject(R.id.tvLittleItemInvest)
    private TextView tvLittleItemInvest;
    @ViewInject(R.id.tvLittleItemAlreadyEarn)
    private TextView tvLittleItemAlreadyEarn;
    @ViewInject(R.id.tvLittleItemWaitEarn)
    private TextView tvLittleItemWaitEarn;
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
    @ViewInject(R.id.tvBonus)
    private TextView tvBonus;
    @ViewInject(R.id.tvWaitBonus)
    private TextView tvWaitBonus;


    private Context context = TotalMoneyActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_money);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.TotalMoney), toptitleView, true);
        requestData();
    }

    @Event(value = {R.id.llBalance, R.id.llRegular, R.id.llLittleItem, R.id.btnRefresh})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //可用余额
            case R.id.llBalance:
                break;
            //爱米定期
            case R.id.llRegular:
                startActivity(new Intent(context, MyInvestActivity.class));
                break;
            //爱米优选
            case R.id.llLittleItem:
                Intent intent = new Intent(context, MyInvestActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.btnRefresh:
                requestData();
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

    private void initViews(TotalAccountEntity entity) {
        tvRegularInvest.setText(String.valueOf(entity.getDeposit()));
        tvRegularAlreadyEarn.setText(String.valueOf(entity.getDeposit_interest()));
        tvRegularWaitEarn.setText(String.valueOf(entity.getWait_deposit_interest()));

        tvLittleItemInvest.setText(String.valueOf(entity.getEnjoy()));
        tvLittleItemAlreadyEarn.setText(String.valueOf(entity.getEnjoy_interest()));
        tvLittleItemWaitEarn.setText(String.valueOf(entity.getWait_enjoy_interest()));

        tvBonus.setText(String.valueOf(entity.getAll_reward_amount()));
        tvAlreadyBonus.setText(String.valueOf(entity.getReward_amount()));
        tvWaitBonus.setText(String.valueOf(entity.getWait_reward_amount()));

        tvBalance.setText(String.valueOf(entity.getBalance()));
        tvIceBalance.setText(String.valueOf(entity.getAppoint()));
        tvCrash.setText(String.valueOf(entity.getWithdraw_amount()));
    }

}
