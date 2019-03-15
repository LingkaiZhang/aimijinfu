package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.entity.CertificationEntity;
import com.yuanin.aimifinance.entity.HKRegisterEntity;
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

public class AddBankCardActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvName)
    private TextView tvName;
    @ViewInject(R.id.tvIdCard)
    private TextView tvIdCard;
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
    @ViewInject(R.id.etBankcard)
    private EditText etBankcard;
    @ViewInject(R.id.etPhone)
    private EditText etPhone;
    @ViewInject(R.id.tvSupportBank)
    private TextView tvSupportBank;

    private Context context = AddBankCardActivity.this;
    private GeneralDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_card);
        x.view().inject(this);
        initTopBarWithPhone("绑定银行卡", toptitleView);
        requestData();

        SpannableString spannableString = new SpannableString(getString(R.string.support_bank));
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.text_gray));
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(1.1f);
        spannableString.setSpan(colorSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(sizeSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvSupportBank.setText(spannableString);
    }

    private void requestData() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_SAFE);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_GET_CERTIFIED);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<CertificationEntity>>() {
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
                        ReturnResultEntity<CertificationEntity> entity = (ReturnResultEntity<CertificationEntity>) object;
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                tvName.setText(entity.getData().get(0).getCertifier());
                                tvIdCard.setText(entity.getData().get(0).getIdcardno());
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


    @Event(value = {R.id.btnConfirm, R.id.btnRefresh, R.id.btnCheckNetwork})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //提交
            case R.id.btnConfirm:
                if (etBankcard.getText().toString().trim().length() == 0) {
                    AppUtils.showToast(this, "请输入您的银行卡号");
                    return;
                }
                if (etPhone.getText().toString().trim().length() != 11) {
                    AppUtils.showToast(this, getResources().getString(R.string.login_input_right_phone));
                    return;
                }
                JSONObject obj = AppUtils.getPublicJsonObject(true);
                try {
                    obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_SAFE);
                    obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_SAVE_BANK_CARD);
                    obj.put(ParamsKeys.CARD_NO, AppUtils.rsaEncode(this, etBankcard.getText().toString().trim()));
                    obj.put(ParamsKeys.CARD_MOBILE, AppUtils.rsaEncode(this, etPhone.getText().toString().trim()));
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
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                Intent intent = new Intent(context, HKRegisterWebActivity.class);
                                intent.putExtra("url", entity.getData().get(0).getRedirect_url());
                                startActivity(intent);
                            }
                            AddBankCardActivity.this.finish();
                        }else{
                            AppUtils.showToast(context, entity.getRemark());
                        }
                    }

                    @Override
                    public void onFailure() {
                        AppUtils.showConectFail(context);
                    }
                });
                break;
            //刷新
            case R.id.btnRefresh:
                requestData();
                break;
            //检查网络
            case R.id.btnCheckNetwork:
                AppUtils.checkNetwork(context);
                break;
        }
    }
}
