package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.dialog.GeneralNoTitleDialog;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.StaticMembers;
import com.yuanin.aimifinance.view.GestureContentView;
import com.yuanin.aimifinance.view.GestureDrawline;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import de.greenrobot.event.EventBus;

/**
 * 手势密码设置界面
 */
public class GesturePasswordActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.includeTop1)
    private View includeTop;
    @ViewInject(R.id.text_tip)
    private TextView mTextTip;
    @ViewInject(R.id.gesture_container)
    private FrameLayout mGestureContainer;
    @ViewInject(R.id.tvReset)
    private TextView tvReset;
    @ViewInject(R.id.tvShu)
    private TextView tvShu;
    @ViewInject(R.id.tvUserPhone)
    private TextView tvUserPhone;

    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    private GestureContentView mGestureContentView;
    private String flag = "";
    private int where;

    private Context context = GesturePasswordActivity.this;
    private GeneralNoTitleDialog generalDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_password);
        x.view().inject(this);
        initTopBar("设置手势密码", includeTop, true);
        //AppUtils.setCurrentDateToTopTitle(toptitleView);
        flag = getIntent().getStringExtra(ParamsKeys.GESTURE_FLAG);
        if (flag == null) {
            flag = "thishere";
        }
        where = getIntent().getIntExtra("where", 0);
        setUpViews();

        if(flag.equals(ParamsKeys.GESTURE_FLAG_EDIT )|| flag.equals(ParamsKeys.GESTURE_FLAG_FIRST_EDIT)){
            generalDialog = new GeneralNoTitleDialog(context, false, "设置手势密码可以使你的账户更加安全",
                    "设置", "立即离开", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    generalDialog.dismiss();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag.equals(ParamsKeys.GESTURE_FLAG_FIRST_EDIT)) {
                        startActivity(new Intent(GesturePasswordActivity.this, OpenAccountActivity.class));
                    }
                    GesturePasswordActivity.this.finish();
                }
            });
        }

    }

    @Event(value = {R.id.tvReset, R.id.tvCancel})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //重新绘制
            case R.id.tvReset:
                mIsFirstInput = true;
                mTextTip.setText(getString(R.string.set_gesture_pattern));
                mTextTip.setTextColor(getResources().getColor(R.color.text_gray));
                tvReset.setVisibility(View.GONE);
                tvShu.setVisibility(View.GONE);
                break;

            //取消
            case R.id.tvCancel:
                if (flag.equals(ParamsKeys.GESTURE_FLAG_FIRST_EDIT)) {
                    startActivity(new Intent(GesturePasswordActivity.this, OpenAccountActivity.class));
                }
                GesturePasswordActivity.this.finish();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (flag.equals(ParamsKeys.GESTURE_FLAG_FIRST_EDIT)) {
            startActivity(new Intent(GesturePasswordActivity.this, OpenAccountActivity.class));
        }
        GesturePasswordActivity.this.finish();
    }

    private void setUpViews() {
        String mobile = AppUtils.getFromSharedPreferences(context, ParamsKeys.LOGIN_FILE, ParamsKeys.LOGIN_KEY_MOBILE);
        tvUserPhone.setText(AppUtils.getProtectedMobile(mobile));
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, false, "",
                new GestureDrawline.GestureCallBack() {
                    @Override
                    public void onGestureCodeInput(String inputCode) {
                        if (!isInputPassValidate(inputCode)) {
                            mTextTip.setText(getResources().getString(R.string.at_least_four_point));
                            mTextTip.setTextColor(getResources().getColor(R.color.theme_color));
                            mGestureContentView.clearDrawlineState(0L);
                            AppUtils.Vibrate(GesturePasswordActivity.this, 250);
                            return;
                        }
                        if (mIsFirstInput) {
                            mTextTip.setText(getResources().getString(R.string.reset_gesture_code));
                            mTextTip.setTextColor(getResources().getColor(R.color.text_gray));
                            mFirstPassword = inputCode;
                            mGestureContentView.clearDrawlineState(0L);
                            tvReset.setVisibility(View.VISIBLE);
                            tvShu.setVisibility(View.VISIBLE);
                        } else {
                            if (inputCode.equals(mFirstPassword)) {
                                AppUtils.save2SharedPreferences(GesturePasswordActivity.this, ParamsKeys.GESTURE_SHARE_FILE_NAME,
                                        ParamsKeys.GESTURE_SHARE_DATA_NAME, inputCode);
                                Toast.makeText(GesturePasswordActivity.this,
                                        getResources().getString(R.string.setup_success), Toast.LENGTH_SHORT).show();
                                mGestureContentView.clearDrawlineState(0L);
                                StaticMembers.IS_NEED_GUSTURE_PWD = true;
                                if (where != 1) {
                                    if (flag.equals(ParamsKeys.GESTURE_FLAG_FIRST_EDIT)) {
                                        startActivity(new Intent(GesturePasswordActivity.this, OpenAccountActivity.class));
                                    }
                                    //通知homepage跳到个人中心
                                    EventMessage eventMessage = new EventMessage();
                                    eventMessage.setType(EventMessage.HOMEPAGE_JUMP_TAB);
                                    eventMessage.setObject(2);
                                    EventBus.getDefault().post(eventMessage);

                                }
                                GesturePasswordActivity.this.finish();
                            } else {
                                mTextTip.setText(getResources().getString(R.string.not_same));
                                mTextTip.setTextColor(getResources().getColor(R.color.theme_color));
                                // 左右移动动画
                                Animation shakeAnimation = AnimationUtils
                                        .loadAnimation(
                                                GesturePasswordActivity.this,
                                                R.anim.shake);
                                mTextTip.startAnimation(shakeAnimation);
                                // 保持绘制的线，1.5秒后清除
                                mGestureContentView.clearDrawlineState(1300L);
                                AppUtils.Vibrate(GesturePasswordActivity.this, 250);
                            }
                        }
                        mIsFirstInput = false;
                    }

                    @Override
                    public void checkedSuccess() {

                    }

                    @Override
                    public void checkedFail() {

                    }
                });
        mGestureContentView.setParentView(mGestureContainer);
    }


//    private void updateCodeList(String inputCode) {
//        // 更新选择的图案
//        mLockIndicator.setPath(inputCode);
//    }

    private boolean isInputPassValidate(String inputPassword) {
        return !(TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4);
    }
}
