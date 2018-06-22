package com.yuanin.aimifinance.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.entity.BuySuccessEntity;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.StaticMembers;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class BuySuccessActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvServeCall)
    private TextView tvServeCall;
    @ViewInject(R.id.imgRedPackets)
    private ImageView imgRedPackets;
    @ViewInject(R.id.imgLogo)
    private ImageView imgLogo;
    @ViewInject(R.id.tvProductName)
    private TextView tvProductName;
    @ViewInject(R.id.tvMoney)
    private TextView tvMoney;
    @ViewInject(R.id.tvType)
    private TextView tvType;
    @ViewInject(R.id.tvRedPacket)
    private TextView tvRedPacket;
    @ViewInject(R.id.bannerView)
    private SimpleDraweeView bannerView;
    @ViewInject(R.id.llMain)
    private LinearLayout llMain;
    @ViewInject(R.id.popMain)
    private LinearLayout popMain;


    private BuySuccessEntity buySuccessEntity;
    private GeneralDialog dialog;
    private View popView;
    private PopupWindow mPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_success);
        popView = getLayoutInflater().inflate(R.layout.popupwindow_redpacket, null);
        x.view().inject(this, popView);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.BuySuccess), toptitleView, true);
        buySuccessEntity = (BuySuccessEntity) getIntent().getSerializableExtra("buySuccessEntity");
        iniView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (buySuccessEntity.getIs_red() == 1) {
            if (mPop == null) {
                mPop = new PopupWindow(popView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
                mPop.setAnimationStyle(R.style.SignPopupWindowAnimation);
                mPop.setFocusable(false);
                mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
            }
        }
    }

    @Event(value = {R.id.tvServeCall, R.id.imgRedPackets, R.id.bannerView, R.id.btnConfirm,
            R.id.popMain, R.id.btnInvite, R.id.ivClose})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //联系客服
            case R.id.tvServeCall:
                dialog = new GeneralDialog(this, true, "联系客服", "客服电话：" + ParamsValues.TEL, "取消", "拨打", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Acp.getInstance(BuySuccessActivity.this).request(new AcpOptions.Builder()
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
                                        AppUtils.showToast(BuySuccessActivity.this, permissions.toString() + "权限拒绝");
                                    }
                                });
                    }
                });
                break;
            case R.id.btnInvite:
            case R.id.imgRedPackets:
                if (StaticMembers.IS_NEED_LOGIN) {
                    startActivity(new Intent(BuySuccessActivity.this, LoginRegisterActivity.class));
                } else {
                    if (buySuccessEntity.getRed_type() == 1) {
                        Intent intent = new Intent(BuySuccessActivity.this, WebViewActivity.class);
                        intent.putExtra(ParamsKeys.TYPE, ParamsValues.SHARE_RED_PACKET);
                        startActivity(intent);
                    } else if (buySuccessEntity.getRed_type() == 2) {
                        Intent intent = new Intent(BuySuccessActivity.this, WebViewActivity.class);
                        intent.putExtra(ParamsKeys.TYPE, ParamsValues.SHARE_NEW_RED_PACKET);
                        intent.putExtra("invest_id", buySuccessEntity.getInvest_id());
                        startActivity(intent);
                    }
                    if (mPop != null && mPop.isShowing()) {
                        mPop.dismiss();
                        imgRedPackets.setVisibility(View.VISIBLE);
                        imgLogo.setVisibility(View.VISIBLE);
                        setImgAnimation();
                    }
                }
                break;
            case R.id.bannerView:
                Intent intent = new Intent(this, HrefActivity.class);
                intent.putExtra("url", buySuccessEntity.getBanner_url());
                startActivity(intent);
                break;
            case R.id.btnConfirm:
                this.finish();
                break;
            case R.id.ivClose:
            case R.id.popMain:
                if (mPop != null && mPop.isShowing()) {
                    ScaleAnimation animation1 = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.8f, Animation.RELATIVE_TO_SELF, 0.9f);
                    animation1.setDuration(500);
                    ScaleAnimation animation2 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
                    animation2.setDuration(500);
                    imgRedPackets.startAnimation(animation2);
                    popMain.startAnimation(animation1);
                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            imgRedPackets.setVisibility(View.VISIBLE);
                            imgLogo.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mPop.dismiss();
                            setImgAnimation();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
                break;
        }
    }

    private void iniView() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bannerView.getLayoutParams();
        lp.width = StaticMembers.SCREEN_WIDTH;
        bannerView.setLayoutParams(lp);
        bannerView.setImageURI(Uri.parse(buySuccessEntity.getBanner()));
        bannerView.setAspectRatio(2.27f);
        tvProductName.setText(buySuccessEntity.getProductName());
        tvMoney.setText(buySuccessEntity.getBuyMoney() + "元");
        tvRedPacket.setText(buySuccessEntity.getRedPacket());
        tvType.setText(buySuccessEntity.getRepay_method());
        tvServeCall.setText(ParamsValues.TEL);
        tvServeCall.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvServeCall.getPaint().setAntiAlias(true);//抗锯齿
    }

    private void setImgAnimation() {
        //上下摇摆
        TranslateAnimation alphaAnimation2 = new TranslateAnimation(0f, 0F, 0F, 30F);  //同一个x轴 (开始结束都是50f,所以x轴保存不变)  y轴开始点50f  y轴结束点80f
        alphaAnimation2.setDuration(800);  //设置时间
        alphaAnimation2.setRepeatCount(Animation.INFINITE);  //为重复执行的次数。如果设置为n，则动画将执行n+1次。INFINITE为无限制播放
        alphaAnimation2.setRepeatMode(Animation.REVERSE);  //为动画效果的重复模式，常用的取值如下。RESTART：重新从头开始执行。REVERSE：反方向执行
        imgRedPackets.setAnimation(alphaAnimation2);
        alphaAnimation2.start();
    }
}
