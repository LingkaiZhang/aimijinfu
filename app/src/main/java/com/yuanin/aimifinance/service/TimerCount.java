package com.yuanin.aimifinance.service;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.yuanin.aimifinance.activity.ModifyBindingPhoneActivity;
import com.yuanin.aimifinance.activity.SetPasswordActivity;

/**
 * @author huangxin
 * @date 2015/11/11
 * @desc
 */
public class TimerCount extends CountDownTimer {
    private int type;
    private TextView tvGetCode, tvTip;
    private boolean isFirst = true;

    public TimerCount(long millisInFuture, long countDownInterval, int type, TextView tvGetCode, TextView tvTip) {
        super(millisInFuture, countDownInterval);
        this.type = type;
        this.tvGetCode = tvGetCode;
        this.tvTip = tvTip;
    }

    @Override
    public void onFinish() {// 计时完毕
        switch (type) {
            case 1://找回密码
                SetPasswordActivity.timeLimit = 0;
                break;
            case 3://更换手机号
                ModifyBindingPhoneActivity.timeLimit = 0;
                break;
        }
        tvTip.setVisibility(View.GONE);
        tvGetCode.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程
        if (isFirst) {
            isFirst = false;
            tvTip.setVisibility(View.VISIBLE);
            tvGetCode.setVisibility(View.GONE);
        }

        if (millisUntilFinished < 2005) {
            this.onFinish();
        } else {
            tvTip.setText("(" + ((millisUntilFinished / 1000) - 1) + "s)重新发送");
        }
        switch (type) {
            case 1://找回密码
                SetPasswordActivity.timeLimit = millisUntilFinished;
                break;
            case 3://注册
                ModifyBindingPhoneActivity.timeLimit = millisUntilFinished;
                break;
        }

    }
}
