package com.yuanin.aimifinance.fragment;


import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.RedPacketsListAdapter;
import com.yuanin.aimifinance.base.BaseFragment;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 红包
 */
public class RedPacketsFragment extends BaseFragment implements XListView.IXListViewListener {
    @ViewInject(R.id.lvPlan)
    private XListView lvPlan;
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
    @ViewInject(R.id.tvMore)
    private TextView tvMore;

    private boolean isNeedLoadBar = true;
    private List<RedPacketsEntity> mList;
    private RedPacketsListAdapter mAdp;
    // 页码
    private int PageIndex = 1;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean hasLoadedOnce = false;
    private boolean isFresh = false;
    private int what = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_little_item, container, false);
        x.view().inject(this, view);
        isPrepared = true;
        lvPlan.setPullRefreshEnable(true);
        lvPlan.setPullLoadEnable(true);
        lvPlan.setXListViewListener(this);
        return view;
    }

    @Event(value = {R.id.btnRefresh, R.id.tvMore, R.id.btnCheckNetwork})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //检查网络
            case R.id.btnCheckNetwork:
                AppUtils.checkNetwork(getActivity());
                break;

            //刷新
            case R.id.btnRefresh:
                if (mList != null) {
                    mList = null;
                }
                PageIndex = 1;
                hasLoadedOnce = false;
                isNeedLoadBar = true;
                requestDatas();
                break;
            case R.id.tvMore:
                if (what == 1) {
                    what = 2;
                    tvMore.setText("返回  >>");
                } else {
                    what = 1;
                    tvMore.setText("没有更多券　|　查看无效券  >>");
                }
                lvPlan.autoRefresh();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requestDatas();
    }

    private void requestDatas() {
        if (!isPrepared || !isVisible || hasLoadedOnce) {
            return;
        }
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_PRODUCT);
            obj.put(ParamsKeys.MOTHED, ParamsValues.GET_GIFT_PRODUCT_LIST);
            obj.put(ParamsKeys.TYPE, "0");
            obj.put(ParamsKeys.STATUS, String.valueOf(what));
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
                        if (AppUtils.isNetworkConnected(getActivity())) {
                            tvNoNet.setText(getActivity().getResources().getString(R.string.connect_fail));
                        } else {
                            tvNoNet.setText(getActivity().getResources().getString(R.string.network_fail));
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
                        if (entity.isSuccess(getActivity())) {
                            if (entity.isNotNull()) {
                                hasLoadedOnce = true;
                                if (mList == null) {
                                    mList = entity.getData();
                                    mAdp = new RedPacketsListAdapter(mList, getActivity());
                                    lvPlan.setAdapter(mAdp);
                                } else {
                                    //集合去重
                                    ArrayList<String> newList = new ArrayList<>();
                                    for (int i = 0; i < mList.size(); i++) {
                                        newList.add(mList.get(i).getId());
                                    }
                                    for (int i = 0; i < entity.getData().size(); i++) {
                                        if (!newList.contains(entity.getData().get(i).getId())) {
                                            mList.add(entity.getData().get(i));
                                        }
                                    }
                                    //mList.addAll(entity.getData());
                                    mAdp.notifyDataSetChanged();
                                }
                                if (entity.getData().size() < StaticMembers.PAGE_SIZE) {
                                    lvPlan.setPullLoadEnable(false);
                                } else {
                                    lvPlan.setPullLoadEnable(true);
                                }
                                lvPlan.setVisibility(View.VISIBLE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.GONE);
                            } else {
                                if (mList == null) {
                                    lvPlan.setVisibility(View.GONE);
                                    tvNoContent.setVisibility(View.VISIBLE);
                                    llNoNet.setVisibility(View.GONE);
                                } else {
                                    AppUtils.showNoMore(getActivity());
                                }
                                lvPlan.setPullLoadEnable(false);
                            }
                        } else {
                            if (PageIndex > 1) {
                                PageIndex -= 1;
                            }
                            if (mList == null && !isFresh) {
                                lvPlan.setVisibility(View.GONE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.VISIBLE);
                            } else {
                                AppUtils.showToast(getActivity(), entity.getRemark());
                            }
                        }
                    }

                    @Override
                    public void onFailure() {
                        if (PageIndex > 1) {
                            PageIndex -= 1;
                        }
                        if (mList == null && !isFresh) {
                            lvPlan.setVisibility(View.GONE);
                            llNoNet.setVisibility(View.VISIBLE);
                            tvNoContent.setVisibility(View.GONE);
                        } else {
                            AppUtils.showConectFail(getActivity());
                        }
                    }
                }
        );
    }

    private void loadComplete() {
        lvPlan.stopRefresh();
        lvPlan.stopLoadMore();
        lvPlan.setRefreshTime(new SimpleDateFormat(StaticMembers.DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
    }

    @Override
    public void onLoadMore() {
        PageIndex += 1;
        hasLoadedOnce = false;
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
        hasLoadedOnce = false;
        isFresh = true;
        requestDatas();
    }
}
