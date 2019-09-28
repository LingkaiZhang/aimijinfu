package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.AimiCashEntity;
import com.yuanin.aimifinance.entity.HKRegisterEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.LogUtils;
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
 * 新网提现
 */
public class WithdrawActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.etMoney)
    private EditText etMoney;
    @ViewInject(R.id.tvFee)
    private TextView tvFee;
    @ViewInject(R.id.tvBalance)
    private TextView tvBalance;
    @ViewInject(R.id.tvGetMoney)
    private TextView tvGetMoney;
    @ViewInject(R.id.tvCount)
    private TextView tvCount;
    @ViewInject(R.id.tvBankName)
    private TextView tvBankName;
    @ViewInject(R.id.tvEveryFee)
    private TextView tvEveryFee;
    @ViewInject(R.id.tvBankCard)
    private TextView tvBankCard;
    @ViewInject(R.id.tvNoContent)
    private TextView tvNoContent;
    @ViewInject(R.id.llNoNet)
    private LinearLayout llNoNet;
    @ViewInject(R.id.svContent)
    private ScrollView svContent;
    @ViewInject(R.id.viewRemind)
    private View viewRemind;
    @ViewInject(R.id.viewLoading)
    private View viewLoading;
    @ViewInject(R.id.imageLoading)
    private ImageView loadingImage;
    @ViewInject(R.id.tvNoNet)
    private TextView tvNoNet;
    @ViewInject(R.id.llTips)
    private LinearLayout llTips;
    @ViewInject(R.id.tvOnceMoney)
    private TextView tvOnceMoney;
    @ViewInject(R.id.tvDayLimit)
    private TextView tvDayLimit;
    @ViewInject(R.id.imageIcon)
    private SimpleDraweeView imageIcon;

    @ViewInject(R.id.tv_freeCount)
    private TextView tv_freeCount;
    @ViewInject(R.id.cb_normalwithdraw)
    private CheckBox cb_normalwithdraw;
    @ViewInject(R.id.cb_quickWithdraw)
    private CheckBox cb_quickWithdraw;
    @ViewInject(R.id.tvNormalWithdraw)
    private TextView tvNormalWithdraw;
    @ViewInject(R.id.tvBalanceDes)
    private TextView tvBalanceDes;


    private String balance;
    private String fee = "2";
    private Context context = WithdrawActivity.this;
    private boolean isFirst = true;
    //提现类型 1 普通提现 2 快速提现
    private int cash_type = 1;
    private String cash_fee = "5.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        x.view().inject(this);
        initTopBarWithPhone(getResources().getString(R.string.Withdraw_xinwang), toptitleView);
        initListener();
        //提现方式选择
        initView();
    }

    private void initView() {

        Intent intent = getIntent();
        String xinwangBlance = intent.getStringExtra("XinwangBlance");

        tvBalance.setText(xinwangBlance);
        etMoney.setText(xinwangBlance);
        tvBalanceDes.setText("新网可用余额(元)");
        initEdittext(etMoney.getText());


        cb_normalwithdraw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_quickWithdraw.setChecked(false);
                    tvFee.setText(fee);
                    initEdittext(etMoney.getText().toString());
                } else {
                    cb_quickWithdraw.setChecked(true);
                }
            }
        });
        cb_quickWithdraw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_normalwithdraw.setChecked(false);
                    tvFee.setText(cash_fee);
                    initEdittext(etMoney.getText().toString());
                } else {
                    cb_normalwithdraw.setChecked(true);
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
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_FUND);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_AIMI_CASH_FEE);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<AimiCashEntity>>() {
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
                        ReturnResultEntity<AimiCashEntity> entity = (ReturnResultEntity<AimiCashEntity>) object;
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                tvBankName.setText(entity.getData().get(0).getBank().get(0).getFull_name());
                                tvBankCard.setText(entity.getData().get(0).getBank().get(0).getCard_no());
                                tvOnceMoney.setText(entity.getData().get(0).getBank().get(0).getSinglepay());
                                tvDayLimit.setText(entity.getData().get(0).getBank().get(0).getDaymaxpay());
                                imageIcon.setImageURI(Uri.parse(entity.getData().get(0).getBank().get(0).getLogo()));
                                initView(entity);
                                svContent.setVisibility(View.VISIBLE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.GONE);
                            } else {
                                svContent.setVisibility(View.GONE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.VISIBLE);
                            }
                        } else {
                            svContent.setVisibility(View.GONE);
                            tvNoContent.setVisibility(View.GONE);
                            llNoNet.setVisibility(View.VISIBLE);
                            AppUtils.showToast(context, entity.getRemark());
                        }
                    }

                    @Override
                    public void onFailure() {
                        svContent.setVisibility(View.GONE);
                        tvNoContent.setVisibility(View.GONE);
                        llNoNet.setVisibility(View.VISIBLE);
                        AppUtils.showConectFail(context);
                    }
                }
        );
    }

    @Event(value = {R.id.btnWithdraw, R.id.btnRefresh, R.id.tvWithdrawAll, R.id.btnCheckNetwork})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tvWithdrawAll:
                if (Double.parseDouble(balance) > 0) {
                    etMoney.setText(balance);
                    etMoney.setSelection(balance.length());
                    initEdittext(balance);
                } else {
                    AppUtils.showToast(this, "无可用余额");
                }
                break;
            //刷新
            case R.id.btnRefresh:
                requestData();
                break;
            //检查网络
            case R.id.btnCheckNetwork:
                AppUtils.checkNetwork(this);
                break;
            //提交提现
            case R.id.btnWithdraw:
                //提现方式选择
                if (cb_normalwithdraw.isChecked() && cb_quickWithdraw.isChecked() == false) {
                    cash_type = 1;
                } else if (cb_quickWithdraw.isChecked() && cb_normalwithdraw.isChecked() == false) {
                    cash_type = 2;
                } else {
                    AppUtils.showToast(this, "请选择提现方式.");
                    return;
                }
                if (etMoney.getText().toString().trim().length() == 0 || Double.parseDouble(etMoney.getText().toString().trim()) <= 0) {
                    AppUtils.showToast(this, getResources().getString(R.string.withdraw_input_more_zero));
                    return;
                }
                /*if (Double.parseDouble(etMoney.getText().toString().trim()) > Double.parseDouble(balance)) {
                    AppUtils.showToast(this, getResources().getString(R.string.withdraw_no_balance));
                    return;
                }*/
                if (Double.parseDouble(tvGetMoney.getText().toString()) <= 0) {
                    AppUtils.showToast(this, getResources().getString(R.string.withdraw_get_money_not_zero));
                    return;
                }
                /*if (Double.parseDouble(etMoney.getText().toString().trim()) > 50000) {
                    AppUtils.showToast(this, getResources().getString(R.string.withdraw_limit));
                    return;
                }*/
                JSONObject obj = AppUtils.getPublicJsonObject(true);
                try {
                    obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_FUND);
                    obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_HK_CASH);
                    obj.put(ParamsKeys.AMOUNT, etMoney.getText().toString().trim());
                    obj.put(ParamsKeys.CASH_TYPE, String.valueOf(cash_type));
                    String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                    obj.put(ParamsKeys.TOKEN, token);
                    obj.remove(ParamsKeys.KEY);
                    LogUtils.d(this, "obj = " + obj);
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
                                if (entity.isSuccess(WithdrawActivity.this)) {
                                    if (entity.isNotNull()) {
                                        Intent intent = new Intent(WithdrawActivity.this, HKRegisterWebActivity.class);
                                        intent.putExtra("url", entity.getData().get(0).getRedirect_url());
                                        startActivity(intent);
                                    }
                                    WithdrawActivity.this.finish();
                                } else {
                                    AppUtils.showToast(WithdrawActivity.this, entity.getRemark());
                                }
                            }

                            @Override
                            public void onFailure() {
                                AppUtils.showConectFail(context);
                            }
                        }
                );
                break;
        }
    }


    private void initView(ReturnResultEntity<AimiCashEntity> entity) {
        if (isFirst) {
            isFirst = false;
            for (int i = 0; i < entity.getData().get(0).getCue().size(); i++) {
                TextView textView = new TextView(this);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setTextColor(getResources().getColor(R.color.text_light_gray));
                textView.setTextSize(12);
                textView.setPadding(0, AppUtils.dip2px(this, 8), 0, 0);
                textView.setText(entity.getData().get(0).getCue().get(i).getStr());
                llTips.addView(textView);
            }
        }
        fee = entity.getData().get(0).getNormalFee();
        cash_fee = entity.getData().get(0).getQuickFee();
        balance = entity.getData().get(0).getBalance();
        //tvBalance.setText(balance);
        //tvFee.setText(fee);
        tvEveryFee.setText("( " + Integer.parseInt(fee.split("\\.")[0]) + "元/次 )");
        tvCount.setText(entity.getData().get(0).getQty());
        tv_freeCount.setText(entity.getData().get(0).getQty());
        //提现金额不可编辑
        etMoney.setFocusable(false);
        etMoney.setFocusableInTouchMode(false);

        AppUtils.openKeyboard(etMoney);


        SpannableString spannableString = new SpannableString("预计下一个工作日到账,遇双休日和法定节假日顺延,无金额限制.");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.theme_color));
        spannableString.setSpan(colorSpan, 2, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvNormalWithdraw.setText(spannableString);
    }

    private void initListener() {

        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                initEdittext(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initEdittext(CharSequence s) {
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
        if (s.length() > 0) {
            if (s.toString().trim().substring(0, 1).equals(".")) {
                s = "0" + s;
                etMoney.setText(s);
                etMoney.setSelection(2);
            }
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
            if (cb_quickWithdraw.isChecked()) {
                double getMoney = Double.parseDouble(s.toString()) - Double.parseDouble(cash_fee);
                if (getMoney <= 0) {
                    tvGetMoney.setText("0.00");
                } else {
                    java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    tvGetMoney.setText(nf.format(getMoney));
                }
            } else {
               /* double getMoney = Double.parseDouble(s.toString()) - Double.parseDouble(fee);
                if (getMoney <= 0) {
                    tvGetMoney.setText("0.00");
                } else {
                    java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    tvGetMoney.setText(nf.format(getMoney));
                }*/
                java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
                nf.setGroupingUsed(false);
                double inputMoney = Double.parseDouble(s.toString());
                if (inputMoney < 100) {
                    double getMoney = inputMoney - Double.parseDouble(fee);
                    if (getMoney <= 0) {
                        tvGetMoney.setText("0.00");
                        tvFee.setText(fee);
                    } else {
                        tvGetMoney.setText(nf.format(getMoney));
                        tvFee.setText(fee);
                    }
                } else {
                    double getMoney = inputMoney * (1 - 0.02);
                    tvGetMoney.setText(nf.format(getMoney));
                    tvFee.setText(nf.format(inputMoney * 0.02));
                }
            }
        } else {
            tvGetMoney.setText("0.00");
        }
    }
}
