package com.yuanin.aimifinance.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.dialog.GeneralSingleButtonDialog;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.StaticMembers;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@TargetApi(Build.VERSION_CODES.M)
public class FingerPasswordActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvTip)
    private TextView tvTip;
    @ViewInject(R.id.tvGestureLogin)
    private TextView tvGestureLogin;
    @ViewInject(R.id.tvChangeUser)
    private TextView tvChangeUser;
    @ViewInject(R.id.tvCancel)
    private TextView tvCancel;
    @ViewInject(R.id.tvShu)
    private TextView tvShu;

    private GeneralSingleButtonDialog dialog;
    private FingerprintManager manager;
    private CancellationSignal mCancellationSignal;

    private int where;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_password);
        x.view().inject(this);
        where = getIntent().getIntExtra("where", 0);
        url = getIntent().getStringExtra("url");
        manager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);
        AppUtils.setCurrentDateToTopTitle(toptitleView);
        tvTip.setText(StaticMembers.GESTURE_TIP);
        if (where == 1) {
            tvCancel.setVisibility(View.VISIBLE);
            tvChangeUser.setVisibility(View.GONE);
            tvGestureLogin.setVisibility(View.GONE);
            tvShu.setVisibility(View.GONE);
        } else {
            tvCancel.setVisibility(View.GONE);
            tvChangeUser.setVisibility(View.VISIBLE);
            if (StaticMembers.IS_NEED_GUSTURE_PWD) {
                tvGestureLogin.setVisibility(View.VISIBLE);
                tvShu.setVisibility(View.VISIBLE);
            } else {
                tvGestureLogin.setVisibility(View.GONE);
                tvShu.setVisibility(View.GONE);
            }
        }
        if (dialog == null) {
            dialog = new GeneralSingleButtonDialog(this, false, "提示", "请验证您的指纹密码", "取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.onDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mCancellationSignal.cancel();
                }
            });
        } else {
            dialog.show();
        }
        mCancellationSignal = new CancellationSignal();
        manager.authenticate(null, mCancellationSignal, 0, mSelfCancelled, null);
    }


    //回调方法
    FingerprintManager.AuthenticationCallback mSelfCancelled = new FingerprintManager.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            //但多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证
            Toast.makeText(FingerPasswordActivity.this, errString, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            Toast.makeText(FingerPasswordActivity.this, helpString, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            Toast.makeText(FingerPasswordActivity.this, "指纹识别成功", Toast.LENGTH_SHORT).show();
            if (where == 1) {
                if (StaticMembers.IS_NEED_FINGER_PWD) {
                    AppUtils.save2SharedPreferences(FingerPasswordActivity.this, ParamsKeys.FINGER_PWD_SHARE_FILE_NAME,
                            ParamsKeys.FINGER_PWD_SHARE_DATA_NAME, "0");
                    StaticMembers.IS_NEED_FINGER_PWD = false;
                } else {
                    AppUtils.save2SharedPreferences(FingerPasswordActivity.this, ParamsKeys.FINGER_PWD_SHARE_FILE_NAME,
                            ParamsKeys.FINGER_PWD_SHARE_DATA_NAME, "1");
                    StaticMembers.IS_NEED_FINGER_PWD = true;
                }
            } else {
                startActivity(new Intent(FingerPasswordActivity.this, HomePageActivity.class));
                if (url != null && url.length() > 0) {
                    Intent intent = new Intent(FingerPasswordActivity.this, AdvertisementWebActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            }
            FingerPasswordActivity.this.finish();
            overridePendingTransition(R.anim.fade_in,
                    R.anim.fade_out);
        }

        @Override
        public void onAuthenticationFailed() {
            Toast.makeText(FingerPasswordActivity.this, "指纹识别失败", Toast.LENGTH_SHORT).show();
        }
    };


    @Event(value = {R.id.btnVerify, R.id.tvVerify, R.id.tvGestureLogin, R.id.tvChangeUser, R.id.tvCancel})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btnVerify:
            case R.id.tvVerify:
                if (dialog == null) {
                    dialog = new GeneralSingleButtonDialog(this, false, "提示", "请验证您的指纹密码", "取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.onDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mCancellationSignal.cancel();
                        }
                    });
                } else {
                    dialog.show();
                }
                mCancellationSignal = new CancellationSignal();
                manager.authenticate(null, mCancellationSignal, 0, mSelfCancelled, null);
                break;
            case R.id.tvGestureLogin:
                Intent intent = new Intent(FingerPasswordActivity.this, GesturePasswordVerifyActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                this.finish();
                break;
            case R.id.tvChangeUser:
                startActivity(new Intent(FingerPasswordActivity.this, HomePageActivity.class));
                AppUtils.exitLogin(this);
                startActivity(new Intent(FingerPasswordActivity.this, LoginRegisterActivity.class));
                this.finish();
                break;
            case R.id.tvCancel:
                this.finish();
                break;
        }
    }
}
