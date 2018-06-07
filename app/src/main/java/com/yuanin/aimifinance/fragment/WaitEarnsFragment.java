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
import com.yuanin.aimifinance.adapter.AddUpEarningsListAdapter;
import com.yuanin.aimifinance.base.BaseFragment;
import com.yuanin.aimifinance.entity.AddUpEarningsEntity;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 待收利息
 */
public class WaitEarnsFragment extends BaseFragment implements XListView.IXListViewListener {
    @ViewInject(R.id.lvAddUpEarnings)
    private XListView lvAddUpEarnings;
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

    private boolean isNeedLoadBar = true;
    private List<AddUpEarningsEntity> mList;
    private AddUpEarningsListAdapter mAdp;
    private TextView tvWait, tvAlready;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_up_earns, container, false);
        x.view().inject(this, view);
        isPrepared = true;
        lvAddUpEarnings.setPullRefreshEnable(true);
        lvAddUpEarnings.setPullLoadEnable(true);
        lvAddUpEarnings.setXListViewListener(this);
        requestDatas();
        return view;
    }

    @Event(value = {R.id.btnRefresh, R.id.btnCheckNetwork})
    private void onViewClicked(View v) {
        switch (v.getId()) {
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
            //检查网络
            case R.id.btnCheckNetwork:
                AppUtils.checkNetwork(getActivity());
                break;
            default:
                break;
        }
    }

    public void setTextView(TextView tvWait, TextView tvAlready) {
        this.tvWait = tvWait;
        this.tvAlready = tvAlready;
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
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_FUND);
            obj.put(ParamsKeys.MOTHED, ParamsValues.GET_USER_ACCUMULATED_INCOME);
            obj.put(ParamsKeys.PAGE_QTY, String.valueOf(StaticMembers.PAGE_SIZE));
            obj.put(ParamsKeys.CURRENT_PAGE, String.valueOf(PageIndex));
            obj.put(ParamsKeys.TYPE, "0");
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<AddUpEarningsEntity>>() {
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
                        ReturnResultEntity<AddUpEarningsEntity> entity = (ReturnResultEntity<AddUpEarningsEntity>) object;
                        if (entity.isSuccess(getActivity())) {
                            if (entity.isNotNull()) {
                                hasLoadedOnce = true;
                                if (mList == null) {
                                    mList = entity.getData();
                                    mAdp = new AddUpEarningsListAdapter(mList, getActivity());
                                    lvAddUpEarnings.setAdapter(mAdp);
                                    tvAlready.setText(String.valueOf(mList.get(0).getReceivedinterest()));
                                    tvWait.setText(String.valueOf(mList.get(0).getWaitinterest()));
                                } else {
                                    mList.addAll(entity.getData());
                                    mAdp.notifyDataSetChanged();
                                }
                                if (entity.getData().size() < StaticMembers.PAGE_SIZE) {
                                    lvAddUpEarnings.setPullLoadEnable(false);
                                } else {
                                    lvAddUpEarnings.setPullLoadEnable(true);
                                }
                                lvAddUpEarnings.setVisibility(View.VISIBLE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.GONE);
                            } else {
                                if (mList == null) {
                                    lvAddUpEarnings.setVisibility(View.GONE);
                                    tvNoContent.setVisibility(View.VISIBLE);
                                    llNoNet.setVisibility(View.GONE);
                                } else {
                                    AppUtils.showNoMore(getActivity());
                                }
                                lvAddUpEarnings.setPullLoadEnable(false);
                            }
                        } else {
                            if (PageIndex > 1) {
                                PageIndex -= 1;
                            }
                            if (mList == null && !isFresh) {
                                lvAddUpEarnings.setVisibility(View.GONE);
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
                            lvAddUpEarnings.setVisibility(View.GONE);
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
        lvAddUpEarnings.stopRefresh();
        lvAddUpEarnings.stopLoadMore();
        lvAddUpEarnings.setRefreshTime(new SimpleDateFormat(StaticMembers.DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
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
        isFresh = true;
        hasLoadedOnce = false;
        requestDatas();
    }
}
