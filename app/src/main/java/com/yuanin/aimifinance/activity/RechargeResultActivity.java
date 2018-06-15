package com.yuanin.aimifinance.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class RechargeResultActivity extends BaseActivity {

    @ViewInject(R.id.includeTop)
    private View toptitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_result);
        x.view().inject(this);
        initTopBar("充值成功", toptitleView, true);
        initView();
    }

    private void initView() {

    }
}
