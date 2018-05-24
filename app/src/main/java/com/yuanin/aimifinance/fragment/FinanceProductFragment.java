package com.yuanin.aimifinance.fragment;


import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.FinanceProductListAdapter;
import com.yuanin.aimifinance.base.BaseFragment;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.entity.FinanceProductEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.LogUtils;
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

import de.greenrobot.event.EventBus;

/**
 * 理财产品
 */
public class FinanceProductFragment extends BaseFragment implements XListView.IXListViewListener {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.lvProduct)
    private XListView lvProduct;
    @ViewInject(R.id.flMain)
    private FrameLayout flMain;
    @ViewInject(R.id.tvNoContent)
    private TextView tvNoContent;
    @ViewInject(R.id.llNoNet)
    private LinearLayout llNoNet;
    @ViewInject(R.id.viewRemind)
    private View viewRemind;
    @ViewInject(R.id.viewLoading)
    private View viewLoading;
    @ViewInject(R.id.tvRegular)
    private TextView tvRegular;
    @ViewInject(R.id.tvLittle)
    private TextView tvLittle;
    @ViewInject(R.id.imageLoading)
    private ImageView loadingImage;
    @ViewInject(R.id.tvNoNet)
    private TextView tvNoNet;
    @ViewInject(R.id.ivToTop)
    private ImageView ivToTop;


    private boolean isNeedLoadBar = true;
    private List<FinanceProductEntity> mList;
    private FinanceProductListAdapter mAdp;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finance_product, container, false);
        x.view().inject(this, view);
        initTopBar("产品", toptitleView, false);
        //透明状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv_title.getLayoutParams();
//            lp.setMargins(0, StaticMembers.STATUS_HEIGHT, 0, 0);
//            tv_title.setLayoutParams(lp);
//        }
        isPrepared = true;
        lvProduct.setPullRefreshEnable(true);
        lvProduct.setPullLoadEnable(true);
        lvProduct.setXListViewListener(this);
        EventBus.getDefault().register(this);
        lvProduct.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 2 && firstVisibleItem < 6) {
                    tvRegular.setVisibility(View.VISIBLE);
                } else {
                    tvRegular.setVisibility(View.GONE);
                }
                if (firstVisibleItem >= 6) {
                    tvLittle.setVisibility(View.VISIBLE);
                } else {
                    tvLittle.setVisibility(View.GONE);
                }
                if (firstVisibleItem >= 3) {
                    ivToTop.setVisibility(View.VISIBLE);
                } else {
                    ivToTop.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    @Event(value = {R.id.btnRefresh, R.id.ivToTop})
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
            case R.id.ivToTop:
                lvProduct.setSelection(0);
                tvRegular.setVisibility(View.GONE);
                tvLittle.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mList != null) {
                lvProduct.setSelection(0);
                lvProduct.autoRefresh();
                tvRegular.setVisibility(View.GONE);
                tvLittle.setVisibility(View.GONE);
            } else {
                isNeedLoadBar = true;
                PageIndex = 1;
                hasLoadedOnce = false;
                requestDatas();
            }
        }
    }

    public void onEventMainThread(EventMessage eventMessage) {
        if (eventMessage != null && eventMessage.getType() == EventMessage.REFRESH_FINANCE_PRODUCT) {
            if (mList != null) {
                mList = null;
            }
            PageIndex = 1;
            hasLoadedOnce = false;
            isNeedLoadBar = true;
            requestDatas();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void requestDatas() {
        if (!isPrepared || hasLoadedOnce || !isVisible) {
            return;
        }
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_PRODUCT);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_GET_NEW_PRODUCT);
            obj.put(ParamsKeys.PAGE_QTY, String.valueOf(StaticMembers.PAGE_SIZE));
            obj.put(ParamsKeys.CURRENT_PAGE, String.valueOf(PageIndex));
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<FinanceProductEntity>>() {
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
                        ReturnResultEntity<FinanceProductEntity> entity = (ReturnResultEntity<FinanceProductEntity>) object;
                        if (entity.isSuccess(getActivity())) {
                            if (entity.isNotNull()) {
                                hasLoadedOnce = true;
                                if (mList == null) {
                                    StaticMembers.aCache.put(ParamsKeys.PRODUCT_ENTITY, entity);
                                    mList = entity.getData();
                                //    setTitleData();
                                    mAdp = new FinanceProductListAdapter(getActivity(), mList);
                                    lvProduct.setAdapter(mAdp);
                                } else {
                                    mList.addAll(entity.getData());
                                    mAdp.notifyDataSetChanged();
                                }
                                if (entity.getData().size() < StaticMembers.PAGE_SIZE) {
                                    lvProduct.setPullLoadEnable(false);
                                } else {
                                    lvProduct.setPullLoadEnable(true);
                                }
                                flMain.setVisibility(View.VISIBLE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.GONE);
                            } else {
                                if (mList == null) {
                                    flMain.setVisibility(View.GONE);
                                    tvNoContent.setVisibility(View.VISIBLE);
                                    llNoNet.setVisibility(View.GONE);
                                } else {
                                    AppUtils.showNoMore(getActivity());
                                }
                                lvProduct.setPullLoadEnable(false);
                            }
                        } else {
                            if (PageIndex > 1) {
                                PageIndex -= 1;
                            }
                            AppUtils.showToast(getActivity(), entity.getRemark());
                            entity = (ReturnResultEntity<FinanceProductEntity>) AppUtils.fail2SetData(ParamsKeys.PRODUCT_ENTITY);
                            if (entity != null) {
                                if (mList == null) {
                                    mList = entity.getData();
                                    setTitleData();
                                    mAdp = new FinanceProductListAdapter(getActivity(), mList);
                                    lvProduct.setAdapter(mAdp);
                                    flMain.setVisibility(View.VISIBLE);
                                    tvNoContent.setVisibility(View.GONE);
                                    llNoNet.setVisibility(View.GONE);
                                }
                            } else {
                                if (isNeedLoadBar) {
                                    flMain.setVisibility(View.GONE);
                                    tvNoContent.setVisibility(View.GONE);
                                    llNoNet.setVisibility(View.VISIBLE);
                                } else {
                                    AppUtils.showRequestFail(getActivity());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure() {
                        if (PageIndex > 1) {
                            PageIndex -= 1;
                        }
                        ReturnResultEntity<FinanceProductEntity> entity = (ReturnResultEntity<FinanceProductEntity>) AppUtils.fail2SetData(ParamsKeys.PRODUCT_ENTITY);
                        if (entity != null) {
                            if (mList == null) {
                                mList = entity.getData();
                                setTitleData();
                                mAdp = new FinanceProductListAdapter(getActivity(), mList);
                                lvProduct.setAdapter(mAdp);
                                flMain.setVisibility(View.VISIBLE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.GONE);
                            }
                            AppUtils.showConectFail(getActivity());
                        } else {
                            if (isNeedLoadBar) {
                                flMain.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.VISIBLE);
                                tvNoContent.setVisibility(View.GONE);
                            } else {
                                AppUtils.showConectFail(getActivity());
                            }
                        }
                    }
                }
        );
    }

    private void setTitleData() {
        FinanceProductEntity entity1 = new FinanceProductEntity();
        entity1.setStyle(1);
        entity1.setTerm("爱米定期");
        entity1.setType(R.mipmap.finance_regular);
        FinanceProductEntity entity2 = new FinanceProductEntity();
        entity2.setStyle(1);
        entity2.setTerm("爱米优选");
        entity2.setType(R.mipmap.finance_good);
        mList.add(4, entity2);
        mList.add(0, entity1);
    }

    private void loadComplete() {
        lvProduct.stopRefresh();
        lvProduct.stopLoadMore();
        lvProduct.setRefreshTime(new SimpleDateFormat(StaticMembers.DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
    }

    @Override
    public void onLoadMore() {
        PageIndex += 1;
        hasLoadedOnce = false;
        isNeedLoadBar = false;
        requestDatas();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible) {
            lvProduct.setSelection(0);
            lvProduct.autoRefresh();
            tvRegular.setVisibility(View.GONE);
            tvLittle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        if (mList != null) {
            mList = null;
            isNeedLoadBar = false;
        } else {
            isNeedLoadBar = true;
        }
        PageIndex = 1;
        hasLoadedOnce = false;
        requestDatas();
    }
}
