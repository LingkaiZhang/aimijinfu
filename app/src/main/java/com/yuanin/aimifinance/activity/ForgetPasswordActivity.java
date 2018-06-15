package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.view.CodeUtils;
import com.yuanin.aimifinance.view.CountDownTextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;

public class ForgetPasswordActivity extends BaseActivity {

    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.ed_sms_code)
    private EditText edSmsCode;
    @ViewInject(R.id.countDownTextView)
    private CountDownTextView countDownTextView;
    @ViewInject(R.id.btnConfirm)
    private Button btnConfirm;

    private Context context;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        x.view().inject(this);
        initTopBar("找回登录密码", toptitleView, true);
        phone = getIntent().getStringExtra("phone");
        context = getApplicationContext();
        initView();
        initData();
    }

    private void initData() {
        //获取注册短信验证码
        requestForgetPasswordSmsCode();
    }

    private void initView() {
        edSmsCode.setInputType( InputType.TYPE_CLASS_NUMBER);
        edSmsCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edSmsCode.getText().toString().trim().length() > 6){
                    String s1 = edSmsCode.getText().toString().substring(0, 6);
                    edSmsCode.setText(s1);
                    edSmsCode.setSelection(s1.length());
                }
                if (edSmsCode.getText().toString().trim().length() != 6) {
                    btnConfirm.setBackgroundResource(R.mipmap.login_button_enable);
                    btnConfirm.setEnabled(false);
                } else {
                    btnConfirm.setBackgroundResource(R.drawable.selector_theme_corner_button);
                    btnConfirm.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        countDownTextView.setCountDownMillis(60*1000);
        countDownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取注册短信验证码
                requestForgetPasswordSmsCode();
            }
        });
        countDownTextView.start();
    }

    @Event(value = {R.id.btnConfirm})
    private void onViewClicked(View v){
        switch (v.getId()) {
            //点击下一步
            case R.id.btnConfirm:
                //进行短信验证
                requestVerificationSms();
                break;

        }
    }

    private void requestVerificationSms() {
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE,ParamsValues.MODULE_USER);
            obj.put(ParamsKeys.MOTHED,ParamsValues.MOTHED_SMS_VERIFICATION);
            obj.put(ParamsKeys.MOBILE,AppUtils.rsaEncode(this, phone));
            obj.put(ParamsKeys.SMS_VERIFY_CODE,edSmsCode.getText().toString().trim());
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType2 = new TypeToken<ReturnResultEntity<?>>() {
        }.getType();
        NetUtils.request(this, obj, mType2, new IHttpRequestCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Object object) {
                ReturnResultEntity<?> entity = (ReturnResultEntity<?>) object;
                if (entity.isSuccess(context)) {
                    String smsCode = edSmsCode.getText().toString().trim();
                    Intent intent = new Intent(ForgetPasswordActivity.this, SetLoginPasswordActivity.class);
                    intent.putExtra("phone",phone);
                    intent.putExtra("smsCode", smsCode);
                    intent.putExtra("where","forgetpassword");
                    startActivity(intent);
                }
                AppUtils.showToast(context, entity.getRemark());
            }

            @Override
            public void onFailure() {
                AppUtils.showConectFail(context);
            }
        });
    }

    private void requestForgetPasswordSmsCode() {
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_SMS);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_SEND);
            obj.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, phone));
            obj.put(ParamsKeys.TYPE, ParamsValues.SEND_FIND_PASSWORD);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType2 = new TypeToken<ReturnResultEntity<?>>() {
        }.getType();
        NetUtils.request(this, obj, mType2, new IHttpRequestCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Object object) {
                ReturnResultEntity<?> entity = (ReturnResultEntity<?>) object;
                if (entity.isSuccess(context)) {

                }
                AppUtils.showToast(context, entity.getRemark());
            }

            @Override
            public void onFailure() {
                AppUtils.showConectFail(context);
            }
        });
    }

}
