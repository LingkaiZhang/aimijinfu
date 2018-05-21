package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.HistoryItemListAdapter;
import com.yuanin.aimifinance.base.BaseListActivity;
import com.yuanin.aimifinance.entity.HistoryItemEntity;
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
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

public class HistoryItemActivity extends BaseListActivity implements XListView.IXListViewListener {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.lvItem)
    private XListView lvItem;
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

    private List<HistoryItemEntity> mList;
    private HistoryItemListAdapter mAdp;
    // 页码
    private int PageIndex = 1;
    private boolean isNeedLoadBar = true;
    private String entityID;
    private Context context = HistoryItemActivity.this;
    private boolean isFresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_item);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.history_item), toptitleView, true);
        super.setListViewFunction(lvItem, true, true);
        lvItem.setXListViewListener(this);
        entityID = getIntent().getStringExtra("entityID");
        requestData();
    }

    private void requestData() {
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_PRODUCT);
            obj.put(ParamsKeys.MOTHED, ParamsValues.PERIOD_PROJECT);
            obj.put(ParamsKeys.PRODUCT_ID, entityID);
            obj.put(ParamsKeys.PAGE_QTY, String.valueOf(StaticMembers.PAGE_SIZE));
            obj.put(ParamsKeys.CURRENT_PAGE, String.valueOf(PageIndex));
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<HistoryItemEntity>>() {
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
                        ReturnResultEntity<HistoryItemEntity> entity = (ReturnResultEntity<HistoryItemEntity>) object;
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                if (mList == null) {
                                    mList = entity.getData();
                                    mAdp = new HistoryItemListAdapter(mList, context);
                                    lvItem.setAdapter(mAdp);
                                } else {
                                    mList.addAll(entity.getData());
                                    mAdp.notifyDataSetChanged();
                                }
                                if (entity.getData().size() < StaticMembers.PAGE_SIZE) {
                                    lvItem.setPullLoadEnable(false);
                                } else {
                                    lvItem.setPullLoadEnable(true);
                                }
                                lvItem.setVisibility(View.VISIBLE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.GONE);
                            } else {
                                if (mList == null) {
                                    lvItem.setVisibility(View.GONE);
                                    tvNoContent.setVisibility(View.VISIBLE);
                                    llNoNet.setVisibility(View.GONE);
                                } else {
                                    AppUtils.showNoMore(context);
                                }
                                lvItem.setPullLoadEnable(false);
                            }
                        } else {
                            if (PageIndex > 1) {
                                PageIndex -= 1;
                            }
                            if (mList == null && !isFresh) {
                                lvItem.setVisibility(View.GONE);
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
                            lvItem.setVisibility(View.GONE);
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
    public void onRefresh() {
        if (mList != null) {
            mList = null;
        }
        PageIndex = 1;
        isNeedLoadBar = false;
        isFresh = true;
        requestData();
    }

    @Override
    public void onLoadMore() {
        PageIndex += 1;
        isNeedLoadBar = false;
        requestData();
    }
}
