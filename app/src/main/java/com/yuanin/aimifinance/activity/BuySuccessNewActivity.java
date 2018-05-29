package com.yuanin.aimifinance.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.BuySuccessEntity;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.utils.StaticMembers;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import de.greenrobot.event.EventBus;

public class BuySuccessNewActivity extends BaseActivity {

    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvServeCall)
    private TextView tvServeCall;
    @ViewInject(R.id.bannerView)
    private SimpleDraweeView bannerView;

    private BuySuccessEntity buySuccessEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_success_new);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.BuySuccess), toptitleView, true);
        buySuccessEntity = (BuySuccessEntity) getIntent().getSerializableExtra("buySuccessEntity");
        iniView();
    }

    private void iniView() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bannerView.getLayoutParams();
        lp.width = StaticMembers.SCREEN_WIDTH;
        bannerView.setLayoutParams(lp);
        bannerView.setImageURI(Uri.parse(buySuccessEntity.getBanner()));
        bannerView.setAspectRatio(2.27f);
    }

    @Event(value = {R.id.btn_check_order, R.id.btn_continue_loan, R.id.bannerView})
    private void onViewClicked(View v){
        switch (v.getId()) {
            case R.id.bannerView:
                Intent intent = new Intent(this, HrefActivity.class);
                intent.putExtra("url", buySuccessEntity.getBanner_url());
                startActivity(intent);
                break;
            case R.id.btn_check_order:
                Intent intent1 = new Intent(this, OrderFormActivity.class);
                intent1.putExtra("entityID",buySuccessEntity.getInvest_id());
                startActivity(intent1);
                break;
            case R.id.btn_continue_loan:
                Intent intent2 = new Intent(this, HomePageActivity.class);
                //通知homepage跳到产品
                EventMessage eventMessage = new EventMessage();
                eventMessage.setType(EventMessage.HOMEPAGE_JUMP_TAB);
                eventMessage.setObject(1);
                EventBus.getDefault().post(eventMessage);
                this.finish();
                break;
        }
    }
}
