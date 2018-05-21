package com.yuanin.aimifinance.service;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.yuanin.aimifinance.inter.INotifyCallBack;
import com.yuanin.aimifinance.utils.AppUtils;

/**
 * @author huangxin
 * @date 2015/11/11
 * @desc
 */
public class ProductTimerCount extends CountDownTimer {
    private TextView textView;
    private INotifyCallBack iNotifyCallBack;

    public ProductTimerCount(long millisInFuture, long countDownInterval, TextView textView, INotifyCallBack iNotifyCallBack) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.iNotifyCallBack = iNotifyCallBack;
    }

    @Override
    public void onFinish() {// 计时完毕
        iNotifyCallBack.onNotify();
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程
        textView.setText(AppUtils.formatToDay(millisUntilFinished));
    }
}
