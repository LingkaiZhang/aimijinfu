package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.utils.AppManager;
import com.yuanin.aimifinance.utils.ParamsKeys;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import de.greenrobot.event.EventBus;

public class ResultActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvTips)
    private TextView tvTips;
    @ViewInject(R.id.llPay)
    private LinearLayout llPay;
    @ViewInject(R.id.llBuy)
    private LinearLayout llBuy;
    @ViewInject(R.id.btnRun)
    private Button btnRun;
    @ViewInject(R.id.btnView)
    private Button btnView;
    @ViewInject(R.id.btnBuy)
    private Button btnBuy;
    @ViewInject(R.id.ivIcon)
    private ImageView ivIcon;

    private int needMoney;
    private Context context = ResultActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        x.view().inject(this);
        int result = getIntent().getIntExtra("result", 0);
        needMoney = getIntent().getIntExtra("needMoney", 0);
        switch (result) {
            case 1:
                initTopBarWithPhone("订单结果", toptitleView);
                llPay.setVisibility(View.GONE);
                llBuy.setVisibility(View.VISIBLE);
                ivIcon.setImageResource(R.mipmap.result_going);
                String str = "您的出借申请已提交，系统正在处理订单，稍后请查看交易流水或下拉刷新个人中心，查看账户余额变动情况。";
                int fstart = str.indexOf("下拉刷新");
                int fend = fstart + "下拉刷新".length();
                SpannableStringBuilder style = new SpannableStringBuilder(str);
                style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_color)), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                tvTips.setText(style);
                btnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(context, MyInvestActivity.class));
                    }
                });
                btnBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ResultActivity.this.finish();
                    }
                });
                break;
            case 2:
                initTopBarWithPhone("提现结果", toptitleView);
                llPay.setVisibility(View.VISIBLE);
                llBuy.setVisibility(View.GONE);
                ivIcon.setImageResource(R.mipmap.result_going);
                String str2 = "您的提现申请已提交，订单正在处理中，稍后请查看交易流水或下拉刷新个人中心，查看账户余额变动情况。";
                int fstart2 = str2.indexOf("下拉刷新");
                int fend2 = fstart2 + "下拉刷新".length();
                SpannableStringBuilder style2 = new SpannableStringBuilder(str2);
                style2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_color)), fstart2, fend2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                tvTips.setText(style2);
                btnRun.setText("返回个人中心");
                btnRun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ResultActivity.this.finish();
                    }
                });
                break;
            //充值成功
            case 3:
                initTopBarWithPhone("充值结果", toptitleView);
                llPay.setVisibility(View.VISIBLE);
                llBuy.setVisibility(View.GONE);
                tvTips.setTextSize(20);
                ivIcon.setImageResource(R.mipmap.result_success);
                tvTips.setText("恭喜您充值成功！");
                btnRun.setText("立即出借");
                btnRun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (needMoney == 0) {
                            //通知homepage跳到产品
                            EventMessage eventMessage = new EventMessage();
                            eventMessage.setType(EventMessage.HOMEPAGE_JUMP_TAB);
                            eventMessage.setObject(1);
                            EventBus.getDefault().post(eventMessage);
                        }
                        ResultActivity.this.finish();
                    }
                });
                break;
            //充值处理中
            case 4:
                initTopBarWithPhone("充值结果", toptitleView);
                llPay.setVisibility(View.VISIBLE);
                llBuy.setVisibility(View.GONE);
                ivIcon.setImageResource(R.mipmap.result_going);
                String str3 = "您的充值申请已提交，订单正在处理中，稍后请查看交易流水或下拉刷新个人中心，查看账户余额变动情况。";
                int fstart3 = str3.indexOf("下拉刷新");
                int fend3 = fstart3 + "下拉刷新".length();
                SpannableStringBuilder style3 = new SpannableStringBuilder(str3);
                style3.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_color)), fstart3, fend3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                tvTips.setText(style3);
                btnRun.setText("返回个人中心");
                btnRun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (needMoney != 0) {
                            AppManager.getAppManager().finishActivity(BuyRegularActivity.class);
                            //跳转
                            Bundle bundle = new Bundle();
                            bundle.putInt(ParamsKeys.REFRESH_HOME_PAGE, 2);
                            EventBus.getDefault().post(bundle);
                        }
                        ResultActivity.this.finish();
                    }
                });
                break;
        }
    }
}
