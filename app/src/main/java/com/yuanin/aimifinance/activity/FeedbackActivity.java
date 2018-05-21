package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.StaticMembers;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;

public class FeedbackActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvCount)
    private TextView tvCount;
    @ViewInject(R.id.etContent)
    private EditText etContent;
    @ViewInject(R.id.btnSubmit)
    private Button btnSubmit;

    private Context context = FeedbackActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.feedback), toptitleView, true);
        initListener();
    }

    private void initListener() {
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 200) {
                    String str = s.toString().substring(0, 200);
                    etContent.setText(str);
                    tvCount.setText("0字");
                    Editable etext = etContent.getText();
                    Selection.setSelection(etext, etext.length());
                } else {
                    tvCount.setText((200 - s.length()) + "字");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etContent.getText().toString().trim().length() == 0) {
                    AppUtils.showToast(context, getResources().getString(R.string.input_feedback));
                    return;
                }
                JSONObject obj = AppUtils.getPublicJsonObject(false);
                try {
                    obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_OTHER);
                    obj.put(ParamsKeys.MOTHED, ParamsValues.FEEDBACK);
                    if (!StaticMembers.IS_NEED_LOGIN) {
                        obj.put(ParamsKeys.USER_ID, StaticMembers.USER_ID);
                        obj.put(ParamsKeys.MOBILE, StaticMembers.RSA_MOBILE);
                    }
                    obj.put(ParamsKeys.MESSAGE, etContent.getText().toString().trim());
                    String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                    obj.put(ParamsKeys.TOKEN, token);
                    obj.remove(ParamsKeys.KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Type mType = new TypeToken<ReturnResultEntity<?>>() {
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
                                ReturnResultEntity<?> entity = (ReturnResultEntity<?>) object;
                                if (entity.isSuccess(context)) {
                                    FeedbackActivity.this.finish();
                                }
                                AppUtils.showToast(context, entity.getRemark());
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

}
