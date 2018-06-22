package com.yuanin.aimifinance.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.entity.HKRegisterEntity;
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

/**
 * 个人设置
 */
public class PersonalSettingsActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvPhone)
    private TextView tvPhone;
    @ViewInject(R.id.tvRealNameCertification)
    private TextView tvRealNameCertification;
    @ViewInject(R.id.tvRiskAssessmentTips)
    private TextView tvRiskAssessmentTips;
    @ViewInject(R.id.llMain)
    private LinearLayout llMain;
    @ViewInject(R.id.tvIsBankCard)
    private TextView tvIsBankCard;
    @ViewInject(R.id.tvRiskToleranc)
    private TextView tvRiskToleranc;
    @ViewInject(R.id.viTopMargin)
    private View viTopMargin;

    private GeneralDialog exitDialog;
    private Context context = PersonalSettingsActivity.this;
 //   private FingerprintManager manager;
//    private KeyguardManager mKeyManager;
    private View popView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_settings);
        popView = getLayoutInflater().inflate(R.layout.popupwindow_hk_register_new, null, false);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.PersonalSettings), toptitleView, true);
        initView();
       /* manager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);
        mKeyManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);*/

    }

    private void initView() {
        String str = getResources().getString(R.string.risk_assessment_tips);
        int a = str.indexOf("。");
        //创建Spannablestring
        SpannableString spannableString = new SpannableString(str);
        //对文本的中间部分设置点击事件
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent4 = new Intent(context, WebViewActivity.class);
                intent4.putExtra(ParamsKeys.TYPE, ParamsValues.QUESTION_NAIRE);
                intent4.putExtra(ParamsKeys.USER_ID,StaticMembers.USER_ID);
                startActivity(intent4);
            }
        }, a + 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体背景颜色
        ForegroundColorSpan  foregroundColorSpan  = new ForegroundColorSpan(getResources().getColor(R.color.theme_color));
        spannableString.setSpan(foregroundColorSpan ,a + 1, str.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRiskAssessmentTips.setText(spannableString);
        //设置，点击后成功跳转
        tvRiskAssessmentTips.setMovementMethod(LinkMovementMethod.getInstance());

    }


    @Event(value = {R.id.rlPhone, R.id.rlRealNameCertification, R.id.rlBankCard, R.id.tvExitLogin,R.id.rlPasswordManagement, R.id.rlRiskToleranceAssessment, R.id.llAboutWe})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.rlPhone:
                if (StaticMembers.HK_STATUS == 1) {
                    startActivity(new Intent(context, ModifyBindingPhoneActivity.class));
                } else {
                    PopupWindow mPop = AppUtils.createHKPop(popView, context);
                    mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                }
                break;
            //实名认证
            case R.id.rlRealNameCertification:
                if (StaticMembers.HK_STATUS == 1) {
                    startActivity(new Intent(context, ViewRealnameInfoActivity.class));
                } else {
                    PopupWindow mPop = AppUtils.createHKPop(popView, context);
                    mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                }
                break;
            //添加银行卡
            case R.id.rlBankCard:
                if (StaticMembers.HK_STATUS == 1) {
                    if (StaticMembers.BANK_CARD_STATUS == 0) {
                        startActivity(new Intent(context, AddBankCardActivity.class));
                    } else {
                        startActivity(new Intent(context, ViewBankCardActivity.class));
                    }
                } else {
                    PopupWindow mPop = AppUtils.createHKPop(popView, context);
                    mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                }
                break;
            //退出当前账户
            case R.id.tvExitLogin:
                exitDialog = new GeneralDialog(this, true, "提示", "您确定要退出登录吗？", "取消", "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitDialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtils.exitLogin(context);
                        PersonalSettingsActivity.this.finish();
                    }
                });
                break;
            //密码管理
            case R.id.rlPasswordManagement:
                Intent intent = new Intent(context, PasswordManagementActivity.class);
                startActivity(intent);
                break;
            //风险承受能力评估
            case R.id.rlRiskToleranceAssessment:
                Intent intent4 = new Intent(context, WebViewActivity.class);
                intent4.putExtra(ParamsKeys.TYPE, ParamsValues.QUESTION_NAIRE);
                intent4.putExtra(ParamsKeys.USER_ID,StaticMembers.USER_ID);
                startActivity(intent4);
                break;
            //关于爱米
            case R.id.llAboutWe:
                startActivity(new Intent(context, AboutOurActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        tvPhone.setText(AppUtils.getProtectedMobile(StaticMembers.MOBILE));
        if (StaticMembers.HK_STATUS == 1) {
            tvRealNameCertification.setText(getResources().getString(R.string.personal_already_certification));
        } else {
            tvRealNameCertification.setText(getResources().getString(R.string.personal_now_certification));
        }
        if (StaticMembers.BANK_CARD_STATUS == 0) {
            tvIsBankCard.setText("未绑卡");
        } else {
            tvIsBankCard.setText("已绑卡");
        }
        if (StaticMembers.QUESTION_NAIRE_STATUS == 0) {
            tvRiskToleranc.setText("未测评");
            tvRiskAssessmentTips.setVisibility(View.VISIBLE);
            viTopMargin.setVisibility(View.GONE);
        } else {
            tvRiskToleranc.setText("已测评");
            tvRiskAssessmentTips.setVisibility(View.GONE);
            viTopMargin.setVisibility(View.VISIBLE);
        }
    }

}
