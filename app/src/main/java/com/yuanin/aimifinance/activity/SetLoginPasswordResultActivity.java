package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.utils.AppManager;

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
        initTopBar("设置登录密码", toptitleView, true);
        context = getApplicationContext();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//当返回按键被按下
            startActivity(new Intent(SetLoginPasswordResultActivity.this,LoginRegisterActivity.class));
            AppManager.getAppManager().finishActivity(SetLoginPasswordResultActivity.class);
        }
        return false;
    }



}
