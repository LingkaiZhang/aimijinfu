package com.yuanin.aimifinance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.StaticMembers;
import com.yuanin.aimifinance.view.GestureContentView;
import com.yuanin.aimifinance.view.GestureDrawline;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 手势绘制/校验界面
 */
public class GesturePasswordVerifyActivity extends BaseActivity {

    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvTip)
    private TextView tvTip;
    @ViewInject(R.id.gesture_container)
    private FrameLayout mGestureContainer;
    @ViewInject(R.id.tvChangeFinger)
    private TextView tvChangeFinger;
    @ViewInject(R.id.tvChangeUser)
    private TextView tvChangeUser;
    @ViewInject(R.id.tvCancel)
    private TextView tvCancel;
    @ViewInject(R.id.tvShu)
    private TextView tvShu;

    private GestureContentView mGestureContentView;
    private int failCount = 0;
    private int where;
    private String url;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_verify);
        x.view().inject(this);
        AppUtils.setCurrentDateToTopTitle(toptitleView);
        tvTip.setText(StaticMembers.GESTURE_TIP);
        where = getIntent().getIntExtra("where", 0);
        url = getIntent().getStringExtra("url");
        if (where == 1) {
            tvCancel.setVisibility(View.VISIBLE);
            tvChangeUser.setVisibility(View.GONE);
            tvChangeFinger.setVisibility(View.GONE);
            tvShu.setVisibility(View.GONE);
        } else {
            tvCancel.setVisibility(View.GONE);
            tvChangeUser.setVisibility(View.VISIBLE);
            if (StaticMembers.IS_NEED_FINGER_PWD) {
                tvChangeFinger.setVisibility(View.VISIBLE);
                tvShu.setVisibility(View.VISIBLE);
            } else {
                tvChangeFinger.setVisibility(View.GONE);
                tvShu.setVisibility(View.GONE);
            }
        }
        setUpViews();
    }

    @Event(value = {R.id.tvChangeFinger, R.id.tvChangeUser, R.id.tvCancel})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //其他账号
            case R.id.tvChangeUser:
                startActivity(new Intent(GesturePasswordVerifyActivity.this, HomePageActivity.class));
                AppUtils.exitLogin(this);
                startActivity(new Intent(GesturePasswordVerifyActivity.this, LoginRegisterActivity.class));
                this.finish();
                break;
            //切换指纹密码
            case R.id.tvChangeFinger:
                Intent intent = new Intent(GesturePasswordVerifyActivity.this, FingerPasswordActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                this.finish();
                break;
            case R.id.tvCancel:
                this.finish();
                break;
        }
    }

    private void setUpViews() {

        String pwd = AppUtils.getFromSharedPreferences(GesturePasswordVerifyActivity.this,
                ParamsKeys.GESTURE_SHARE_FILE_NAME, ParamsKeys.GESTURE_SHARE_DATA_NAME);
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, true, pwd,
                new GestureDrawline.GestureCallBack() {

                    @Override
                    public void onGestureCodeInput(String inputCode) {

                    }

                    @Override
                    public void checkedSuccess() {
                        mGestureContentView.clearDrawlineState(0L);
                        Toast.makeText(GesturePasswordVerifyActivity.this, getResources().getString(R.string.pwd_right), Toast.LENGTH_SHORT)
                                .show();
                        if (where == 1) {
                            AppUtils.deleteSharedPreferences(GesturePasswordVerifyActivity.this, ParamsKeys.GESTURE_SHARE_FILE_NAME);
                            AppUtils.deleteSharedPreferences(GesturePasswordVerifyActivity.this, ParamsKeys.FINGER_PWD_SHARE_FILE_NAME);
                            AppUtils.initBooleanData(GesturePasswordVerifyActivity.this);
                        } else {
                            startActivity(new Intent(GesturePasswordVerifyActivity.this, HomePageActivity.class));
                            if (url != null && url.length() > 0) {
                                Intent intent = new Intent(GesturePasswordVerifyActivity.this, AdvertisementWebActivity.class);
                                intent.putExtra("url", url);
                                startActivity(intent);
                            }
                        }
                        GesturePasswordVerifyActivity.this.finish();
                        overridePendingTransition(R.anim.fade_in,
                                R.anim.fade_out);
                    }

                    @Override
                    public void checkedFail() {
                        failCount += 1;
                        if (failCount == 5) {
                            AppUtils.showToast(GesturePasswordVerifyActivity.this, getResources().getString(R.string.wrong_five));
                            startActivity(new Intent(GesturePasswordVerifyActivity.this, HomePageActivity.class));
                            AppUtils.exitLogin(GesturePasswordVerifyActivity.this);
                            GesturePasswordVerifyActivity.this.finish();
                        } else {
                            mGestureContentView.clearDrawlineState(1300L);
                            tvTip.setText(getResources().getString(R.string.pwd_wrong) + "，您还可以输入" + (5 - failCount) + "次");
                            tvTip.setTextColor(getResources().getColor(R.color.theme_color));
                            // 左右移动动画
                            Animation shakeAnimation = AnimationUtils
                                    .loadAnimation(GesturePasswordVerifyActivity.this,
                                            R.anim.shake);
                            tvTip.startAnimation(shakeAnimation);
                            AppUtils.Vibrate(GesturePasswordVerifyActivity.this, 250);
                        }
                    }
                });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
    }

}
