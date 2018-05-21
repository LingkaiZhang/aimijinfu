package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.AutoInvestListAdapter;
import com.yuanin.aimifinance.base.BaseListActivity;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.entity.AutoInvestEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IDeleteCallBack;
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
 * 自动投资
 */
public class AutoInvestActivity extends BaseListActivity implements XListView.IXListViewListener {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.lvAuto)
    private XListView lvAuto;
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

    private List<AutoInvestEntity> mList;
    private AutoInvestListAdapter mAdp;
    private boolean isNeedLoadBar = true;
    private GeneralDialog dialog;
    private View popView;
    private PopupWindow morePop;
    private Context context = AutoInvestActivity.this;
    private boolean isFresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_invest);
        popView = getLayoutInflater().inflate(R.layout.popupwindow_auto_right_more, null);
        x.view().inject(this);
        x.view().inject(this, popView);
        super.setListViewFunction(lvAuto, true, false);
        initTopBarWithRandow("自动出借", toptitleView, R.drawable.selector_auto_right_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });
        lvAuto.setXListViewListener(this);
        lvAuto.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                dialog = new GeneralDialog(context, true, "提示", "您确定删除此条委托吗？", "取消", "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        requestDeleteData(mList.get(position - 1), 1);
                    }
                });
                return true;
            }
        });
    }

    private void requestDeleteData(final AutoInvestEntity data, final int status) {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_INVEST);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_AUTO_DELETE);
            obj.put(ParamsKeys.ID, data.getId());
            obj.put(ParamsKeys.STATUS, String.valueOf(status));
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<Object>>() {
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
                        ReturnResultEntity<Object> entity = (ReturnResultEntity<Object>) object;
                        if (entity.isSuccess(context)) {
                            if (status == 1) {
                                mList.remove(data);
                                mAdp.closeSwipe();
                                if (mList.size() == 0) {
                                    mList = null;
                                    lvAuto.setVisibility(View.GONE);
                                    tvNoContent.setVisibility(View.VISIBLE);
                                    llNoNet.setVisibility(View.GONE);
                                }
                            } else {
                                data.setStatus(String.valueOf(status));
                            }
                        }
                        AppUtils.showToast(context, entity.getRemark());
                        mAdp.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure() {
                        AppUtils.showConectFail(context);
                        mAdp.notifyDataSetChanged();
                    }
                }
        );
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mList == null) {
            isNeedLoadBar = true;
            requestData();
        } else {
            lvAuto.autoRefresh();
        }
    }

    @Event(value = {R.id.btnAdd, R.id.btnRefresh, R.id.tvRule, R.id.tvRecord,})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tvRule:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(ParamsKeys.TYPE, ParamsValues.AUTO_INVEST_PROTOCOL);
                startActivity(intent);
                if (morePop != null && morePop.isShowing()) {
                    morePop.dismiss();
                }
                break;
            case R.id.tvRecord:
                startActivity(new Intent(this, AutoInvestRecordActivity.class));
                if (morePop != null && morePop.isShowing()) {
                    morePop.dismiss();
                }
                break;
            case R.id.btnAdd:
                if (mList != null && mList.size() >= 5) {
                    AppUtils.showToast(this, "最多只能添加五条自动委托出借！");
                } else {
                    startActivity(new Intent(context, AddAutoInvestActivity.class));
                }
                break;
            case R.id.btnRefresh:
                if (mList != null) {
                    mList = null;
                }
                isNeedLoadBar = true;
                requestData();
                break;
        }
    }

    private void showPop() {
        if (morePop == null) {
            morePop = new PopupWindow(popView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
            morePop.setAnimationStyle(R.style.PopupWindowRightAnimation);
            morePop.setFocusable(false);
            int topY = toptitleView.getHeight() + StaticMembers.STATUS_HEIGHT;
            morePop.showAtLocation(toptitleView, Gravity.RIGHT | Gravity.TOP, AppUtils.dip2px(this, 8), topY);
        } else {
            if (morePop.isShowing()) {
                morePop.dismiss();
            } else {
                int topY = toptitleView.getHeight() + StaticMembers.STATUS_HEIGHT;
                morePop.showAtLocation(toptitleView, Gravity.RIGHT | Gravity.TOP, AppUtils.dip2px(this, 8), topY);
            }
        }
    }

    private void requestData() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_INVEST);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_AUTO_LIST);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<AutoInvestEntity>>() {
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
                        ReturnResultEntity<AutoInvestEntity> entity = (ReturnResultEntity<AutoInvestEntity>) object;
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                if (mList == null) {
                                    mList = entity.getData();
                                    mAdp = new AutoInvestListAdapter(mList, context, new IDeleteCallBack() {
                                        @Override
                                        public void onDelete(Object object, int status) {
                                            requestDeleteData((AutoInvestEntity) object, status);
                                        }
                                    });
                                    lvAuto.setAdapter(mAdp);
                                } else {
                                    mList.addAll(entity.getData());
                                    mAdp.notifyDataSetChanged();
                                }
                                lvAuto.setVisibility(View.VISIBLE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.GONE);
                            } else {
                                if (mList == null) {
                                    lvAuto.setVisibility(View.GONE);
                                    tvNoContent.setVisibility(View.VISIBLE);
                                    llNoNet.setVisibility(View.GONE);
                                } else {
                                    AppUtils.showNoMore(context);
                                }
                            }
                        } else {
                            if (mList == null && !isFresh) {
                                lvAuto.setVisibility(View.GONE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.VISIBLE);
                            } else {
                                AppUtils.showToast(context, entity.getRemark());
                            }
                        }
                    }

                    @Override
                    public void onFailure() {
                        if (mList == null && !isFresh) {
                            lvAuto.setVisibility(View.GONE);
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
    }


    @Override
    public void onRefresh() {
        if (mList != null) {
            mList = null;
        }
        isFresh = true;
        isNeedLoadBar = false;
        requestData();
    }
}
