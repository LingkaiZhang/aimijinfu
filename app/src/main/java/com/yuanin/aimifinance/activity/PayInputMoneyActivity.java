package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.BindingCardEntity;
import com.yuanin.aimifinance.entity.HKRegisterEntity;
import com.yuanin.aimifinance.entity.PayEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
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
 * 充值
 */
public class PayInputMoneyActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.etMoney)
    private EditText etMoney;
    @ViewInject(R.id.imageIcon)
    private SimpleDraweeView imageIcon;
    @ViewInject(R.id.tvBankName)
    private TextView tvBankName;
    @ViewInject(R.id.tvBankCard)
    private TextView tvBankCard;
    @ViewInject(R.id.tvOnceMoney)
    private TextView tvOnceMoney;
    @ViewInject(R.id.tvDayLimit)
    private TextView tvDayLimit;
    @ViewInject(R.id.tvBalance)
    private TextView tvBalance;
    @ViewInject(R.id.tvAfterMoney)
    private TextView tvAfterMoney;
    @ViewInject(R.id.tvNoContent)
    private TextView tvNoContent;
    @ViewInject(R.id.llNoNet)
    private LinearLayout llNoNet;
    @ViewInject(R.id.llMain)
    private ScrollView llMain;
    @ViewInject(R.id.viewRemind)
    private View viewRemind;
    @ViewInject(R.id.viewLoading)
    private View viewLoading;
    @ViewInject(R.id.imageLoading)
    private ImageView loadingImage;
    @ViewInject(R.id.tvNoNet)
    private TextView tvNoNet;

    private Context context = PayInputMoneyActivity.this;

    private double minMoney, balance = 0;
    private int money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_input_money);
        x.view().inject(this);
        initTopBarWithPhone(getResources().getString(R.string.Pay), toptitleView);
        money = getIntent().getIntExtra("money", 0);
        if (money != 0) {
            etMoney.setText(String.valueOf(money));
            Editable etext = etMoney.getText();
            Selection.setSelection(etext, etext.length());
        }
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int dotCount = 0;
                for (int i = 0; i < s.toString().length(); i++) {
                    if (s.toString().charAt(i) == '.') {
                        dotCount++;
                    }
                }
                if (dotCount > 1) {
                    s = s.toString().subSequence(0, s.toString().length() - 1);
                    etMoney.setText(s);
                    etMoney.setSelection(s.length());
                    return;
                }
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        etMoney.setText(s);
                        etMoney.setSelection(s.length());
                    }
                }
                if (s.toString().trim().length() > 0 && s.toString().trim().substring(0, 1).equals(".")) {
                    s = "0" + s;
                    etMoney.setText(s);
                    etMoney.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etMoney.setText(s.subSequence(0, 1));
                        etMoney.setSelection(1);
                        return;
                    }
                }
                if (s.length() > 0) {
                    double afterMoney = balance + Double.parseDouble(s.toString());
                    java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    tvAfterMoney.setText(nf.format(afterMoney) + "");
                } else {
                    tvAfterMoney.setText(balance + "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_FUND);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_HK_BANK_AMOUNT);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<PayEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, new IHttpRequestCallBack() {
                    @Override
                    public void onStart() {
                        ((Animatable) loadingImage.getDrawable()).start();
                        viewRemind.setVisibility(View.GONE);
                        viewLoading.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
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
                        ReturnResultEntity<PayEntity> entity = (ReturnResultEntity<PayEntity>) object;
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                tvBalance.setText(entity.getData().get(0).getBalance());
                                balance = Double.parseDouble(entity.getData().get(0).getBalance());
                                minMoney = Double.parseDouble(entity.getData().get(0).getBank().get(0).getMin_recharge());
                                initView(entity.getData().get(0).getBank().get(0));
                            }
                            llMain.setVisibility(View.VISIBLE);
                            tvNoContent.setVisibility(View.GONE);
                            llNoNet.setVisibility(View.GONE);
                        } else {
                            llMain.setVisibility(View.GONE);
                            tvNoContent.setVisibility(View.GONE);
                            llNoNet.setVisibility(View.VISIBLE);
                            AppUtils.showToast(context, entity.getRemark());
                        }
                    }

                    @Override
                    public void onFailure() {
                        llMain.setVisibility(View.GONE);
                        tvNoContent.setVisibility(View.GONE);
                        llNoNet.setVisibility(View.VISIBLE);
                        AppUtils.showConectFail(context);
                    }
                }
        );
    }

    private void initView(BindingCardEntity entity) {
        etMoney.setHint("最小充值金额为" + entity.getMin_recharge() + "元");
        tvBankName.setText(entity.getFull_name());
        tvBankCard.setText(entity.getCard_no());
        tvOnceMoney.setText(entity.getSinglepay());
        tvDayLimit.setText(entity.getDaymaxpay());
        imageIcon.setImageURI(Uri.parse(entity.getLogo()));
        double afterMoney = balance + money;
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        tvAfterMoney.setText(nf.format(afterMoney) + "");
        etMoney.requestFocus();
    }

    @Event(value = {R.id.btnPay, R.id.btnRefresh, R.id.btnCheckNetwork})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //刷新
            case R.id.btnRefresh:
                requestData();
                break;
            //检查网络
            case R.id.btnCheckNetwork:
                AppUtils.checkNetwork(context);
                break;
            //提交充值
            case R.id.btnPay:
                String buyqty = etMoney.getText().toString().trim();
                if (buyqty.length() == 0) {
                    AppUtils.showToast(this, "请输入正确的充值金额");
                    return;
                }
                if (Double.parseDouble(buyqty) < minMoney) {
                    AppUtils.showToast(this, "最小充值金额为" + minMoney + "元");
                    return;
                }
                JSONObject obj = AppUtils.getPublicJsonObject(true);
                try {
                    obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_FUND);
                    obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_HK_RECHARGE);
                    obj.put(ParamsKeys.AMOUNT, buyqty);
                    String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                    obj.put(ParamsKeys.TOKEN, token);
                    obj.remove(ParamsKeys.KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Type mType = new TypeToken<ReturnResultEntity<HKRegisterEntity>>() {
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
                        ReturnResultEntity<HKRegisterEntity> entity = (ReturnResultEntity<HKRegisterEntity>) object;
                        if (entity.isSuccess(PayInputMoneyActivity.this)) {
                            if (entity.isNotNull()) {
                                Intent intent = new Intent(PayInputMoneyActivity.this, HKRegisterWebActivity.class);
                                intent.putExtra("url", entity.getData().get(0).getRedirect_url());
                                startActivity(intent);
                            }
                            PayInputMoneyActivity.this.finish();
                        } else {
                            AppUtils.showToast(PayInputMoneyActivity.this, entity.getRemark());
                        }
                    }

                    @Override
                    public void onFailure() {
                        AppUtils.showConectFail(PayInputMoneyActivity.this);
                    }
                });
                break;
        }
    }
}
