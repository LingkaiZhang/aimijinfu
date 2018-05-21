package com.yuanin.aimifinance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;

/**
 * 实名认证
 */
public class OpenAccountActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.etName)
    private EditText etName;
    @ViewInject(R.id.etIdCard)
    private EditText etIdCard;
    @ViewInject(R.id.etBankcard)
    private EditText etBankcard;
    @ViewInject(R.id.etPhone)
    private EditText etPhone;

    private GeneralDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_account);
        x.view().inject(this);
        initTopBarWithPhone("开户", toptitleView);
    }

    @Event(value = {R.id.btnConfirm})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //提交
            case R.id.btnConfirm:
                if (etName.getText().toString().trim().length() == 0) {
                    AppUtils.showToast(this, getResources().getString(R.string.certification_input_name));
                    return;
                }
                if (etIdCard.getText().toString().trim().length() != 18 && etIdCard.getText().toString().trim().length() != 15) {
                    AppUtils.showToast(this, getResources().getString(R.string.certification_input_id_card));
                    return;
                }
                if (etBankcard.getText().toString().trim().length() == 0) {
                    AppUtils.showToast(this, "请输入您的银行卡号");
                    return;
                }
                if (etPhone.getText().toString().trim().length() != 11) {
                    AppUtils.showToast(this, getResources().getString(R.string.login_input_right_phone));
                    return;
                }
                JSONObject obj = AppUtils.getPublicJsonObject(true);
                try {
                    obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_SAFE);
                    obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_PERSONAL_REGISTER);
                    obj.put(ParamsKeys.REAL_NAME, AppUtils.rsaEncode(this, etName.getText().toString().trim()));
                    obj.put(ParamsKeys.ID_NUMBER, AppUtils.rsaEncode(this, etIdCard.getText().toString().trim()));
                    obj.put(ParamsKeys.CARD_NO, AppUtils.rsaEncode(this, etBankcard.getText().toString().trim()));
                    obj.put(ParamsKeys.CARD_MOBILE, AppUtils.rsaEncode(this, etPhone.getText().toString().trim()));
                    String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                    obj.put(ParamsKeys.TOKEN, token);
                    obj.remove(ParamsKeys.KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Type mType = new TypeToken<ReturnResultEntity<HKRegisterEntity>>() {
                }.getType();
                NetUtils.request(this, obj, mType, new IHttpRequestCallBack() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onSuccess(Object object) {
                        ReturnResultEntity<HKRegisterEntity> entity = (ReturnResultEntity<HKRegisterEntity>) object;
                        if (entity.isSuccess(OpenAccountActivity.this)) {
                            if (entity.isNotNull()) {
                                Intent intent = new Intent(OpenAccountActivity.this, HKRegisterWebActivity.class);
                                intent.putExtra("url", entity.getData().get(0).getRedirect_url());
                                startActivity(intent);
                            }
                            OpenAccountActivity.this.finish();
                        } else {
                            AppUtils.showToast(OpenAccountActivity.this, entity.getRemark());
                        }
                    }

                    @Override
                    public void onFailure() {
                        AppUtils.showConectFail(OpenAccountActivity.this);
                    }
                });
                break;
        }
    }
}
