package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.entity.BuyProductEntity;
import com.yuanin.aimifinance.entity.BuySuccessEntity;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.entity.RedPacketsEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
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
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 购买爱米定期
 */
public class BuyRegularActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvRate)
    private TextView tvRate;
    @ViewInject(R.id.tvTime)
    private TextView tvTime;
    @ViewInject(R.id.tvLeaveMoney)
    private TextView tvLeaveMoney;
    @ViewInject(R.id.tvEarnMoney)
    private TextView tvEarnMoney;
    @ViewInject(R.id.tvName)
    private TextView tvName;
    @ViewInject(R.id.tvUnit)
    private TextView tvUnit;
    @ViewInject(R.id.tvPayMoney)
    private TextView tvPayMoney;
    @ViewInject(R.id.tvBalance)
    private TextView tvBalance;
    @ViewInject(R.id.tvRedPackets)
    private TextView tvRedPackets;
    @ViewInject(R.id.tvRedPacketsEarnMoney)
    private TextView tvRedPacketsEarnMoney;
    @ViewInject(R.id.tvPlus)
    private TextView tvPlus;
    @ViewInject(R.id.llLeft)
    private LinearLayout llLeft;
    @ViewInject(R.id.llMiddle)
    private LinearLayout llMiddle;
    @ViewInject(R.id.llRight)
    private LinearLayout llRight;
    @ViewInject(R.id.ivRight)
    private ImageView ivRight;
    @ViewInject(R.id.etShare)
    private EditText etShare;
    @ViewInject(R.id.btnConfirm)
    private Button btnConfirm;
    @ViewInject(R.id.cbChoose)
    private CheckBox cbChoose;
    @ViewInject(R.id.rlRedPackets)
    private RelativeLayout rlRedPackets;
    @ViewInject(R.id.tvNoContent)
    private TextView tvNoContent;
    @ViewInject(R.id.llNoNet)
    private LinearLayout llNoNet;
    @ViewInject(R.id.viewRemind)
    private View viewRemind;
    @ViewInject(R.id.viewLoading)
    private View viewLoading;
    @ViewInject(R.id.flMain)
    private FrameLayout flMain;
    @ViewInject(R.id.imageLoading)
    private ImageView loadingImage;
    @ViewInject(R.id.tvNoNet)
    private TextView tvNoNet;
    @ViewInject(R.id.llMain)
    private LinearLayout llMain;
    @ViewInject(R.id.tvTimeNew)
    private TextView tvTimeNew;
    @ViewInject(R.id.tvSingle)
    private TextView tvSingle;
    @ViewInject(R.id.tvLeaveMoneyNew)
    private TextView tvLeaveMoneyNew;

    @ViewInject(R.id.interestRates)
    private TextView interestRates;

    private String entityID;
    private BuyProductEntity buyProductEntity;
    private boolean isAgree = true;
    private RedPacketsEntity redPacketsEntity;
    private List<RedPacketsEntity> redPacketsList;
    private ScaleAnimation leftAnimation, leftAnimation2;
    private boolean isAnimationEnd = true;
    private Context context = BuyRegularActivity.this;
    private GeneralDialog generalDialog;
    private View popView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_regular);
        popView = getLayoutInflater().inflate(R.layout.popupwindow_hk_register_new, null, false);
        x.view().inject(this);
        btnConfirm.setClickable(false);
        entityID = getIntent().getStringExtra("entityID");
        initTopBarWithRightText("确认支付", toptitleView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyRegularActivity.this, FinanceProductDetailActivity.class);
                intent.putExtra("entityID", entityID);
                startActivity(intent);
            }
        }, "项目详情");
        //刷新个人中心数据
        EventMessage eventMessage = new EventMessage();
        eventMessage.setType(EventMessage.REFRESH_MINE);
        EventBus.getDefault().post(eventMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!StaticMembers.IS_NEED_LOGIN) {
            requestData();
        }
    }

    private void requestData() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_PRODUCT);
            obj.put(ParamsKeys.MOTHED, ParamsValues.GET_BUY_PRODUCT_DETAIL);
            obj.put(ParamsKeys.PRODUCT_ID, entityID);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<BuyProductEntity>>() {
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
                        ReturnResultEntity<BuyProductEntity> entity = (ReturnResultEntity<BuyProductEntity>) object;
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                buyProductEntity = entity.getData().get(0);
                                redPacketsList = buyProductEntity.getList();
                                initView();
                                initListener();
                                btnConfirm.setClickable(true);
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
                }
        );
    }

    private void initView() {
        etShare.requestFocus();
        AppUtils.openKeyboard(etShare);
        StaticMembers.HK_STATUS = buyProductEntity.getIs_activate_hkaccount();
        StaticMembers.BANK_CARD_STATUS = buyProductEntity.getIs_bind_bankcard();
 //       tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(buyProductEntity.getAnnual())));
        tvTime.setText(buyProductEntity.getTerm());
        tvTimeNew.setText(buyProductEntity.getTerm());
        tvSingle.setText(buyProductEntity.getEachamount());
        tvLeaveMoneyNew.setText(String.valueOf(buyProductEntity.getAmount()));
        tvUnit.setText(buyProductEntity.getUnit());
        tvLeaveMoney.setText(String.valueOf(buyProductEntity.getAmount()));
        tvBalance.setText(String.valueOf(buyProductEntity.getBalance()));
        etShare.setHint("请输入" + buyProductEntity.getEachamount() + "元的整数倍");
        if (buyProductEntity.getIs_new() == 1) {
            rlRedPackets.setVisibility(View.GONE);
        } else {
            rlRedPackets.setVisibility(View.VISIBLE);
        }

        if (buyProductEntity.getExtannual() == null || buyProductEntity.getOrgannual() == null) {
            tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(buyProductEntity.getAnnual())));
            interestRates.setVisibility(View.GONE);
        } else {
            if (Double.valueOf(buyProductEntity.getExtannual()) > 0 && Double.valueOf(buyProductEntity.getOrgannual()) > 0 ) {
                interestRates.setText("+" + buyProductEntity.getExtannual() + "%");
                tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(buyProductEntity.getOrgannual())));
                interestRates.setVisibility(View.VISIBLE);
            } else {
                tvRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(buyProductEntity.getAnnual())));
                interestRates.setVisibility(View.GONE);
            }
        }

        if (leftAnimation == null) {
            leftAnimation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
            leftAnimation.setDuration(1000);
            llMiddle.startAnimation(leftAnimation);
        }
        if (leftAnimation2 == null) {
            leftAnimation2 = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
            leftAnimation2.setDuration(1000);
            llLeft.startAnimation(leftAnimation2);
            llRight.startAnimation(leftAnimation2);
        }
    }

    private void initListener() {
        etShare.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.toString().substring(0, 1).equals("0")) {
                        etShare.setText("");
                    } else {
                        double fee = (Double.valueOf(s.toString()));
                        double earnMoney = AppUtils.multiply(fee, buyProductEntity.getInterest());
                        double trueEarnMoney = AppUtils.divide(earnMoney, 100);
                        tvPayMoney.setText(s);
                        tvEarnMoney.setText(AppUtils.round(trueEarnMoney, 2) + "");
                        if (buyProductEntity.getIs_new() != 1 && redPacketsList != null && redPacketsList.size() > 0) {
                            chooseRedpacket(fee);
                        }
                    }
                } else {
                    tvPayMoney.setText("0.00");
                    tvEarnMoney.setText("0.00");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAgree = isChecked;
            }
        });
    }

    private void chooseRedpacket(double fee) {
        redPacketsEntity = null;
        for (int i = 0; i < redPacketsList.size(); i++) {
            if (fee >= redPacketsList.get(i).getMin_invest_amount()) {
                redPacketsEntity = redPacketsList.get(i);
                break;
            }
        }
        if (redPacketsEntity == null) {
            if (isAnimationEnd) {
                tvRedPacketsEarnMoney.setVisibility(View.GONE);
                ivRight.setImageResource(R.mipmap.buy_right_arrow);
                tvRedPackets.setText("");
            }
        } else {
            setRedPacketData();
        }
    }

    @Event(value = {R.id.btnConfirm, R.id.rlRedPackets, R.id.tvProtocol, R.id.ivRight, R.id.btnRefresh})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //刷新
            case R.id.btnRefresh:
                requestData();
                break;
            //协议
            case R.id.tvProtocol:
                Intent intent3 = new Intent(this, WebViewHtmlActivity.class);
                intent3.putExtra(ParamsKeys.TYPE, ParamsValues.LOAN_RISK_STATEMENT);
                startActivity(intent3);
                break;
            //选择红包
            case R.id.rlRedPackets:
                String money2 = "";
                if (etShare.getText().toString().trim().length() == 0 || Integer.parseInt(etShare.getText().toString().trim()) < 1) {
                    money2 = "0";
                } else {
                    money2 = etShare.getText().toString().trim();
                }
                Intent intent4 = new Intent(this, ChooseRedpacketActivity.class);
                intent4.putExtra("money", money2);
                startActivityForResult(intent4, 1);
                break;
            //删除红包
            case R.id.ivRight:
                if (redPacketsEntity != null) {
                    if (isAnimationEnd) {
                        redPacketsEntity = null;
                        tvRedPacketsEarnMoney.setVisibility(View.GONE);
                        ivRight.setImageResource(R.mipmap.buy_right_arrow);
                        tvRedPackets.setText("");
                    }
                } else {
                    String money3 = "";
                    if (etShare.getText().toString().trim().length() == 0 || Integer.parseInt(etShare.getText().toString().trim()) < 1) {
                        money3 = "0";
                    } else {
                        money3 = etShare.getText().toString().trim();
                    }
                    Intent intent5 = new Intent(this, ChooseRedpacketActivity.class);
                    intent5.putExtra("money", money3);
                    startActivityForResult(intent5, 1);
                }
                break;
            //提交购买
            case R.id.btnConfirm:
                final String buyqty = etShare.getText().toString().trim();
                if (buyProductEntity.getIs_activate_hkaccount() == 1) {
                    if (buyProductEntity.getIs_bind_bankcard() == 0) {
                        AppUtils.showToast(this, "请先绑定银行卡");
                        startActivity(new Intent(context, AddBankCardActivity.class));
                        return;
                    }
                } else {
                    PopupWindow mPop = AppUtils.createHKPop(popView, context);
                    mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                    return;
                }
                if (buyqty.length() == 0 || Integer.parseInt(buyqty) < 1) {
                    AppUtils.showToast(this, getResources().getString(R.string.buy_regular_input_right_share));
                    return;
                }
                if (Integer.parseInt(buyqty) % Integer.parseInt(buyProductEntity.getEachamount()) != 0) {
                    AppUtils.showToast(this, "请按" + buyProductEntity.getEachamount() + "元的整数倍出借");
                    return;
                }
                if (Integer.parseInt(buyqty) > buyProductEntity.getAmount()) {
                    AppUtils.showToast(this, getResources().getString(R.string.buy_regular_input__share_too_big));
                    return;
                }
                if (buyProductEntity.getAmount() >= buyProductEntity.getMinbuyvote()) {
                    if (Integer.parseInt(buyqty) < buyProductEntity.getMinbuyvote()) {
                        AppUtils.showToast(this, getResources().getString(R.string.buy_regular_input__share_min) + buyProductEntity.getMinbuyvote() + "元");
                        return;
                    }
                }
                if (buyProductEntity.getMaxbuyvote() != 0) {
                    if (Integer.parseInt(buyqty) > buyProductEntity.getMaxbuyvote()) {
                        AppUtils.showToast(this, getResources().getString(R.string.buy_regular_input__share_max) + buyProductEntity.getMaxbuyvote() + "元");
                        return;
                    }
                }
                if (redPacketsEntity != null) {
                    if (Integer.parseInt(buyqty) < redPacketsEntity.getMin_invest_amount()) {
                        AppUtils.showToast(this, getResources().getString(R.string.buy_regular_use_redpacket));
                        return;
                    }
                }
                if (!isAgree) {
                    AppUtils.showToast(this, getResources().getString(R.string.buy_regular_agree));
                    return;
                }
                if (Integer.parseInt(buyqty) > buyProductEntity.getBalance()) {
                    AppUtils.showToast(this, getResources().getString(R.string.buy_regular_balance_not));
                    int money = Integer.parseInt(buyqty) - (int) buyProductEntity.getBalance();
                    Intent intent = new Intent(context, PayInputMoneyActivity.class);
                    intent.putExtra("money", money);
                    startActivity(intent);
                } else {
                    String tips;
                    if (redPacketsEntity != null) {
                        tips = "您确定要出借" + buyqty + "元并使用" + redPacketsEntity.getAmount() + "元红包吗？";
                    } else {
                        tips = "您确定要出借" + buyqty + "元吗？";
                    }
                    generalDialog = new GeneralDialog(context, false, "确认支付",
                            tips, "取消", "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            generalDialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            generalDialog.dismiss();
                            JSONObject obj = AppUtils.getPublicJsonObject(true);
                            try {
                                obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_PRODUCT);
                                obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_BUY_PRODUCT);
                                obj.put(ParamsKeys.PRODUCT_ID, entityID);
                                obj.put(ParamsKeys.BUY_QTY, buyqty);
                                if (redPacketsEntity == null) {
                                    obj.put(ParamsKeys.GIFT_ID, "0");
                                } else {
                                    obj.put(ParamsKeys.GIFT_ID, redPacketsEntity.getId());
                                }
                                String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                                obj.put(ParamsKeys.TOKEN, token);
                                obj.remove(ParamsKeys.KEY);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Type mType = new TypeToken<ReturnResultEntity<BuySuccessEntity>>() {
                            }.getType();
                            NetUtils.request(context, obj, mType, new IHttpRequestCallBack() {
                                        @Override
                                        public void onStart() {

                                        }

                                        @Override
                                        public void onFinish() {

                                        }

                                        @Override
                                        public void onSuccess(Object object) {
                                            ReturnResultEntity<BuySuccessEntity> entity = (ReturnResultEntity<BuySuccessEntity>) object;
                                            if (entity.isSuccess(context)) {
                                                if (entity.isNotNull()) {
                                                    BuySuccessEntity buySuccessEntity = entity.getData().get(0);
                                                    buySuccessEntity.setProductName(buyProductEntity.getProject_name());
                                                    buySuccessEntity.setBuyMoney(buyqty);
                                                    if (redPacketsEntity != null) {
                                                        buySuccessEntity.setRedPacket("红包" + redPacketsEntity.getAmount() + "元");
                                                    } else {
                                                        buySuccessEntity.setRedPacket("无红包");
                                                    }
                                                    //购买成功
                                                    Intent intent = new Intent(context, BuySuccessNewActivity.class);
                                                    intent.putExtra("buySuccessEntity", buySuccessEntity);
                                                    startActivity(intent);
                                                }
                                                //刷新个人中心数据
                                                EventMessage eventMessage = new EventMessage();
                                                eventMessage.setType(EventMessage.REFRESH_MINE);
                                                EventBus.getDefault().post(eventMessage);
                                                //刷新商品界面数据
                                                EventMessage eventMessage2 = new EventMessage();
                                                eventMessage2.setType(EventMessage.REFRESH_FINANCE_PRODUCT);
                                                EventBus.getDefault().post(eventMessage2);
                                                BuyRegularActivity.this.finish();
                                            } else {
                                                AppUtils.showToast(context, entity.getRemark());
                                            }
                                        }

                                        @Override
                                        public void onFailure() {
                                            AppUtils.showConectFail(context);
                                        }
                                    }
                            );
                        }
                    });
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                if (data != null) {
                    redPacketsEntity = (RedPacketsEntity) data.getExtras().getSerializable("entity");
                    setRedPacketData();
                }
                break;
        }
    }

    private void setRedPacketData() {
        tvRedPackets.setText(redPacketsEntity.getName());
        ivRight.setImageResource(R.mipmap.redpacket_close);
        tvRedPacketsEarnMoney.setVisibility(View.VISIBLE);
        tvRedPacketsEarnMoney.setText("+" + redPacketsEntity.getAmount());
    }
}
