package com.yuanin.aimifinance.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.HKRegisterEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
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

public class PasswordManagementActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.toggleBtnPwd)
    private ToggleButton toggleBtnPwd;
    @ViewInject(R.id.rlModifyGesturePwd)
    private RelativeLayout rlModifyGesturePwd;
    @ViewInject(R.id.rlFingerPwd)
    private RelativeLayout rlFingerPwd;
    @ViewInject(R.id.toggleBtnFingerPwd)
    private ToggleButton toggleBtnFingerPwd;
    @ViewInject(R.id.llMain)
    private LinearLayout llMain;

    private Context context = PasswordManagementActivity.this;
    private View popView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_management);
        popView = getLayoutInflater().inflate(R.layout.popupwindow_hk_register, null, false);
        x.view().inject(this);
        initTopBar("密码管理", toptitleView, true);

        toggleBtnPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleBtnPwd.isChecked()) {
                    Intent intent = new Intent(context, GesturePasswordEditActivity.class);
                    intent.putExtra("where", 1);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, GesturePasswordVerifyActivity.class);
                    intent.putExtra("where", 1);
                    startActivity(intent);
                }
            }
        });

        toggleBtnFingerPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FingerPasswordActivity.class);
                intent.putExtra("where", 1);
                startActivity(intent);
            }
        });
    }

    @Event(value = {R.id.rlModifyGesturePwd, R.id.rlLoginPwd, R.id.rlExchangePwd})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //修改交易密码
            case R.id.rlExchangePwd:
                if (StaticMembers.HK_STATUS == 1) {
                    JSONObject obj = AppUtils.getPublicJsonObject(true);
                    try {
                        obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_SAFE);
                        obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_CHANGE_HK_PWD);
                        String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                        obj.put(ParamsKeys.TOKEN, token);
                        obj.remove(ParamsKeys.KEY);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Type mType = new TypeToken<ReturnResultEntity<HKRegisterEntity>>() {
                    }.getType();
                    NetUtils.request(context, obj, mType, new IHttpRequestCallBack() {
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
                                    context.startActivity(intent);
                                }
                            } else {
                                AppUtils.showToast(context, entity.getRemark());
                            }
                        }

                        @Override
                        public void onFailure() {
                            AppUtils.showConectFail(context);
                        }
                    });
                } else {
                    PopupWindow mPop = AppUtils.createHKPop(popView, context);
                    mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                }
                break;
            //修改登录密码
            case R.id.rlLoginPwd:
                startActivity(new Intent(context, ModifyLoginPasswordActivity.class));
                break;
            //修改手势密码
            case R.id.rlModifyGesturePwd:
                Intent intent = new Intent();
                if (StaticMembers.IS_NEED_GUSTURE_PWD) {
                    intent.setClass(context, GesturePasswordModifyActivity.class);
                    intent.putExtra(ParamsKeys.GESTURE_FLAG, ParamsKeys.GESTURE_FLAG_MODIFY);
                } else {
                    intent.setClass(context, GesturePasswordEditActivity.class);
                    intent.putExtra(ParamsKeys.GESTURE_FLAG, ParamsKeys.GESTURE_FLAG_EDIT);
                }
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (StaticMembers.IS_NEED_GUSTURE_PWD) {
            toggleBtnPwd.setChecked(true);
            rlModifyGesturePwd.setVisibility(View.VISIBLE);
        } else {
            toggleBtnPwd.setChecked(false);
            rlModifyGesturePwd.setVisibility(View.GONE);
        }
        if(android.os.Build.VERSION.SDK_INT >= 23 ){
            FingerprintManager manager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);
            KeyguardManager mKeyManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
            //硬件支持，开启屏幕锁，录入指纹
            if (manager.isHardwareDetected()
                    && mKeyManager.isKeyguardSecure()
                    && manager.hasEnrolledFingerprints()) {
                rlFingerPwd.setVisibility(View.VISIBLE);
                if (StaticMembers.IS_NEED_FINGER_PWD) {
                    toggleBtnFingerPwd.setChecked(true);
                } else {
                    toggleBtnFingerPwd.setChecked(false);
                }
            } else {
                rlFingerPwd.setVisibility(View.GONE);
            }
        }else {
            rlFingerPwd.setVisibility(View.GONE);
        }
    }
}
