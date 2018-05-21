package com.yuanin.aimifinance.base;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.umeng.analytics.MobclickAgent;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.entity.TopBarInfo;
import com.yuanin.aimifinance.utils.AppManager;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.StaticMembers;
import com.yuanin.aimifinance.utils.TopBarUtil;
import com.yuanin.aimifinance.view.XListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public abstract class BaseListActivity extends Activity implements XListView.IXListViewListener {

    private XListView listView;
    private TextView titleTextView;
    private Animation animation;
    private GeneralDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
//        PushAgent.getInstance(this).onAppStart();
        if (animation == null) {
            animation = AnimationUtils.loadAnimation(this, R.anim.top_title_enter);
        }
        /**
         * 获取手机屏幕宽度/高度 赋值给StaticCache类中对应的静态变量 用于整个程序
         */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    // 设置ListView是否支持上下拉
    protected void setListViewFunction(XListView listView, boolean hasRefresh, boolean hasOnLoad) {
        this.listView = listView;
        listView.setPullRefreshEnable(hasRefresh);
        listView.setPullLoadEnable(hasOnLoad);
    }

    protected void loadComplete() {
        listView.stopRefresh();
        listView.stopLoadMore();
        listView.setRefreshTime(new SimpleDateFormat(StaticMembers.DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
    }


    /**
     * 给TopBar赋值
     *
     * @param name ：TopBar中间的名称
     * @param view ：TopBar的View
     */
    protected void initTopBar(String name, View view, boolean hasBack) {
        TopBarInfo topBarInfo = new TopBarInfo();
        topBarInfo.setTxtStr(name);
        titleTextView = (TextView) view.findViewById(R.id.tv_title);
        if (hasBack) {
            topBarInfo.setLeftBtnListener(new TopBarUtil().new AlwaysUseListener(this));
        }
        TopBarUtil.setTopBarInfo(view, topBarInfo);
    }

    /**
     * 给TopBar赋值
     *
     * @param name ：TopBar中间的名称
     * @param view ：TopBar的View
     */
    protected void initTopBarWithPhone(String name, View view) {
        TopBarInfo topBarInfo = new TopBarInfo();
        topBarInfo.setTxtStr(name);
        titleTextView = (TextView) view.findViewById(R.id.tv_title);
        topBarInfo.setLeftBtnListener(new TopBarUtil().new AlwaysUseListener(this));
        topBarInfo.setRightPicId(R.drawable.selector_right_call);
        topBarInfo.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new GeneralDialog(BaseListActivity.this, true, "联系客服", "客服电话：" + ParamsValues.TEL, "取消", "拨打", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Acp.getInstance(BaseListActivity.this).request(new AcpOptions.Builder()
                                        .setPermissions(Manifest.permission.CALL_PHONE)
                                        .build(),
                                new AcpListener() {
                                    @Override
                                    public void onGranted() {
                                        //用intent启动拨打电话
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ParamsValues.TEL));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onDenied(List<String> permissions) {
                                        AppUtils.showToast(BaseListActivity.this, permissions.toString() + "权限拒绝");
                                    }
                                });
                    }
                });
            }
        });
        TopBarUtil.setTopBarInfo(view, topBarInfo);
    }

    protected void initTopBarWithRandow(String name, View view, int rightPic, View.OnClickListener rightLis) {
        TopBarInfo topBarInfo = new TopBarInfo();
        topBarInfo.setTxtStr(name);
        titleTextView = (TextView) view.findViewById(R.id.tv_title);
        topBarInfo.setLeftBtnListener(new TopBarUtil().new AlwaysUseListener(this));
        topBarInfo.setRightPicId(rightPic);
        topBarInfo.setRightClickListener(rightLis);
        TopBarUtil.setTopBarInfo(view, topBarInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (titleTextView != null) {
            titleTextView.startAnimation(animation);
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
