package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.entity.LoginEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.entity.VerifyEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.service.TimerCount;
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

import static com.yuanin.aimifinance.R.id.tvProtocol;

/**
 * 设置密码
 */
public class SetPasswordActivity extends BaseActivity implements TextWatcher {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.etVerifyCode)
    private EditText etVerifyCode;
    @ViewInject(R.id.etPwd)
    private EditText etPwd;
    @ViewInject(R.id.tvCode)
    private TextView tvCode;
    @ViewInject(R.id.tvTip)
    private TextView tvTip;
    @ViewInject(R.id.tvPhone)
    private TextView tvPhone;
    @ViewInject(R.id.btnConfirm)
    private Button btnConfirm;


    private TimerCount time;
    public static long timeLimit;
    private Context context = SetPasswordActivity.this;
    private VerifyEntity verifyEntity;
    private GeneralDialog generalDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        x.view().inject(this);
        initTopBar2(getResources().getString(R.string.set_login_pwd), toptitleView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTipDialog();
            }
        });
        verifyEntity = (VerifyEntity) getIntent().getSerializableExtra("verifyEntity");
        tvPhone.setText(verifyEntity.phone);
        etVerifyCode.addTextChangedListener(this);
        etPwd.addTextChangedListener(this);

        etVerifyCode.requestFocus();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showTipDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showTipDialog() {
        generalDialog = new GeneralDialog(context, false, "提示", "验证码信息可能略有延迟，确定返回并重新开始？", "取消", "返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalDialog.dismiss();
                SetPasswordActivity.this.finish();
            }
        });
    }

    @Event(value = {R.id.btnConfirm, R.id.tvCode, tvProtocol})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //提交
            case R.id.btnConfirm:
                String newPwd = etPwd.getText().toString().trim();
                String code = etVerifyCode.getText().toString().trim();
                if (code.length() != 6) {
                    AppUtils.showToast(this, getResources().getString(R.string.register_input_right_phone));
                    return;
                }
                if (newPwd.length() < 6 || newPwd.length() > 16) {
                    AppUtils.showToast(this, getResources().getString(R.string.login_input_right_pwd));
                    return;
                }
                JSONObject obj = AppUtils.getPublicJsonObject(false);
                if (verifyEntity.where == 1) {
                    try {
                        obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_USER);
                        obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_FIND_PWD);
                        obj.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, verifyEntity.phone));
                        obj.put(ParamsKeys.PASSWORD, AppUtils.rsaEncode(this, newPwd));
                        obj.put(ParamsKeys.VERIFYCODE, code);
                        String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                        obj.put(ParamsKeys.TOKEN, token);
                        obj.remove(ParamsKeys.KEY);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (verifyEntity.where == 2) {
                    try {
                        obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_USER);
                        obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_REGISTER);
                        obj.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, verifyEntity.phone));
                        obj.put(ParamsKeys.PASSWORD, AppUtils.rsaEncode(this, newPwd));
                        obj.put(ParamsKeys.VERIFYCODE, code);
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
                            if (verifyEntity.where == 2) {
                                intent.putExtra(ParamsKeys.GESTURE_FLAG, ParamsKeys.GESTURE_FLAG_FIRST_EDIT);
                            }
                            startActivity(intent);
                            SetPasswordActivity.this.finish();
                        }
                        AppUtils.showToast(context, entity.getRemark());
                    }

                    @Override
                    public void onFailure() {
                        AppUtils.showConectFail(context);
                    }
                });
                break;
            //获取验证码
            case R.id.tvCode:
                JSONObject obj2 = AppUtils.getPublicJsonObject(false);
                try {
                    obj2.put(ParamsKeys.MODULE, ParamsValues.MODULE_SMS);
                    obj2.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_SEND);
                    obj2.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, verifyEntity.phone));
                    if (verifyEntity.where == 1) {
                        obj2.put(ParamsKeys.TYPE, ParamsValues.SEND_FIND_PASSWORD);
                    } else if (verifyEntity.where == 2) {
                        obj2.put(ParamsKeys.TYPE, ParamsValues.SEND_REGISTER);
                    }
                    String token = AppUtils.getMd5Value(AppUtils.getToken(obj2));
                    obj2.put(ParamsKeys.TOKEN, token);
                    obj2.remove(ParamsKeys.KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Type mType2 = new TypeToken<ReturnResultEntity<?>>() {
                }.getType();
                NetUtils.request(this, obj2, mType2, new IHttpRequestCallBack() {
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
                            if (time != null) {
                                time.cancel();
                                time = null;
                                time = new TimerCount(StaticMembers.TIME_TOTAL, StaticMembers.TIME_PER, 1, tvCode, tvTip);
                                time.start();
                            }
                        }
                        AppUtils.showToast(context, entity.getRemark());
                    }

                    @Override
                    public void onFailure() {
                        AppUtils.showConectFail(context);
                    }
                });
                break;
            case tvProtocol:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(ParamsKeys.TYPE, ParamsValues.REGISTER_PROTOCOL);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (time == null) {
            time = new TimerCount(StaticMembers.TIME_TOTAL, StaticMembers.TIME_PER, 1, tvCode, tvTip);
            time.start();
        } else {
            if (timeLimit != 0) {
                time.cancel();
                time = null;
                time = new TimerCount(timeLimit, StaticMembers.TIME_PER, 1, tvCode, tvTip);
                time.start();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etVerifyCode.getText().toString().trim().length() != 6 || etPwd.getText().toString().trim().length() < 6 || etPwd.getText().toString().trim().length() > 16) {
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
}
