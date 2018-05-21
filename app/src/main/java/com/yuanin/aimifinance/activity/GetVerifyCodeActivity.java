package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.entity.VerifyEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.view.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;


public class GetVerifyCodeActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.etPhone)
    private ClearEditText etPhone;
    @ViewInject(R.id.btnConfirm)
    private Button btnConfirm;
    @ViewInject(R.id.tvProtocol)
    private TextView tvProtocol;
    @ViewInject(R.id.cbChoose)
    private CheckBox cbChoose;
    @ViewInject(R.id.llPegistration_protocol)
    private View llPegistration_protocol;

    private int where;
    private String type;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_verify_code);
        x.view().inject(this);
        context = getApplicationContext();
        where = getIntent().getIntExtra("where", 0);
        if (where == 1) {
            initTopBar(getResources().getString(R.string.FindPassword), toptitleView, true);
            type = ParamsValues.SEND_FIND_PASSWORD;
            tvProtocol.setVisibility(View.GONE);
            llPegistration_protocol.setVisibility(View.GONE);
        } else if (where == 2) {
            initTopBar(getResources().getString(R.string.Register), toptitleView, true);
            type = ParamsValues.SEND_REGISTER;
            tvProtocol.setVisibility(View.VISIBLE);
//            tvTip.setVisibility(View.VISIBLE);
        }
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPhone.getText().toString().trim().length() != 11) {
                    btnConfirm.setBackgroundResource(R.mipmap.login_button_enable);
                    btnConfirm.setEnabled(false);
                } else {
                    btnConfirm.setBackgroundResource(R.drawable.selector_theme_corner_button);
                    btnConfirm.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPhone.requestFocus();
        AppUtils.openKeyboard(etPhone);
    }

    @Event(value = {R.id.btnConfirm, R.id.tvProtocol})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //获取验证码
            case R.id.btnConfirm:
                if (etPhone.getText().toString().trim().length() != 11) {
                    AppUtils.showToast(this, getResources().getString(R.string.login_input_right_phone));
                    return;
                }
                if (cbChoose.isChecked() == false) {
                    AppUtils.showToast(this,"请点击同意《爱米金服服务协议》");
                    return;
                }
                JSONObject obj = AppUtils.getPublicJsonObject(false);
                try {
                    obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_SMS);
                    obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_SEND);
                    obj.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, etPhone.getText().toString().trim()));
                    obj.put(ParamsKeys.TYPE, type);
                    String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                    obj.put(ParamsKeys.TOKEN, token);
                    obj.remove(ParamsKeys.KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Type mType2 = new TypeToken<ReturnResultEntity<?>>() {
                }.getType();
                NetUtils.request(this, obj, mType2, new IHttpRequestCallBack() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onSuccess(Object object) {
                        ReturnResultEntity<?> entity = (ReturnResultEntity<?>) object;
                        if (entity.isSuccess(context)) {
                            Intent intent = new Intent(context, SetPasswordActivity.class);
                            intent.putExtra("verifyEntity", new VerifyEntity(etPhone.getText().toString().trim(), where));
                            startActivity(intent);
                        }
                        AppUtils.showToast(context, entity.getRemark());
                    }

                    @Override
                    public void onFailure() {
                        AppUtils.showConectFail(context);
                    }
                });
                break;
            case R.id.tvProtocol:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(ParamsKeys.TYPE, ParamsValues.REGISTER_PROTOCOL);
                startActivity(intent);
                break;
        }
    }
}
