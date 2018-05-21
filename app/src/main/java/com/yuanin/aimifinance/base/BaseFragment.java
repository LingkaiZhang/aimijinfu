package com.yuanin.aimifinance.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.entity.TopBarInfo;
import com.yuanin.aimifinance.utils.TopBarUtil;


public abstract class BaseFragment extends Fragment {

    // Fragment当前状态是否可见
    protected boolean isVisible;
    private Animation animation;
    private TextView titleTextView;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = getUserVisibleHint();
    }

    /**
     * 给TopBar赋值
     *
     * @param name    ：TopBar中间的名称
     * @param view    ：TopBar的View
     * @param hasBack ：是否带返回功能
     */
    protected void initTopBar(String name, View view, boolean hasBack) {
        TopBarInfo topBarInfo = new TopBarInfo();
        topBarInfo.setTxtStr(name);
        titleTextView = (TextView) view.findViewById(R.id.tv_title);
        if (hasBack) {
            topBarInfo.setLeftBtnListener(new TopBarUtil().new AlwaysUseListener(getActivity()));
        }
        TopBarUtil.setTopBarInfo(view, topBarInfo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (animation == null) {
            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.top_title_enter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (titleTextView != null) {
            titleTextView.startAnimation(animation);
        }
    }
}
