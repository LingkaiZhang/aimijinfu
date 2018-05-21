package com.yuanin.aimifinance.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.AdvertisementEntity;
import com.yuanin.aimifinance.utils.StaticMembers;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

public class AdvertisementActivity extends BaseActivity {
    @ViewInject(R.id.ivPic)
    private SimpleDraweeView ivPic;
    @ViewInject(R.id.tvJump)
    private TextView tvJump;


    private AdvertisementEntity advertisementEntity;
    private Timer timer;
    private int recLen = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);
        x.view().inject(this);
        advertisementEntity = (AdvertisementEntity) getIntent().getSerializableExtra("advertisementEntity");
        if (advertisementEntity != null) {
            ivPic.setImageURI(Uri.parse(advertisementEntity.getSrc()));
            Log.e("hx", "url:" + advertisementEntity.getSrc());
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {      // UI thread
                    @Override
                    public void run() {
                        tvJump.setText("跳过(" + recLen + "s)");
                        recLen -= 1;
                        if (recLen < 0) {
                            timer.cancel();
                            if (StaticMembers.IS_NEED_FINGER_PWD) {
                                Intent intent = new Intent(AdvertisementActivity.this, FingerPasswordActivity.class);
                                startActivity(intent);
                            } else if (StaticMembers.IS_NEED_GUSTURE_PWD) {
                                Intent intent = new Intent(AdvertisementActivity.this, GesturePasswordVerifyActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(AdvertisementActivity.this, HomePageActivity.class);
                                startActivity(intent);
                            }
                            AdvertisementActivity.this.finish();
                            overridePendingTransition(R.anim.fade_in,
                                    R.anim.fade_out);
                        }
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 1000);       // timeTask
    }

    // 点击事件
    @Event(value = {R.id.tvJump, R.id.flMain})
    private void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvJump:
                if (timer != null) {
                    timer.cancel();
                }
                if (StaticMembers.IS_NEED_GUSTURE_PWD) {
                    if (StaticMembers.IS_NEED_FINGER_PWD) {
                        Intent intent = new Intent(AdvertisementActivity.this, FingerPasswordActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(AdvertisementActivity.this, GesturePasswordVerifyActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(AdvertisementActivity.this, HomePageActivity.class);
                    startActivity(intent);
                }
                this.finish();
                overridePendingTransition(R.anim.fade_in,
                        R.anim.fade_out);
                break;
            case R.id.flMain:
                if (advertisementEntity.getHref() != null && advertisementEntity.getHref().length() > 0) {
                    if (timer != null) {
                        timer.cancel();
                    }
                    if (StaticMembers.IS_NEED_FINGER_PWD) {
                        Intent intent = new Intent(AdvertisementActivity.this, FingerPasswordActivity.class);
                        intent.putExtra("url", advertisementEntity.getHref());
                        startActivity(intent);
                    } else if (StaticMembers.IS_NEED_GUSTURE_PWD) {
                        Intent intent = new Intent(AdvertisementActivity.this, GesturePasswordVerifyActivity.class);
                        intent.putExtra("url", advertisementEntity.getHref());
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(AdvertisementActivity.this, HomePageActivity.class));
                        Intent intent = new Intent(AdvertisementActivity.this, AdvertisementWebActivity.class);
                        intent.putExtra("url", advertisementEntity.getHref());
                        startActivity(intent);
                    }
                    this.finish();
                    overridePendingTransition(R.anim.fade_in,
                            R.anim.fade_out);
                }
                break;
        }
    }

}
