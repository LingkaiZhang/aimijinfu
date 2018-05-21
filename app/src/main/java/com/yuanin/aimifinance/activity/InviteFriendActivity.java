package com.yuanin.aimifinance.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.ViewPagerFragmentAdapter;
import com.yuanin.aimifinance.base.BaseFragmentActivity;
import com.yuanin.aimifinance.entity.InviteFriendTopEntity;
import com.yuanin.aimifinance.entity.TabIndicatorEntity;
import com.yuanin.aimifinance.fragment.InviteFriendFragment;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.StaticMembers;
import com.yuanin.aimifinance.utils.ViewPagerUtils;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class InviteFriendActivity extends BaseFragmentActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.vpProductDetail)
    private ViewPager mViewPager;
    @ViewInject(R.id.ivTop)
    private SimpleDraweeView ivTop;

    private InviteFriendTopEntity inviteFriendTopEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        x.view().inject(this);
        initTopBar("邀请列表", toptitleView, true);
        initViewPager();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            inviteFriendTopEntity = (InviteFriendTopEntity) bundle.getSerializable("inviteFriendTopEntity");
        }
        if (inviteFriendTopEntity != null) {
            ivTop.setImageURI(Uri.parse(inviteFriendTopEntity.getImg()));
        }
    }

    // 点击事件
    @Event(value = {R.id.imgview_back, R.id.btnInvite})
    private void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgview_back:
                this.finish();
                break;
            case R.id.btnInvite:
                if (inviteFriendTopEntity != null) {
                    AppUtils.shareInviteFrient(inviteFriendTopEntity, this, StaticMembers.SHARE_LIST, 4);
                }
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    private void initViewPager() {
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(1);
        // 包含3个fragment界面
        List<TabIndicatorEntity> list = ViewPagerUtils.getTabIndicator(1);
        // 3个fragment界面封装
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new InviteFriendFragment());
        // 设置ViewPager适配器
        ViewPagerFragmentAdapter viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), list, fragmentList);
        mViewPager.setAdapter(viewPagerFragmentAdapter);
    }
}
