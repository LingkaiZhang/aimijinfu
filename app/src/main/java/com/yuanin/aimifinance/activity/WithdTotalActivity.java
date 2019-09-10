package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.AimiCashEntity;
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

public class WithdTotalActivity extends BaseActivity {

    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.btnXinwangWithdraw)
    private Button btnXinwangWithdraw;
    @ViewInject(R.id.btnAimiWithdraw)
    private Button btnAimiWithdraw;
    @ViewInject(R.id.tvXinWangBalance)
    private TextView tvXinWangBalance;
    @ViewInject(R.id.tvAimiBalance)
    private TextView tvAimiBalance;
    @ViewInject(R.id.tvBalance)
    private TextView tvBalance;

    private Context context = WithdTotalActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withd_total);
        x.view().inject(this);
        initTopBarWithPhone("可用余额", toptitleView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
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
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onSuccess(Object object) {
                        ReturnResultEntity<TotalAccountEntity> entity = (ReturnResultEntity<TotalAccountEntity>) object;
                        if (entity.isSuccess(context)) {
                                if (entity.isNotNull()) {
                                    tvXinWangBalance.setText(entity.data.get(0).getRemote_amount());
                                    tvAimiBalance.setText(entity.data.get(0).getLocal_amount());
                                    tvBalance.setText(String.valueOf(entity.data.get(0).getBalance()));
                                } else {
                                    AppUtils.showToast(context, entity.getRemark());
                                }
                            }
                    }

                    @Override
                    public void onFailure() {
                    }
                }
        );
    }


    @Event(value = {R.id.btnAimiWithdraw, R.id.btnXinwangWithdraw})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btnXinwangWithdraw:
                Intent intent = new Intent(context, WithdrawActivity.class);
                intent.putExtra("XinwangBlance", tvXinWangBalance.getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.btnAimiWithdraw:
                Intent intent1 = new Intent(context, AimiWithdrawActivity.class);
                intent1.putExtra("AimiBalance", tvAimiBalance.getText().toString().trim());
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
