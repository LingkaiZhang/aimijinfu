package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.activity.HKRegisterWebActivity;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.entity.BankEntity;
import com.yuanin.aimifinance.entity.HKRegisterEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.GeneralAdapter;
import com.yuanin.aimifinance.utils.GeneralViewHolder;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/20
 * @time 13:16
 * @desc 银行列表poplist
 */
public class HKBankListAdapter extends GeneralAdapter<BankEntity> {
    private GeneralDialog dialog;
    private JSONObject obj;

    public HKBankListAdapter(List<BankEntity> list, Context context) {
        super(context, list, R.layout.adapter_my_bank_list);
    }

    @Override
    public void convert(GeneralViewHolder holder, BankEntity data) {
        holder.setTextForTextView(R.id.tvBankName, data.getFull_name());
        holder.setTextForTextView(R.id.tvBankCard, data.getCard_no());
        holder.setTextForTextView(R.id.tvPhone, data.getMobile());
        SimpleDraweeView imgView = holder.getView(R.id.imageIcon);
        if (!TextUtils.isEmpty(data.getLogo())) {
            imgView.setImageURI(Uri.parse(data.getLogo()));
        }
        TextView btnUnbinding = holder.getView(R.id.btnUnbinding);
        btnUnbinding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new GeneralDialog(context, true, "提示", "您确定要解绑银行卡吗？", "取消", "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        obj = AppUtils.getPublicJsonObject(true);
                        try {
                            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_SAFE);
                            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_UNBINDING_HK_BANK_CARD);
                            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                            obj.put(ParamsKeys.TOKEN, token);
                            obj.remove(ParamsKeys.KEY);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestUnbindingBankCard();
                    }
                });
            }
        });
        TextView btnModify = holder.getView(R.id.btnModify);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new GeneralDialog(context, true, "提示", "您确定要修改银行预留手机号吗？", "取消", "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        obj = AppUtils.getPublicJsonObject(true);
                        try {
                            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_SAFE);
                            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_CHANGE_BANK_MOBILE);
                            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
                            obj.put(ParamsKeys.TOKEN, token);
                            obj.remove(ParamsKeys.KEY);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestUnbindingBankCard();
                    }
                });
            }
        });
    }

    private void requestUnbindingBankCard() {
        Type mType = new TypeToken<ReturnResultEntity<HKRegisterEntity>>() {
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
                ReturnResultEntity<HKRegisterEntity> entity = (ReturnResultEntity<HKRegisterEntity>) object;
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (entity.isSuccess(context)) {
                    if (entity.isNotNull()) {
                        Intent intent = new Intent(context, HKRegisterWebActivity.class);
                        intent.putExtra("url", entity.getData().get(0).getRedirect_url());
                        context.startActivity(intent);
                    }
                } else {
                    AppUtils.showToast(context, entity.getRemark());
                }
            }

            @Override
            public void onFailure() {
                AppUtils.showConectFail(context);
            }
        });
    }
}
