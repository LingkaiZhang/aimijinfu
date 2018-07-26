package com.yuanin.aimifinance.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yuanin.aimifinance.utils.AppManager;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.StaticMembers;
import com.yuanin.aimifinance.view.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

import de.greenrobot.event.EventBus;

public class LoginOneActivity extends BaseActivity {

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
    @ViewInject(R.id.tv_forget_password)
    private TextView tvForgetPassword;
    @ViewInject(R.id.tv_phone)
    private TextView tvPhone;
    @ViewInject(R.id.ll_password)
    private LinearLayout ll_password;

    private Context context;
    private boolean isShowPassword = false;
    private String phone;
    private boolean isClear = false;
    private GeneralDialog generalDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_one);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.login), toptitleView, true);
        context = getApplicationContext();
        phone = getIntent().getStringExtra("phone");
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        tvPhone.setText(AppUtils.getProtectedMobile(phone));
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
       /* //输入框获取焦点监听
        etLoginPassword.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    // 获得焦点
                    ll_password.setBackground(getResources().getDrawable(R.drawable.login_edittext_bg_red));

                } else {

                    // 失去焦点
                    ll_password.setBackground(getResources().getDrawable(R.drawable.login_edittext_bg_gray));
                }

            }

        });*/
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
                //判断密码长度
                if (etLoginPassword.getText().toString().trim().length()< 6) {
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
    }

    @Event(value = { R.id.btnConfirm , R.id.tv_forget_password})
    private void onViewClicked(View v){
        switch (v.getId()) {
            case R.id.btnConfirm:
                if (etLoginPassword.getText().toString().trim().length() < 6 || etLoginPassword.getText().toString().trim().length() > 20) {
                    AppUtils.showToast(this, getResources().getString(R.string.login_input_right_pwd));
                    return;
                }
                JSONObject obj = AppUtils.getPublicJsonObject(false);
                try {
                    obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_USER);
                    obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_LOGIN);
                    obj.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, phone));
                    obj.put(ParamsKeys.PASSWORD, AppUtils.rsaEncode(this, etLoginPassword.getText().toString().trim()));
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
                                Intent intent = new Intent(context, GesturePasswordActivity.class);
                                intent.putExtra(ParamsKeys.GESTURE_FLAG, ParamsKeys.GESTURE_FLAG_EDIT);
                                startActivity(intent);
                            }
                            LoginOneActivity.this.finish();
                            AppManager.getAppManager().finishActivity(LoginRegisterActivity.class);
                        } else {
                            isClear = true;
                            AppUtils.openKeyboard(etLoginPassword);
                        }
                        AppUtils.showToast(context, entity.getRemark());
                    }

                    @Override
                    public void onFailure() {
                        AppUtils.showConectFail(context);
                    }
                });
                break;
            case R.id.tv_forget_password:

                generalDialog = new GeneralDialog(this, true, "提示", "您的手机号能否接收短信？", "不能", "能", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Acp.getInstance(LoginOneActivity.this).request(new AcpOptions.Builder()
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
                                        AppUtils.showToast(LoginOneActivity.this, permissions.toString() + "权限拒绝");
                                    }
                                });
                        generalDialog.dismiss();
                    }

                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginOneActivity.this, ForgetPasswordActivity.class);
                        intent.putExtra("phone",phone);
                        startActivity(intent);
                        generalDialog.dismiss();
                    }
                });
                break;
        }
    }

}
