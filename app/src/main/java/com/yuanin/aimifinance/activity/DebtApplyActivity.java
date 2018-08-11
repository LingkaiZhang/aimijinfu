package com.yuanin.aimifinance.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class DebtApplyActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_apply);
        x.view().inject(this);

        initTopBarWithRightText("债权转让", toptitleView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, "转让说明");
    }
}
