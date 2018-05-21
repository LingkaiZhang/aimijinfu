package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.RedPacketsListAdapter;
import com.yuanin.aimifinance.base.BaseListActivity;
import com.yuanin.aimifinance.entity.RedPacketsEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.StaticMembers;
import com.yuanin.aimifinance.view.XListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 累计收益
 */
public class ChooseRedpacketActivity extends BaseListActivity implements XListView.IXListViewListener {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.lvRedpacket)
    private XListView lvRedpacket;
    @ViewInject(R.id.tvNoContent)
    private TextView tvNoContent;
    @ViewInject(R.id.llNoNet)
    private LinearLayout llNoNet;
    @ViewInject(R.id.viewRemind)
    private View viewRemind;
    @ViewInject(R.id.viewLoading)
    private View viewLoading;
    @ViewInject(R.id.imageLoading)
    private ImageView loadingImage;
    @ViewInject(R.id.tvNoNet)
    private TextView tvNoNet;


    // 页码
    private int PageIndex = 1;
    private List<RedPacketsEntity> mList;
    private RedPacketsListAdapter mAdp;
    private boolean isNeedLoadBar = true;
    private String money;
    private Context context = ChooseRedpacketActivity.this;
    private boolean isFresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_redpacket);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.choose_redpackets), toptitleView, true);
        super.setListViewFunction(lvRedpacket, true, true);
        lvRedpacket.setXListViewListener(this);
        money = getIntent().getStringExtra("money");
        requestDatas();
        lvRedpacket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RedPacketsEntity entity = (RedPacketsEntity) parent.getItemAtPosition(position);
                if (entity.getMin_invest_amount() > Integer.parseInt(money)) {
                    AppUtils.showToast(context, getResources().getString(R.string.buy_regular_use_redpacket));
                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("entity", entity);
                    intent.putExtras(bundle);
                    setResult(1, intent);
                    ChooseRedpacketActivity.this.finish();
                }
            }
        });
    }

    @Event(value = {R.id.llAlreadyEarn, R.id.llWaitEarn, R.id.btnRefresh})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //刷新
            case R.id.btnRefresh:
                if (mList != null) {
                    mList = null;
                }
                PageIndex = 1;
                isNeedLoadBar = true;
                requestDatas();
                break;
        }
    }

    private void requestDatas() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_PRODUCT);
            obj.put(ParamsKeys.MOTHED, ParamsValues.GET_GIFT_PRODUCT_LIST);
            obj.put(ParamsKeys.TYPE, "0");
            obj.put(ParamsKeys.STATUS, "1");
            obj.put(ParamsKeys.PAGE_QTY, String.valueOf(StaticMembers.PAGE_SIZE));
            obj.put(ParamsKeys.CURRENT_PAGE, String.valueOf(PageIndex));
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<RedPacketsEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, new IHttpRequestCallBack() {
                    @Override
                    public void onStart() {
                        if (isNeedLoadBar) {
                            ((Animatable) loadingImage.getDrawable()).start();
                            viewRemind.setVisibility(View.GONE);
                            viewLoading.setVisibility(View.VISIBLE);
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
                        } else {
                            loadComplete();
                        }
                    }

                    @Override
                    public void onSuccess(Object object) {
                        ReturnResultEntity<RedPacketsEntity> entity = (ReturnResultEntity<RedPacketsEntity>) object;
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                if (mList == null) {
                                    mList = entity.getData();
                                    mAdp = new RedPacketsListAdapter(mList, context);
                                    lvRedpacket.setAdapter(mAdp);
                                } else {
                                    mList.addAll(entity.getData());
                                    mAdp.notifyDataSetChanged();
                                }
                                if (entity.getData().size() < StaticMembers.PAGE_SIZE) {
                                    lvRedpacket.setPullLoadEnable(false);
                                } else {
                                    lvRedpacket.setPullLoadEnable(true);
                                }
                                lvRedpacket.setVisibility(View.VISIBLE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.GONE);
                            } else {
                                if (mList == null) {
                                    lvRedpacket.setVisibility(View.GONE);
                                    tvNoContent.setVisibility(View.VISIBLE);
                                    llNoNet.setVisibility(View.GONE);
                                } else {
                                    AppUtils.showNoMore(context);
                                }
                                lvRedpacket.setPullLoadEnable(false);
                            }
                        } else {
                            if (PageIndex > 1) {
                                PageIndex -= 1;
                            }
                            if (mList == null && !isFresh) {
                                lvRedpacket.setVisibility(View.GONE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.VISIBLE);
                            } else {
                                AppUtils.showToast(context, entity.getRemark());
                            }
                        }
                    }

                    @Override
                    public void onFailure() {
                        if (PageIndex > 1) {
                            PageIndex -= 1;
                        }
                        if (mList == null && !isFresh) {
                            lvRedpacket.setVisibility(View.GONE);
                            llNoNet.setVisibility(View.VISIBLE);
                            tvNoContent.setVisibility(View.GONE);
                        } else {
                            AppUtils.showConectFail(context);
                        }
                    }
                }
        );
    }

    @Override
    public void onLoadMore() {
        PageIndex += 1;
        isNeedLoadBar = false;
        requestDatas();
    }


    @Override
    public void onRefresh() {
        if (mList != null) {
            mList = null;
        }
        PageIndex = 1;
        isNeedLoadBar = false;
        isFresh = true;
        requestDatas();
    }
}