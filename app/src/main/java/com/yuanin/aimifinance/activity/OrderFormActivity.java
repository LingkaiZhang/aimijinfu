package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.OrderFormEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.FmtMicrometer;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.StaticMembers;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;

/**
 * 订单详情
 */

public class OrderFormActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvNoContent)
    private TextView tvNoContent;
    @ViewInject(R.id.llNoNet)
    private LinearLayout llNoNet;
    @ViewInject(R.id.flMain)
    private FrameLayout flMain;
    @ViewInject(R.id.viewRemind)
    private View viewRemind;
    @ViewInject(R.id.viewLoading)
    private View viewLoading;
    @ViewInject(R.id.imgLogo)
    private ImageView imgLogo;
    @ViewInject(R.id.tvName)
    private TextView tvName;
    @ViewInject(R.id.tvStatus)
    private TextView tvStatus;
    @ViewInject(R.id.tvMoney)
    private TextView tvMoney;
    @ViewInject(R.id.tvRate)
    private TextView tvRate;
    @ViewInject(R.id.tvEarn)
    private TextView tvEarn;
    @ViewInject(R.id.tvTime)
    private TextView tvTime;
    @ViewInject(R.id.tvType)
    private TextView tvType;
    @ViewInject(R.id.tvInvestTime)
    private TextView tvInvestTime;
    @ViewInject(R.id.tvEarnTime)
    private TextView tvEarnTime;
    @ViewInject(R.id.tvEndTime)
    private TextView tvEndTime;
    @ViewInject(R.id.tvNoFinishMoney)
    private TextView tvNoFinishMoney;
    @ViewInject(R.id.tvNoFinishEarn)
    private TextView tvNoFinishEarn;
    @ViewInject(R.id.tvFinishMoney)
    private TextView tvFinishMoney;
    @ViewInject(R.id.tvFinishEarn)
    private TextView tvFinishEarn;
    @ViewInject(R.id.imgRedPackets)
    private ImageView imgRedPackets;
    @ViewInject(R.id.imageLoading)
    private ImageView loadingImage;
    @ViewInject(R.id.tvNoNet)
    private TextView tvNoNet;
    @ViewInject(R.id.mine_contract_mode)
    private View mineContractMode;
    @ViewInject(R.id.tvNoAward)
    private TextView tvNoAward;
    @ViewInject(R.id.tvFinishAward)
    private TextView tvFinishAward;
    @ViewInject(R.id.rlNoAward)
    private RelativeLayout rlNoAward;
    @ViewInject(R.id.rlFinishAward)
    private RelativeLayout rlFinishAward;
    @ViewInject(R.id.imageView)
    private ImageView imageView;
    @ViewInject(R.id.rlDebtAssignment)
    private RelativeLayout rlDebtAssignment;
    @ViewInject(R.id.tv_product_detail)
    private Button tvProductDetail;
    @ViewInject(R.id.tv_debt_protocol)
    private Button tvDebtProtocol;


    private String entityID;
    private OrderFormEntity orderFormEntity;
    private Context context = OrderFormActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.Order_Form), toptitleView, true);
        entityID = getIntent().getStringExtra("entityID");
        requestData();
    }

    @Event(value = {R.id.btnRefresh, R.id.imgRedPackets, R.id.viewer_contract, R.id.btnCheckNetwork, R.id.tv_product_detail, R.id.tv_debt_protocol})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_product_detail:
                Intent intent2 = new Intent(context, FinanceProductDetailActivity.class);
                intent2.putExtra("entityID", orderFormEntity.getBorrowAmountId());
                context.startActivity(intent2);
                break;
            case R.id.tv_debt_protocol:
                if (orderFormEntity.getTransferProtocolLink() == null) {
                    AppUtils.showMiddleToast(context,"债权转让协议暂未生成，请耐心等待。。。");
                } else {
                    Intent intentPDF2 = new Intent(context, PDFActivity.class);
                    intentPDF2.putExtra("pdfURL",orderFormEntity.getTransferProtocolLink());
                    intentPDF2.putExtra("isTranfer",true);
                    startActivity(intentPDF2);
                }
                break;
            //电子合同
            case R.id.viewer_contract:
//                 Toast.makeText(context,orderFormEntity.getEle_contact_link(),Toast.LENGTH_SHORT).show();
                Intent intentPDF = new Intent(context, PDFActivity.class);
                intentPDF.putExtra("pdfURL",orderFormEntity.getEle_contact_link());
                startActivity(intentPDF);
                break;
            //刷新
            case R.id.btnRefresh:
                requestData();
                break;
            //检查网络
            case R.id.btnCheckNetwork:
                AppUtils.checkNetwork(context);
                break;
            //红包
            case R.id.imgRedPackets:
                if (StaticMembers.IS_NEED_LOGIN) {
                    startActivity(new Intent(context, LoginRegisterActivity.class));
                } else {
                    if (orderFormEntity.getRed_type() == 1) {
                        Intent intent = new Intent(OrderFormActivity.this, WebViewActivity.class);
                        intent.putExtra(ParamsKeys.TYPE, ParamsValues.SHARE_RED_PACKET);
                        startActivity(intent);
                    } else if (orderFormEntity.getRed_type() == 2) {
                        Intent intent = new Intent(OrderFormActivity.this, WebViewActivity.class);
                        intent.putExtra(ParamsKeys.TYPE, ParamsValues.SHARE_NEW_RED_PACKET);
                        intent.putExtra("invest_id", entityID);
                        startActivity(intent);
                    }
                }
                break;
        }
    }

    private void requestData() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_PRODUCT);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_GET_NEW_INVEST_DETAIL);
            obj.put(ParamsKeys.INVEST_ID, entityID);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<OrderFormEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, new IHttpRequestCallBack() {
            @Override
            public void onStart() {
                ((Animatable) loadingImage.getDrawable()).start();
                viewRemind.setVisibility(View.GONE);
                viewLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                if (AppUtils.isNetworkConnected(context)) {
                    tvNoNet.setText(context.getResources().getString(R.string.connect_fail));
                } else {
                    tvNoNet.setText(context.getResources().getString(R.string.network_fail));
                }
                ((Animatable) loadingImage.getDrawable()).stop();
                viewRemind.setVisibility(View.VISIBLE);
                viewLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(Object object) {
                ReturnResultEntity<OrderFormEntity> entity = (ReturnResultEntity<OrderFormEntity>) object;
                if (entity.isSuccess(context)) {
                    if (entity.isNotNull()) {
                        orderFormEntity = entity.getData().get(0);
                        setUpView();
                        flMain.setVisibility(View.VISIBLE);
                        tvNoContent.setVisibility(View.GONE);
                        llNoNet.setVisibility(View.GONE);
                    } else {
                        flMain.setVisibility(View.GONE);
                        tvNoContent.setVisibility(View.VISIBLE);
                        llNoNet.setVisibility(View.GONE);
                    }
                } else {
                    flMain.setVisibility(View.GONE);
                    tvNoContent.setVisibility(View.GONE);
                    llNoNet.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure() {
                flMain.setVisibility(View.GONE);
                tvNoContent.setVisibility(View.GONE);
                llNoNet.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setUpView() {
        switch (Integer.parseInt(orderFormEntity.getBorrow_status_id())) {
            case 2:
                tvStatus.setText("募集中");
                tvStatus.setTextColor(getResources().getColor(R.color.jin_xing));
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.yellow_print));
                break;
            case 3:
                tvStatus.setText("已满标");
                tvStatus.setTextColor(getResources().getColor(R.color.man_biao));
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.blue_print));
                break;
            case 4:
                tvStatus.setText("已流标");
                tvStatus.setTextColor(getResources().getColor(R.color.liu_biao));
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.gray_print));
                break;
            case 7:
                //TODO  提前结清
                if (orderFormEntity.getIs_prerepayment() != null){
                    if (orderFormEntity.getIs_prerepayment().equals("1")) {
                        tvStatus.setText("提前结清");
                        tvStatus.setTextColor(getResources().getColor(R.color.prepayment));
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.prepayment_print));
                    } else {
                        tvStatus.setText("已还款");
                        tvStatus.setTextColor(getResources().getColor(R.color.huan_kuan));
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.red_print));
                    }
                } else {
                    tvStatus.setText("已还款");
                    tvStatus.setTextColor(getResources().getColor(R.color.huan_kuan));
                    imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.huankuan_print));
                }

                break;
            case 5:
            case 6:
            case 8:
                tvStatus.setText("还款中");
                tvStatus.setTextColor(getResources().getColor(R.color.jin_xing));
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.yellow_print));
                break;
        }
        tvName.setText(orderFormEntity.getProject_name());
        tvMoney.setText(orderFormEntity.getAmount() + "元");
        tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(orderFormEntity.getAnnual_new())) + "%");
        tvEarn.setText(FmtMicrometer.format5(orderFormEntity.getInterest_total()) + "元");
        tvTime.setText(orderFormEntity.getTerm());
        tvType.setText(orderFormEntity.getRepay_method());
        tvInvestTime.setText(orderFormEntity.getInvestdate());
        tvEarnTime.setText(orderFormEntity.getInterestdate());
        tvEndTime.setText(orderFormEntity.getExpire_time());
        tvNoFinishMoney.setText(FmtMicrometer.format5(orderFormEntity.getCapital_wait()) + "元");
        tvNoFinishEarn.setText(FmtMicrometer.format5(orderFormEntity.getInterest_wait()) + "元");
        tvFinishMoney.setText(FmtMicrometer.format5(orderFormEntity.getCapital_yes()) + "元");
        tvFinishEarn.setText(FmtMicrometer.format5(orderFormEntity.getInterest_yes()) + "元");
        if (orderFormEntity.getIs_red() == 1) {
            imgRedPackets.setVisibility(View.VISIBLE);
            imgLogo.setVisibility(View.VISIBLE);
            setImgAnimation();
        } else {
            imgRedPackets.setVisibility(View.GONE);
            imgLogo.setVisibility(View.GONE);
        }
        if (orderFormEntity.getEle_contact_link() == null || orderFormEntity.getEle_contact_link() == "") {
            mineContractMode.setVisibility(View.GONE);
        } else {
            mineContractMode.setVisibility(View.VISIBLE);
        }
        //订单奖励
        if(orderFormEntity.getWait_interest() == 0 && orderFormEntity.getYes_interest() == 0) {
            rlNoAward.setVisibility(View.GONE);
            rlFinishAward.setVisibility(View.GONE);
        } else {
            tvNoAward.setText(FmtMicrometer.format5(orderFormEntity.getWait_interest()) + "元");
            tvFinishAward.setText(FmtMicrometer.format5(orderFormEntity.getYes_interest()) + "元");
        }

        //债转标的两个底部按钮
        if (orderFormEntity.getIsTransferBid() == 0) {
            rlDebtAssignment.setVisibility(View.GONE);
        } else {
            rlDebtAssignment.setVisibility(View.VISIBLE);
        }

    }

    private void setImgAnimation() {
        //上下摇摆
        TranslateAnimation alphaAnimation2 = new TranslateAnimation(50f, 50F, 50F, 80F);  //同一个x轴 (开始结束都是50f,所以x轴保存不变)  y轴开始点50f  y轴结束点80f
        alphaAnimation2.setDuration(800);  //设置时间
        alphaAnimation2.setRepeatCount(Animation.INFINITE);  //为重复执行的次数。如果设置为n，则动画将执行n+1次。INFINITE为无限制播放
        alphaAnimation2.setRepeatMode(Animation.REVERSE);  //为动画效果的重复模式，常用的取值如下。RESTART：重新从头开始执行。REVERSE：反方向执行
        imgRedPackets.setAnimation(alphaAnimation2);
        alphaAnimation2.start();
    }
}
