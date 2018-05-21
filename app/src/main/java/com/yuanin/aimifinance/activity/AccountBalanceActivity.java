package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.entity.UserBalanceEntity;
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

public class AccountBalanceActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvBalance)
    private TextView tvBalance;
    @ViewInject(R.id.tvWithdrawing)
    private TextView tvWithdrawing;
    @ViewInject(R.id.tvIceBalance)
    private TextView tvIceBalance;
    @ViewInject(R.id.tvPayTotal)
    private TextView tvPayTotal;
    @ViewInject(R.id.tvWithdrawTotal)
    private TextView tvWithdrawTotal;

    private Context context = AccountBalanceActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_balance);
        x.view().inject(this);
        initTopBar("账户余额", toptitleView, true);
        requestData();
    }

    @Event(value = {R.id.btnRefresh})
    private void loginClicked(View v) {
        switch (v.getId()) {
            case R.id.btnRefresh:
                requestData();
                break;
        }
    }


    private void requestData() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_FUND);
            obj.put(ParamsKeys.MOTHED, ParamsValues.TOTAL_USER_ACCOUNT_AMOUNT);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<UserBalanceEntity>>() {
        }.getType();
        NetUtils.request(this, obj, mType, new IHttpRequestCallBack() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onSuccess(Object object) {
                        ReturnResultEntity<UserBalanceEntity> entity = (ReturnResultEntity<UserBalanceEntity>) object;
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                initViews(entity.getData().get(0));
                            } else {
                                AppUtils.showRequestFail(context);
                            }
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

    private void initViews(UserBalanceEntity entity) {
        tvBalance.setText(String.valueOf(entity.getBalance()));
        tvWithdrawing.setText(String.valueOf(entity.getWithdraw_amount()));
        tvPayTotal.setText(String.valueOf(entity.getAll_recharge_amount()));
        tvIceBalance.setText(String.valueOf(entity.getAppoint()));
        tvWithdrawTotal.setText(String.valueOf(entity.getAll_withdraw_amount()));
    }
}