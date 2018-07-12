package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class SetLoginPasswordResultActivity extends BaseActivity {

    @ViewInject(R.id.includeTop)
    private View toptitleView;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_login_password_result);
        x.view().inject(this);
        initTopBar3("设置登录密码", toptitleView, true);
        context = getApplicationContext();
    }


}
