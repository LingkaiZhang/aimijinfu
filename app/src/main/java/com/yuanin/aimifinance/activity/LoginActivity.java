package com.yuanin.aimifinance.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.entity.LoginEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.StaticMembers;
import com.yuanin.aimifinance.view.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements TextWatcher {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.etPwd)
    private EditText etPwd;
    @ViewInject(R.id.etPhone)
    private ClearEditText etPhone;
    @ViewInject(R.id.btnLogin)
    private Button btnLogin;
    @ViewInject(R.id.tvForgetPwd)
    private TextView tvForgetPwd;

    private Context context;
    private GeneralDialog generalDialog;
    private boolean isClear = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.view().inject(this);
        initTopBar("", toptitleView, true);
        context = getApplicationContext();
        btnLogin.setEnabled(false);
        etPhone.addTextChangedListener(this);
        etPwd.addTextChangedListener(this);
        String mobile = AppUtils.getFromSharedPreferences(this, ParamsKeys.LOGIN_MBOILE_FILE, ParamsKeys.LOGIN_MBOILE);
        if (mobile.length() > 0) {
            etPhone.setText(mobile);
            Editable etext = etPhone.getText();
            Selection.setSelection(etext, etext.length());
            etPhone.setClearIconVisible(true);
            etPwd.requestFocus();
        } else {
            etPhone.requestFocus();
        }
        tvForgetPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Event(value = {R.id.btnLogin, R.id.tvForgetPwd, R.id.btnRegister})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (etPhone.getText().toString().trim().length() != 11) {
                    AppUtils.showToast(this, getResources().getString(R.string.login_input_right_phone));
                    return;
                }
                if (etPwd.getText().toString().trim().length() < 6 || etPwd.getText().toString().trim().length() > 16) {
                    AppUtils.showToast(this, getResources().getString(R.string.login_input_right_pwd));
                    return;
                }
                JSONObject obj = AppUtils.getPublicJsonObject(false);
                try {
                    obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_USER);
                    obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_LOGIN);
                    obj.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, etPhone.getText().toString().trim()));
                    obj.put(ParamsKeys.PASSWORD, AppUtils.rsaEncode(this, etPwd.getText().toString().trim()));
                    String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                    obj.put(ParamsKeys.TOKEN, token);
                    obj.remove(ParamsKeys.KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
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
                            if (entity.isNotNull()) {
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
                                intent.putExtra(ParamsKeys.GESTURE_FLAG, ParamsKeys.GESTURE_FLAG_EDIT);
                                startActivity(intent);
                            }
                            LoginActivity.this.finish();
                        } else {
                            isClear = true;
                            AppUtils.openKeyboard(etPwd);
                        }
                        AppUtils.showToast(context, entity.getRemark());
                    }

                    @Override
                    public void onFailure() {
                        AppUtils.showConectFail(context);
                    }
                });
                break;
            case R.id.tvForgetPwd:
                generalDialog = new GeneralDialog(this, true, "提示", "您的手机号能否接收短信？", "不能", "能", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Acp.getInstance(LoginActivity.this).request(new AcpOptions.Builder()
                                        .setPermissions(Manifest.permission.CALL_PHONE)
                                        .build(),
                                new AcpListener() {
                                    @Override
                                    public void onGranted() {
                                        //用intent启动拨打电话
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ParamsValues.TEL));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onDenied(List<String> permissions) {
                                        AppUtils.showToast(LoginActivity.this, permissions.toString() + "权限拒绝");
                                    }
                                });
                        generalDialog.dismiss();
                    }

                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, GetVerifyCodeActivity.class);
                        intent.putExtra("where", 1);
                        startActivity(intent);
                        generalDialog.dismiss();
                    }
                });
                break;
            case R.id.btnRegister:
                Intent intent = new Intent(context, GetVerifyCodeActivity.class);
                intent.putExtra("where", 2);
                startActivity(intent);
//                startActivity(new Intent(context, RegisterActivity.class));
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isClear) {
            isClear = false;
            etPwd.setText("");
        }
        if (etPhone.getText().toString().trim().length() != 11 || etPwd.getText().toString().trim().length() < 6 || etPwd.getText().toString().trim().length() > 16) {
            btnLogin.setBackgroundResource(R.mipmap.login_button_enable);
            btnLogin.setEnabled(false);
        } else {
            btnLogin.setBackgroundResource(R.drawable.selector_theme_corner_button);
            btnLogin.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
