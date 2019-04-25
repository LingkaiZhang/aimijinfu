package com.yuanin.aimifinance.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseFragment;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class SmartChoseExplainFragment extends BaseFragment {

    @ViewInject(R.id.tvServiceIntroduce)
    private TextView tvServiceIntroduce;
    @ViewInject(R.id.tvServiceIntroduceBody)
    private TextView tvServiceIntroduceBody;
    @ViewInject(R.id.tvRiskReminder)
    private TextView tvRiskReminder;
    @ViewInject(R.id.tvRiskReminderBody)
    private TextView tvRiskReminderBody;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smart_chose_explain, container, false);
        x.view().inject(this, view);

        initView();

        return view;
    }

    private void initView() {
        Typeface PingFangSC_Medium= Typeface.createFromAsset(getActivity().getAssets(),"苹方黑体-中粗-简.ttf");
        Typeface PingFangSC_Regular= Typeface.createFromAsset(getActivity().getAssets(),"苹方黑体-准-简.ttf");
        tvServiceIntroduce.setTypeface(PingFangSC_Medium);
        tvRiskReminder.setTypeface(PingFangSC_Medium);
        tvServiceIntroduceBody.setTypeface(PingFangSC_Regular);
        tvRiskReminderBody.setTypeface(PingFangSC_Regular);

    }
}
