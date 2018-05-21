package com.yuanin.aimifinance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.dialog.GeneralSingleButtonDialog;
import com.yuanin.aimifinance.dialog.WheelDialog;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.inter.IWheelSetTextCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;

public class AddAutoInvestActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvRate)
    private TextView tvRate;
    @ViewInject(R.id.tvTime)
    private TextView tvTime;
    @ViewInject(R.id.tvTips)
    private TextView tvTips;
    @ViewInject(R.id.etName)
    private EditText etName;
    @ViewInject(R.id.etMoney)
    private EditText etMoney;
    @ViewInject(R.id.rlSingleNum)
    private RelativeLayout rlSingleNum;
    @ViewInject(R.id.cbAll)
    private CheckBox cbAll;
    @ViewInject(R.id.cbSingle)
    private CheckBox cbSingle;
    @ViewInject(R.id.toggleUse)
    private ToggleButton toggleUse;


    private int investType = 1;
    private int isUseRed = 1;
    private int minRate = 0;
    private int maxRate = 0;
    private int minDate = 0;
    private int maxDate = 0;
    private GeneralSingleButtonDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_auto_invest);
        x.view().inject(this);
        initTopBar("添加自动投资", toptitleView, true);
        //讲光标放在文末
        Editable etext = etName.getText();
        Selection.setSelection(etext, etext.length());
        etName.requestFocus();
        //标记文中个别文字颜色
        String str = tvTips.getText().toString();
        int start = str.indexOf("2小时");
        int end = start + "2小时".length();
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.theme_color)), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvTips.setText(style);
        toggleUse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isUseRed = 1;
                } else {
                    isUseRed = 0;
                }
            }
        });
    }


    @Event(value = {R.id.tvProtocol1, R.id.tvProtocol2, R.id.rlRate, R.id.rlTime, R.id.btnConfirm, R.id.rlAll, R.id.rlSingle, R.id.tvUseRedPacket})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //投资协议
            case R.id.tvProtocol2:
                Intent intent3 = new Intent(this, WebViewActivity.class);
                intent3.putExtra(ParamsKeys.TYPE, ParamsValues.BUY_PROTOCOL);
                startActivity(intent3);
                break;
            //委托协议
            case R.id.tvProtocol1:
                Intent intent2 = new Intent(this, WebViewActivity.class);
                intent2.putExtra(ParamsKeys.TYPE, ParamsValues.AUTO_INVEST_PROTOCOL);
                startActivity(intent2);
                break;
            case R.id.tvUseRedPacket:
                dialog = new GeneralSingleButtonDialog(this, false, "提示"
                        , "系统会为您自动选择符合条件的金额最大的加息红包。", "朕知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.rlAll:
                if (investType != 1) {
                    investType = 1;
                    cbAll.setChecked(true);
                    cbSingle.setChecked(false);
                    rlSingleNum.setVisibility(View.GONE);
                }
                break;
            case R.id.rlSingle:
                if (investType != 0) {
                    investType = 0;
                    cbAll.setChecked(false);
                    cbSingle.setChecked(true);
                    rlSingleNum.setVisibility(View.VISIBLE);
                }
                break;
            //利率
            case R.id.rlRate:
                new WheelDialog(this, 2, "请选择利率区间(%)", "auto_rate_data.xml", 8, new IWheelSetTextCallBack() {
                    @Override
                    public void onNotify(String text) {

                    }
                    @Override
                    public void onNotify(String text, String text2) {
                        if (text.equals(text2)) {
                            if (text.equals("不限")) {
                                tvRate.setText(text);
                                minRate = 0;
                                maxRate = 0;
                            } else {
                                tvRate.setText(text + "%");
                                minRate = Integer.parseInt(text);
                                maxRate = Integer.parseInt(text);
                            }
                        } else {
                            if (text.equals("不限")) {
                                text = "0";
                            }
                            tvRate.setText(text + "%" + "~" + text2 + "%");
                            minRate = Integer.parseInt(text);
                            maxRate = Integer.parseInt(text2);
                        }
                    }

                    @Override
                    public void onNotify(String text, String text2, String text3) {

                    }
                });
                break;
            //时间
            case R.id.rlTime:
                new WheelDialog(this, 2, "请选择期限范围(月)", "auto_time_data.xml", 7, new IWheelSetTextCallBack() {
                    @Override
                    public void onNotify(String text) {

                    }

                    @Override
                    public void onNotify(String text, String text2) {
                        if (text.equals(text2)) {
                            if (text.equals("不限")) {
                                tvTime.setText(text);
                                minDate = 0;
                                maxDate = 0;
                            } else {
                                int day = Integer.parseInt(text) * 30;
                                tvTime.setText(text + "个月  (" + day + "天)");
                                minDate = Integer.parseInt(text);
                                maxDate = Integer.parseInt(text);
                            }
                        } else {
                            if (text.equals("不限")) {
                                text = "0";
                            }
                            int day1 = Integer.parseInt(text) * 30;
                            int day2 = Integer.parseInt(text2) * 30;
                            tvTime.setText(text + "~" + text2 + "个月  (" + day1 + "~" + day2 + "天)");
                            minDate = Integer.parseInt(text);
                            maxDate = Integer.parseInt(text2);
                        }
                    }

                    @Override
                    public void onNotify(String text, String text2, String text3) {

                    }
                });
                break;
            case R.id.btnConfirm:
                if (etName.getText().toString().trim().length() == 0) {
                    AppUtils.showToast(AddAutoInvestActivity.this, "请输入方案名称");
                    etName.requestFocus();
                    AppUtils.openKeyboard(etName);
                    return;
                }

                if (minRate == 0 && maxRate == 0 && minDate == 0 && maxDate == 0) {
                    AppUtils.showToast(AddAutoInvestActivity.this, "请至少选择一个利率区间或者期限区间");
                    return;
                }

                if (investType == 0) {
                    if (etMoney.getText().toString().trim().length() > 0) {
                        int money = Integer.parseInt(etMoney.getText().toString().trim());
                        if (money < 1000) {
                            AppUtils.showToast(AddAutoInvestActivity.this, "请输入大于1000的金额");
                            etMoney.requestFocus();
                            AppUtils.openKeyboard(etMoney);
                            return;
                        }
                    } else {
                        AppUtils.showToast(AddAutoInvestActivity.this, "请输入自动投资金额");
                        etMoney.requestFocus();
                        AppUtils.openKeyboard(etMoney);
                        return;
                    }
                }
                JSONObject obj = AppUtils.getPublicJsonObject(true);
                try {
                    obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_INVEST);
                    obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_AUTO_SAVE);
                    obj.put("remark", etName.getText().toString().trim());
                    obj.put("all_amount", String.valueOf(investType));
                    if (investType == 0) {
                        obj.put("amount", etMoney.getText().toString().trim());
                    }
                    obj.put("min_annual", String.valueOf(minRate));
                    obj.put("max_annual", String.valueOf(maxRate));
                    obj.put("min_unit", String.valueOf(minDate));
                    obj.put("max_unit", String.valueOf(maxDate));
                    obj.put("is_red", String.valueOf(isUseRed));
                    String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                    obj.put(ParamsKeys.TOKEN, token);
                    obj.remove(ParamsKeys.KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Type mType = new TypeToken<ReturnResultEntity<?>>() {
                }.getType();
                NetUtils.request(AddAutoInvestActivity.this, obj, mType, new IHttpRequestCallBack() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onFinish() {

                            }

                            @Override
                            public void onSuccess(Object object) {
                                ReturnResultEntity<?> entity = (ReturnResultEntity<?>) object;
                                if (entity.isSuccess(AddAutoInvestActivity.this)) {
                                    AddAutoInvestActivity.this.finish();
                                }
                                AppUtils.showToast(AddAutoInvestActivity.this, entity.getRemark());
                            }

                            @Override
                            public void onFailure() {
                                AppUtils.showConectFail(AddAutoInvestActivity.this);
                            }
                        }
                );
                break;
        }
    }
}
