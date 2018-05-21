package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.service.TimerCount;
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

public class ModifyBindingPhoneActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.etName)
    private EditText etName;
    @ViewInject(R.id.etIdCard)
    private EditText etIdCard;
    @ViewInject(R.id.etPhone)
    private EditText etPhone;
    @ViewInject(R.id.tvGetCode)
    private TextView tvGetCode;
    @ViewInject(R.id.etCode)
    private EditText etCode;

    private TimerCount time;
    public static long timeLimit;
    private Context context = ModifyBindingPhoneActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_binding_phone);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.ModifyBindingPhone), toptitleView, true);
    }

    @Event(value = {R.id.btnConfirm, R.id.tvGetCode,})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //获取验证码
            case R.id.tvGetCode:
                if (etPhone.getText().toString().trim().length() != 11) {
                    AppUtils.showToast(this, getResources().getString(R.string.login_input_right_phone));
                    return;
                }
                JSONObject obj2 = AppUtils.getPublicJsonObject(false);
                try {
                    obj2.put(ParamsKeys.MODULE, ParamsValues.MODULE_SMS);
                    obj2.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_SEND);
                    obj2.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, etPhone.getText().toString().trim()));
                    obj2.put(ParamsKeys.TYPE, ParamsValues.SEND_CHANGE_MOBILE);
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
                            time = new TimerCount(StaticMembers.TIME_TOTAL, StaticMembers.TIME_PER, 3, tvGetCode,tvGetCode);
                            time.start();
                        }
                        AppUtils.showToast(context, entity.getRemark());
                    }

                    @Override
                    public void onFailure() {
                        AppUtils.showConectFail(context);
                    }
                });
                break;
            //确定
            case R.id.btnConfirm:
                String name = etName.getText().toString().trim();
                String id = etIdCard.getText().toString().trim();
                String newPhone = etPhone.getText().toString().trim();
                if (name.length() == 0) {
                    AppUtils.showToast(this, getResources().getString(R.string.certification_input_name));
                    return;
                }
                if (id.length() != 18 && id.length() != 15) {
                    AppUtils.showToast(this, getResources().getString(R.string.certification_input_id_card));
                    return;
                }
                if (newPhone.length() != 11) {
                    AppUtils.showToast(this, getResources().getString(R.string.login_input_right_phone));
                    return;
                }
                if (etCode.getText().toString().trim().length() != 6) {
                    AppUtils.showToast(this, getResources().getString(R.string.register_input_right_phone));
                    return;
                }
                JSONObject obj = AppUtils.getPublicJsonObject(true);
                try {
                    obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_SAFE);
                    obj.put(ParamsKeys.MOTHED, ParamsValues.CHANGE_REGISTER_MOBILE);
                    obj.put(ParamsKeys.REAL_NAME, AppUtils.rsaEncode(this, name));
                    obj.put(ParamsKeys.ID_NUMBER, AppUtils.rsaEncode(this, id));
                    obj.put(ParamsKeys.NEW_MOBILE, AppUtils.rsaEncode(this, newPhone));
                    obj.put(ParamsKeys.VERIFYCODE, etCode.getText().toString().trim());
                    String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                    obj.put(ParamsKeys.TOKEN, token);
                    obj.remove(ParamsKeys.KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Type mType = new TypeToken<ReturnResultEntity<?>>() {
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
                                ReturnResultEntity<?> entity = (ReturnResultEntity<?>) object;
                                if (entity.isSuccess(context)) {
                                    AppUtils.save2SharedPreferences(context, ParamsKeys.LOGIN_FILE, ParamsKeys.LOGIN_KEY_MOBILE, etPhone.getText().toString().trim());
                                    StaticMembers.MOBILE = etPhone.getText().toString().trim();
                                    StaticMembers.RSA_MOBILE = AppUtils.rsaEncode(context, etPhone.getText().toString().trim());
                                    ModifyBindingPhoneActivity.this.finish();
                                }
                                AppUtils.showToast(context, entity.getRemark());
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

    @Override
    protected void onResume() {
        super.onResume();
        if (timeLimit != 0) {
            if (time != null) {
                time.cancel();
                time = null;
            }
            time = new TimerCount(timeLimit, StaticMembers.TIME_PER, 3, tvGetCode,tvGetCode);
            time.start();
        }
    }
}
