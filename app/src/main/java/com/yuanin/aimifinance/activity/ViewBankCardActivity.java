package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.HKBankListAdapter;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.BankEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.LogUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

public class ViewBankCardActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.lvBankCard)
    private ListView lvBankCard;
    @ViewInject(R.id.tvNoContent)
    private TextView tvNoContent;
    @ViewInject(R.id.llNoNet)
    private LinearLayout llNoNet;
    @ViewInject(R.id.viewRemind)
    private View viewRemind;
    @ViewInject(R.id.viewLoading)
    private View viewLoading;
    @ViewInject(R.id.rlAddBankCard)
    private RelativeLayout rlAddBankCard;
    @ViewInject(R.id.imageLoading)
    private ImageView loadingImage;
    @ViewInject(R.id.tvNoNet)
    private TextView tvNoNet;

    private HKBankListAdapter mAdp;
    private List<BankEntity> mList;
    private boolean isNeedLoadBar = true;
    private Context context = ViewBankCardActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bank_card);
        x.view().inject(this);
        initTopBar("我的银行卡", toptitleView, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mList != null) {
            mList = null;
            isNeedLoadBar = false;
        } else {
            isNeedLoadBar = true;
        }
        requestData();
    }

    @Event(value = {R.id.btnRefresh, R.id.rlAddBankCard, R.id.tvHelp})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btnRefresh:
                mList = null;
                requestData();
                break;
            case R.id.rlAddBankCard:
                startActivity(new Intent(this, AddBankCardActivity.class));
                break;
            case R.id.tvHelp:
                Intent intent2 = new Intent(this, WebViewActivity.class);
                intent2.putExtra(ParamsKeys.TYPE, ParamsValues.ADD_BANK);
                startActivity(intent2);
                break;
        }
    }

    private void requestData() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_SAFE);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_GET_HK_BANK);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
            LogUtils.d(this,ParamsValues.MOTHED_GET_HK_BANK + ", obj = " + obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<BankEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, new IHttpRequestCallBack() {
                    @Override
                    public void onStart() {
                        if (isNeedLoadBar) {
                            ((Animatable) loadingImage.getDrawable()).start();
                            viewRemind.setVisibility(View.GONE);
                            viewLoading.setVisibility(View.VISIBLE);
                            lvBankCard.setVisibility(View.GONE);
                            rlAddBankCard.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFinish() {
                        if (AppUtils.isNetworkConnected(context)) {
                            tvNoNet.setText(context.getResources().getString(R.string.connect_fail));
                        } else {
                            tvNoNet.setText(context.getResources().getString(R.string.network_fail));
                        }
                        if (isNeedLoadBar) {
                            ((Animatable) loadingImage.getDrawable()).stop();
                            viewRemind.setVisibility(View.VISIBLE);
                            viewLoading.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onSuccess(Object object) {
                        ReturnResultEntity<BankEntity> entity = (ReturnResultEntity<BankEntity>) object;
                        LogUtils.d(this,ParamsValues.MOTHED_GET_HK_BANK + entity.toString());
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                if (mList == null) {
                                    mList = entity.getData();
                                    mAdp = new HKBankListAdapter(mList, context);
                                    lvBankCard.setAdapter(mAdp);
                                    AppUtils.setListViewHeightBasedOnChildren(lvBankCard);
                                } else {
                                    mList.addAll(entity.getData());
                                    mAdp.notifyDataSetChanged();
                                }
                                lvBankCard.setVisibility(View.VISIBLE);
                                rlAddBankCard.setVisibility(View.GONE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.GONE);
                            } else {
                                lvBankCard.setVisibility(View.GONE);
                                tvNoContent.setVisibility(View.GONE);
                                rlAddBankCard.setVisibility(View.VISIBLE);
                                llNoNet.setVisibility(View.GONE);
                            }
                        } else {
                            lvBankCard.setVisibility(View.GONE);
                            rlAddBankCard.setVisibility(View.GONE);
                            tvNoContent.setVisibility(View.GONE);
                            llNoNet.setVisibility(View.VISIBLE);
                            AppUtils.showToast(context, entity.getRemark());
                        }
                    }

                    @Override
                    public void onFailure() {
                        lvBankCard.setVisibility(View.GONE);
                        rlAddBankCard.setVisibility(View.GONE);
                        tvNoContent.setVisibility(View.GONE);
                        llNoNet.setVisibility(View.VISIBLE);
                    }
                }
        );
    }
}
