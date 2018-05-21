package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
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
 * 修改登录密码
 */
public class ModifyLoginPasswordActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.etOriginalPwd)
    private EditText etOriginalPwd;
    @ViewInject(R.id.etNewPwd)
    private EditText etNewPwd;
    @ViewInject(R.id.etNewPwdAgain)
    private EditText etNewPwdAgain;

    private Context context = ModifyLoginPasswordActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_login_password);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.ModifyLoginPassword), toptitleView, true);
    }

    @Event(value = {R.id.btnConfirm})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //确定
            case R.id.btnConfirm:
                if (etOriginalPwd.getText().toString().trim().length() < 6 || etNewPwd.getText().toString().trim().length() > 16) {
                    AppUtils.showToast(this, getResources().getString(R.string.modify_login_input_right_pwd_old));
                    return;
                }
                if (etNewPwd.getText().toString().trim().length() < 6 || etNewPwd.getText().toString().trim().length() > 16) {
                    AppUtils.showToast(this, getResources().getString(R.string.modify_login_input_right_pwd_new));
                    return;
                }
                if (!etNewPwd.getText().toString().trim().equals(etNewPwdAgain.getText().toString().trim())) {
                    AppUtils.showToast(this, getResources().getString(R.string.find_input_pwd_not_same));
                    return;
                }
                JSONObject obj = AppUtils.getPublicJsonObject(true);
                try {
                    obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_USER);
                    obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_UPDATE_PASSWORD);
                    obj.put(ParamsKeys.NEW_PASSWORD, AppUtils.rsaEncode(this, etNewPwd.getText().toString().trim()));
                    obj.put(ParamsKeys.OLD_PASSWORD, AppUtils.rsaEncode(this, etOriginalPwd.getText().toString().trim()));
                    String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                    obj.put(ParamsKeys.TOKEN, token);
                    obj.remove(ParamsKeys.KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Type mType = new TypeToken<ReturnResultEntity<?>>() {
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
                        ReturnResultEntity<?> entity = (ReturnResultEntity<?>) object;
                        if (entity.isSuccess(context)) {
                            ModifyLoginPasswordActivity.this.finish();
                        }
                        AppUtils.showToast(context, entity.getRemark());
                    }

                    @Override
                    public void onFailure() {
                        AppUtils.showConectFail(context);
                    }
                });
                break;
        }
    }
}
