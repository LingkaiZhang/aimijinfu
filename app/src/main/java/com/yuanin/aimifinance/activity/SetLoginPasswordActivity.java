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
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.entity.LoginEntity;
import com.yuanin.aimifinance.entity.RegisterNewEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppManager;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.StaticMembers;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;

import de.greenrobot.event.EventBus;

public class SetLoginPasswordActivity extends BaseActivity {

    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.et_login_password)
    private EditText etLoginPassword;
    @ViewInject(R.id.im_clear_mark)
    private ImageView imClearMark;
    @ViewInject(R.id.im_password_mark)
    private ImageView imPasswordMark;
    @ViewInject(R.id.btnConfirm)
    private Button btnConfirm;

    private Context context;
    private boolean isShowPassword = false;
    private String phone;
    private String smsCode;
    private String where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_login_password);
        x.view().inject(this);
        initTopBar("设置登录密码", toptitleView, true);
        context = getApplicationContext();
        initView();
        initData();
    }

    private void initData() {
        phone = getIntent().getStringExtra("phone");
        smsCode = getIntent().getStringExtra("smsCode");
        where = getIntent().getStringExtra("where");
    }

    private void initView() {
        btnConfirm.setBackgroundResource(R.mipmap.login_button_enable);
        btnConfirm.setEnabled(false);

        imClearMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etLoginPassword.setText("");
            }
        });
        imPasswordMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowPassword) {
                    imPasswordMark.setImageResource(R.mipmap.login_hide_pwd);
                    etLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etLoginPassword.setSelection(etLoginPassword.getText().length());
                    isShowPassword = false;
                } else {
                    imPasswordMark.setImageResource(R.mipmap.login_show_pwd);
                    etLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    etLoginPassword.setSelection(etLoginPassword.getText().length());
                    isShowPassword = true;
                }
            }
        });
        etLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etLoginPassword.getText().toString().trim().length() > 0) {
                    imClearMark.setVisibility(View.VISIBLE);
                    imPasswordMark.setVisibility(View.VISIBLE);
                }else {
                    imClearMark.setVisibility(View.GONE);
                    imPasswordMark.setVisibility(View.GONE);
                }
                if (etLoginPassword.getText().toString().trim().length() >= 6) {
                    btnConfirm.setBackgroundResource(R.drawable.selector_theme_corner_button);
                    btnConfirm.setEnabled(true);
                } else {
                    btnConfirm.setBackgroundResource(R.mipmap.login_button_enable);
                    btnConfirm.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Event(value = {R.id.btnConfirm})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //点击完成
            case R.id.btnConfirm:
                //老的接口
                //requestRegisterAndForgetPassword();

                //新的接口
                if(where.equals("register") ){
                    questRegisterNew();
                } else if (where.contains("forgetpassword")) {
                    questUpdatePasswordNew();
                }
                break;
        }
    }

    private void questUpdatePasswordNew() {
        String password = etLoginPassword.getText().toString().trim();
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_USER);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_UPDATE_PASSWORD_NEW);
            obj.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, phone));
            obj.put(ParamsKeys.NEW_PASSWORD, AppUtils.rsaEncode(this, password));
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
                    startActivity(new Intent(SetLoginPasswordActivity.this,SetLoginPasswordResultActivity.class));
                    AppManager.getAppManager().finishActivity(LoginOneActivity.class);
                    AppManager.getAppManager().finishActivity(ForgetPasswordActivity.class);
                    AppManager.getAppManager().finishActivity(SetLoginPasswordActivity.class);
                }
                AppUtils.showToast(context, entity.getRemark());
            }

            @Override
            public void onFailure() {
                AppUtils.showConectFail(context);
            }
        });
    }

    private void questRegisterNew() {
        String password = etLoginPassword.getText().toString().trim();
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_USER);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_REGISTER_NEW);
            obj.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, phone));
            obj.put(ParamsKeys.PASSWORD, AppUtils.rsaEncode(this, password));
            obj.put(ParamsKeys.VERIFYCODE, smsCode);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<RegisterNewEntity>>() {
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
                ReturnResultEntity<RegisterNewEntity> entity = (ReturnResultEntity<RegisterNewEntity>) object;
                if (entity.isSuccess(context)) {
                    AppManager.getAppManager().finishActivity(LoginRegisterActivity.class);
                    AppManager.getAppManager().finishActivity(RegisterOne.class);
                    AppManager.getAppManager().finishActivity(SetLoginPasswordActivity.class);
                    AppUtils.save2SharedPreferences(context, ParamsKeys.LOGIN_FILE, ParamsKeys.LOGIN_KEY_MOBILE, entity.getData().get(0).getMobile());
                    AppUtils.save2SharedPreferences(context, ParamsKeys.LOGIN_FILE, ParamsKeys.LOGIN_KEY_USERID, entity.getData().get(0).getUserid());
                    AppUtils.save2SharedPreferences(context, ParamsKeys.LOGIN_FILE, ParamsKeys.LOGIN_KEY_TOKEN, entity.getData().get(0).getLogin_token());
                    StaticMembers.IS_NEED_LOGIN = false;
                    StaticMembers.USER_ID = entity.getData().get(0).getUserid();
                    StaticMembers.MOBILE = entity.getData().get(0).getMobile();
                    StaticMembers.LOGIN_TOKEN = entity.getData().get(0).getLogin_token();
                    StaticMembers.RSA_MOBILE = AppUtils.rsaEncode(context, entity.getData().get(0).getMobile());
                    //刷新个人中心
                    EventMessage eventMessage = new EventMessage();
                    eventMessage.setType(EventMessage.REFRESH_MINE);
                    EventBus.getDefault().post(eventMessage);
                    //刷新首页登录状态
                    EventMessage eventMessage2 = new EventMessage();
                    eventMessage2.setType(EventMessage.UPDATE_INDEX_LOGIN);
                    EventBus.getDefault().post(eventMessage2);
                    Intent intent = new Intent(context, GesturePasswordActivity.class);
                    if (where == "register") {
                        intent.putExtra(ParamsKeys.GESTURE_FLAG, ParamsKeys.GESTURE_FLAG_FIRST_EDIT);
                    }
                    startActivity(intent);
                    SetLoginPasswordActivity.this.finish();
                    AppManager.getAppManager().finishActivity(LoginRegisterActivity.class);
                }
                AppUtils.showToast(context, entity.getRemark());
            }

            @Override
            public void onFailure() {
                AppUtils.showConectFail(context);
            }
        });
    }

    //原来接口
    private void requestRegisterAndForgetPassword() {
        String password = etLoginPassword.getText().toString().trim();
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        if (where == "forgetpassword") {
            try {
                obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_USER);
                obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_FIND_PWD);
                obj.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, phone));
                obj.put(ParamsKeys.PASSWORD, AppUtils.rsaEncode(this, password));
                obj.put(ParamsKeys.VERIFYCODE, smsCode);
                String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                obj.put(ParamsKeys.TOKEN, token);
                obj.remove(ParamsKeys.KEY);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (where == "register") {
            try {
                obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_USER);
                obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_REGISTER);
                obj.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, phone));
                obj.put(ParamsKeys.PASSWORD, AppUtils.rsaEncode(this, password));
                obj.put(ParamsKeys.VERIFYCODE, smsCode);
                String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                obj.put(ParamsKeys.TOKEN, token);
                obj.remove(ParamsKeys.KEY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Type mType = new TypeToken<ReturnResultEntity<LoginEntity>>() {
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
                ReturnResultEntity<LoginEntity> entity = (ReturnResultEntity<LoginEntity>) object;
                if (entity.isSuccess(context)) {
                    AppManager.getAppManager().finishActivity(LoginActivity.class);
                    AppManager.getAppManager().finishActivity(GetVerifyCodeActivity.class);
                    AppUtils.save2SharedPreferences(context, ParamsKeys.LOGIN_FILE, ParamsKeys.LOGIN_KEY_MOBILE, entity.getData().get(0).getMobile());
                    AppUtils.save2SharedPreferences(context, ParamsKeys.LOGIN_FILE, ParamsKeys.LOGIN_KEY_USERID, entity.getData().get(0).getUserid());
                    AppUtils.save2SharedPreferences(context, ParamsKeys.LOGIN_FILE, ParamsKeys.LOGIN_KEY_TOKEN, entity.getData().get(0).getLogin_token());
                    StaticMembers.IS_NEED_LOGIN = false;
                    StaticMembers.USER_ID = entity.getData().get(0).getUserid();
                    StaticMembers.MOBILE = entity.getData().get(0).getMobile();
                    StaticMembers.LOGIN_TOKEN = entity.getData().get(0).getLogin_token();
                    StaticMembers.RSA_MOBILE = AppUtils.rsaEncode(context, entity.getData().get(0).getMobile());
                    //刷新个人中心
                    EventMessage eventMessage = new EventMessage();
                    eventMessage.setType(EventMessage.REFRESH_MINE);
                    EventBus.getDefault().post(eventMessage);
                    //刷新首页登录状态
                    EventMessage eventMessage2 = new EventMessage();
                    eventMessage2.setType(EventMessage.UPDATE_INDEX_LOGIN);
                    EventBus.getDefault().post(eventMessage2);
                    Intent intent = new Intent(context, GesturePasswordEditActivity.class);
                    if (where == "register") {
                        intent.putExtra(ParamsKeys.GESTURE_FLAG, ParamsKeys.GESTURE_FLAG_FIRST_EDIT);
                    }
                    startActivity(intent);
                    SetLoginPasswordActivity.this.finish();
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
