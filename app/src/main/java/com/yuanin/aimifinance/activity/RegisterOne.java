package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.CaptchaEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.entity.VerifyEntity;
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

import java.io.InputStream;
import java.lang.reflect.Type;

public class RegisterOne extends BaseActivity {

    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.image_code)
    private SimpleDraweeView imageCode;
    @ViewInject(R.id.ed_image_code)
    private EditText edImageCode;
    @ViewInject(R.id.ed_sms_code)
    private EditText edSmsCode;
    @ViewInject(R.id.countDownTextView)
    private CountDownTextView countDownTextView;
    @ViewInject(R.id.btnConfirm)
    private Button btnConfirm;

    private Context context;
    private CodeUtils codeUtils;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.Register), toptitleView, true);
        context = getApplicationContext();
        initView();
        initData();
    }

    private void initData() {
        phone = getIntent().getStringExtra("phone");
        //获取注册短信验证码
        requestRegisterSmsCode();
        //获取图形验证码
        requestRegisterImageCode();
    }

    private void requestRegisterImageCode() {
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_CAPTCHA);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_GET_CAPTCHA);
            obj.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, phone));
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType2 = new TypeToken<ReturnResultEntity<CaptchaEntity>>() {
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
                ReturnResultEntity<CaptchaEntity> entity = (ReturnResultEntity<CaptchaEntity>) object;
                if (entity.isSuccess(context)) {
                    if (entity.isNotNull()) {
                        String captcha = entity.getData().get(0).getCaptcha();
                        //设置图形验证码
                        codeUtils = CodeUtils.getInstance();
                        Bitmap bitmap = codeUtils.createBitmap(captcha);
                        imageCode.setImageBitmap(bitmap);
                    }
                    AppUtils.showToast(context, entity.getRemark());
                } else {
                    AppUtils.showToast(context, entity.getRemark());
                }
            }

            @Override
            public void onFailure() {
                AppUtils.showConectFail(context);
            }
        });
    }

    private void requestRegisterSmsCode() {
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_SMS);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_SEND);
            obj.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, phone));
            obj.put(ParamsKeys.TYPE, ParamsValues.SEND_REGISTER);
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

    private void initView() {

        //图形验证码输入框
        edImageCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edImageCode.getText().toString().trim().length() > 4){
                    String s1 = edImageCode.getText().toString().substring(0, 4);
                    edImageCode.setText(s1);
                    edImageCode.setSelection(s1.length());
                }
                if (edSmsCode.getText().toString().trim().length() != 6|| edImageCode.getText().toString().trim().length() != 4) {
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
                if (edSmsCode.getText().toString().trim().length() != 6|| edImageCode.getText().toString().trim().length() != 4) {
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
                requestRegisterSmsCode();
            }
        });
        countDownTextView.start();
    }

    @Event(value = {R.id.btnConfirm, R.id.image_code})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //点击图形验证码
            case R.id.image_code:
                requestRegisterImageCode();
                break;
            //点击下一步
            case R.id.btnConfirm:
                String imageCode = edImageCode.getText().toString().trim();
                if (null == imageCode || TextUtils.isEmpty(imageCode)) {
                    Toast.makeText(this, "请输入图片验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = codeUtils.getCode();
                Log.e("code", code);
                if (code.equalsIgnoreCase(imageCode)) {
                //请求校验
                    requestRegisterVerifyCode();
                } else {
                    Toast.makeText(this, "图片验证码错误", Toast.LENGTH_SHORT).show();
                    return;
                }


                break;

        }
    }

    private void requestRegisterVerifyCode() {
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_USER);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_REGISTER_VERIFY_CODE);
            obj.put(ParamsKeys.IMAGECODE,edImageCode.getText().toString().trim());
            obj.put(ParamsKeys.SMSCODE,edSmsCode.getText().toString().trim());
            obj.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, phone));
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
                    Intent intent = new Intent(RegisterOne.this, SetLoginPasswordActivity.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("smsCode", smsCode);
                    intent.putExtra("where", "register");
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
}
