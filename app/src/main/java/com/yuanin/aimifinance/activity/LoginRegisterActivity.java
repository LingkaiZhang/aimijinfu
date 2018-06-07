package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.entity.LoginEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.StaticMembers;
import com.yuanin.aimifinance.view.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;

import de.greenrobot.event.EventBus;

public class LoginRegisterActivity extends BaseActivity {

    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.etPhone)
    private ClearEditText etPhone;
    @ViewInject(R.id.btnConfirm)
    private Button btnConfirm;
    @ViewInject(R.id.tvProtocol)
    private TextView tvProtocol;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.loginRegister), toptitleView, true);
        context = getApplicationContext();
        initView();
    }

    private void initView() {
        String mobile = AppUtils.getFromSharedPreferences(this, ParamsKeys.LOGIN_MBOILE_FILE, ParamsKeys.LOGIN_MBOILE);
        etPhone.setText(mobile);
        if (etPhone.getText().toString().trim().length() != 11) {
            btnConfirm.setBackgroundResource(R.mipmap.login_button_enable);
            btnConfirm.setEnabled(false);
        } else {
            btnConfirm.setBackgroundResource(R.drawable.selector_theme_corner_button);
            btnConfirm.setEnabled(true);
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
    private void onViewClicked(View v){
        switch (v.getId()) {
            //点击下一步
            case R.id.btnConfirm:
                if (etPhone.getText().toString().trim().length() != 11) {
                    AppUtils.showToast(this, getResources().getString(R.string.login_input_right_phone));
                    return;
                }
                //请求接口：判断手机号是否是已注册用户
                //跳转注册页面
                /*Intent intent1 = new Intent(this, RegisterOne.class);
                intent1.putExtra("phone", etPhone.getText().toString().trim() );
                startActivity(intent1);*/
                //跳转登录页面
                /*Intent intent2 = new Intent(this, LoginOneActivity.class);
                intent2.putExtra("phone", etPhone.getText().toString().trim());
                startActivity(intent2);*/
                JSONObject obj = AppUtils.getPublicJsonObject(false);
                try {
                    obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_USER);
                    obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_LOGIN);
                    obj.put(ParamsKeys.MOBILE, AppUtils.rsaEncode(this, etPhone.getText().toString().trim()));
                    obj.put(ParamsKeys.PASSWORD, AppUtils.rsaEncode(this,"Aabc1234" ));
                    String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                    obj.put(ParamsKeys.TOKEN, token);
                    obj.remove(ParamsKeys.KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Type mType = new TypeToken<ReturnResultEntity<LoginEntity>>() {
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
                        ReturnResultEntity<LoginEntity> entity = (ReturnResultEntity<LoginEntity>) object;
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                //跳转登录页面
                            Intent intent2 = new Intent(context, LoginOneActivity.class);
                            intent2.putExtra("phone", etPhone.getText().toString().trim());
                            startActivity(intent2);
                            }
                        } else {
                           if (entity.getRemark().equals("抱歉，您输入的手机号不存在！")) {
                               //跳转注册页面
                                Intent intent1 = new Intent(context, RegisterOne.class);
                                intent1.putExtra("phone", etPhone.getText().toString().trim() );
                                startActivity(intent1);
                                LoginRegisterActivity.this.finish();
                           } else {
                               //跳转登录页面
                               Intent intent2 = new Intent(context, LoginOneActivity.class);
                               intent2.putExtra("phone", etPhone.getText().toString().trim());
                               startActivity(intent2);
                               LoginRegisterActivity.this.finish();
                           }
                        }
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
