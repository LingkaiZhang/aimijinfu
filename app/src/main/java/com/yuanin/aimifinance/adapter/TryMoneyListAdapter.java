package com.yuanin.aimifinance.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.entity.RedPacketsEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.inter.INotifyCallBack;
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
 * @time 15:41
 * @desc 体验金list
 */
public class TryMoneyListAdapter extends GeneralAdapter<RedPacketsEntity> {
    private GeneralDialog dialog;
    private INotifyCallBack iNotifyCallBack;

    public TryMoneyListAdapter(List<RedPacketsEntity> list, Context context, INotifyCallBack iNotifyCallBack) {
        super(context, list, R.layout.adapter_try_money);
        this.iNotifyCallBack = iNotifyCallBack;
    }

    @Override
    public void convert(GeneralViewHolder holder, final RedPacketsEntity data) {
        holder.setTextForTextView(R.id.tvMoney, String.valueOf(data.getAmount()));
        LinearLayout rlMain = holder.getView(R.id.rlMain);
        TextView tvTips = holder.getView(R.id.tvTips);
        TextView tvMoney = holder.getView(R.id.tvMoney);
        TextView tvMoneyTitle = holder.getView(R.id.tvMoneyTitle);
        TextView tvUse = holder.getView(R.id.tvUse);
        TextView tvName = holder.getView(R.id.tvName);
        TextView tvStartDateDate = holder.getView(R.id.tvStartDateDate);
        TextView tvTo = holder.getView(R.id.tvTo);
        TextView tvEndDate = holder.getView(R.id.tvEndDate);

        switch (data.getStatus()) {
            //未使用
            case 0:
                tvUse.setText("立即使用");
                rlMain.setBackgroundResource(R.mipmap.red_packets_try_no_use_bg);
                tvMoney.setTextColor(context.getResources().getColor(R.color.try_money_yellow));
                tvMoneyTitle.setTextColor(context.getResources().getColor(R.color.try_money_yellow));
                tvTips.setTextColor(context.getResources().getColor(R.color.text_black));
                tvUse.setTextColor(context.getResources().getColor(R.color.try_money_yellow));
                tvName.setTextColor(context.getResources().getColor(R.color.text_black));
                tvStartDateDate.setTextColor(context.getResources().getColor(R.color.text_black));
                tvTo.setTextColor(context.getResources().getColor(R.color.text_black));
                tvEndDate.setTextColor(context.getResources().getColor(R.color.text_black));
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = new GeneralDialog(context, true, "提示", "您确定要使用体验金吗？", "取消", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                JSONObject obj = AppUtils.getPublicJsonObject(true);
                                try {
                                    obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_PRODUCT);
                                    obj.put(ParamsKeys.MOTHED, ParamsValues.USE_EXPERIENCE_GOLD);
                                    obj.put(ParamsKeys.PRODUCT_ID, data.getId());
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
                                                    dialog.dismiss();
                                                    iNotifyCallBack.onNotify();
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
                });
                break;
            //已使用
            case 1:
                tvUse.setText("已使用");
                rlMain.setBackgroundResource(R.mipmap.red_packets_use_bg);
                tvMoney.setTextColor(context.getResources().getColor(R.color.text_gray));
                tvMoneyTitle.setTextColor(context.getResources().getColor(R.color.text_gray));
                tvTips.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvUse.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvName.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvStartDateDate.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvTo.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvEndDate.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                break;
            //已过期
            case 2:
                tvUse.setText("已过期");
                rlMain.setBackgroundResource(R.mipmap.red_packets_use_bg);
                tvMoney.setTextColor(context.getResources().getColor(R.color.text_gray));
                tvMoneyTitle.setTextColor(context.getResources().getColor(R.color.text_gray));
                tvTips.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvUse.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvName.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvStartDateDate.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvTo.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                tvEndDate.setTextColor(context.getResources().getColor(R.color.text_light_gray));
                break;
        }
        holder.setTextForTextView(R.id.tvName, data.getName());
        holder.setTextForTextView(R.id.tvStartDateDate, data.getStartdate());
        holder.setTextForTextView(R.id.tvEndDate, data.getExpirydate());
    }
}
