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
import com.yuanin.aimifinance.utils.AppManager;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.view.GestureContentView;
import com.yuanin.aimifinance.view.GestureDrawline;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 手势密码修改界面
 */
public class GesturePasswordModifyActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.text_tip)
    private TextView mTextTip;
    @ViewInject(R.id.gesture_container)
    private FrameLayout mGestureContainer;

    private GestureContentView mGestureContentView;
    private int failCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_gesture_password);
        x.view().inject(this);
        AppUtils.setCurrentDateToTopTitle(toptitleView);
        setUpViews();
    }

    // 点击事件
    @Event(value = {R.id.text_forget_gesture, R.id.tvCancel})
    private void onViewClicked(View view) {
        switch (view.getId()) {
            //忘记手势密码
            case R.id.text_forget_gesture:
                startActivity(new Intent(GesturePasswordModifyActivity.this, HomePageActivity.class));
                AppUtils.exitLogin(this);
                startActivity(new Intent(GesturePasswordModifyActivity.this, LoginActivity.class));
                this.finish();
                break;
            //取消
            case R.id.tvCancel:
                GesturePasswordModifyActivity.this.finish();
                break;
        }
    }

    private void setUpViews() {
        String pwd = AppUtils.getFromSharedPreferences(GesturePasswordModifyActivity.this, ParamsKeys.GESTURE_SHARE_FILE_NAME, ParamsKeys.GESTURE_SHARE_DATA_NAME);
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, true, pwd,
                new GestureDrawline.GestureCallBack() {

                    @Override
                    public void onGestureCodeInput(String inputCode) {

                    }

                    @Override
                    public void checkedSuccess() {
                        mGestureContentView.clearDrawlineState(0L);
                        Toast.makeText(GesturePasswordModifyActivity.this, getResources().getString(R.string.pwd_right), Toast.LENGTH_SHORT)
                                .show();
                        Intent intent = new Intent(GesturePasswordModifyActivity.this, GesturePasswordEditActivity.class);
                        intent.putExtra(ParamsKeys.GESTURE_FLAG, ParamsKeys.GESTURE_FLAG_MODIFY);
                        startActivity(intent);
                        GesturePasswordModifyActivity.this.finish();
                    }

                    @Override
                    public void checkedFail() {
                        failCount += 1;
                        if (failCount == 5) {
                            AppUtils.showToast(GesturePasswordModifyActivity.this, getResources().getString(R.string.wrong_five));
                            AppUtils.exitLogin(GesturePasswordModifyActivity.this);
                            AppManager.getAppManager().finishActivity(PersonalSettingsActivity.class);
                            GesturePasswordModifyActivity.this.finish();
                        } else {
                            mGestureContentView.clearDrawlineState(1300L);
                            mTextTip.setText(getResources().getString(R.string.pwd_wrong) + "，您还可以输入" + (5 - failCount) + "次");
                            mTextTip.setTextColor(getResources().getColor(R.color.theme_color));
                            // 左右移动动画
                            Animation shakeAnimation = AnimationUtils
                                    .loadAnimation(GesturePasswordModifyActivity.this,
                                            R.anim.shake);
                            mTextTip.startAnimation(shakeAnimation);
                            AppUtils.Vibrate(GesturePasswordModifyActivity.this, 250);
                        }
                    }
                });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
    }
}
