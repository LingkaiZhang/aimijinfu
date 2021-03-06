package com.yuanin.aimifinance.fragment;


import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.DebtFinanceProductListAdapter;
import com.yuanin.aimifinance.adapter.FinanceProductListAdapter;
import com.yuanin.aimifinance.base.BaseFragment;
import com.yuanin.aimifinance.entity.DebtFinanceProductEntity;
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

import static com.yuanin.aimifinance.R.mipmap.select_up_red_arrow;

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

    @ViewInject(R.id.tv_sanbiaolist)
    private TextView tvSanbiaolist;
    @ViewInject(R.id.tv_zhuanranglist)
    private TextView tvZhanranglist;
    @ViewInject(R.id.llQueryOrder)
    private LinearLayout llQueryOrder;
    @ViewInject(R.id.tvIssueTime)
    private TextView tvIssueTime;
    @ViewInject(R.id.tvDiscountRate)
    private TextView tvDiscountRate;
    @ViewInject(R.id.tvSurplusTime)
    private TextView tvSurplusTime;
    @ViewInject(R.id.ivDiscountRateUp)
    private ImageView ivDiscountRateUp;
    @ViewInject(R.id.ivDiscountRateDown)
    private ImageView ivDiscountRateDown;
    @ViewInject(R.id.ivSurplusTimeDown)
    private ImageView ivSurplusTimeDown;
    @ViewInject(R.id.ivSurplusTimeUp)
    private ImageView ivSurplusTimeUp;
    @ViewInject(R.id.ivIssueTimeUp)
    private ImageView ivIssueTimeUp;
    @ViewInject(R.id.ivIssueTimeDown)
    private ImageView ivIssueTimeDown;


    private boolean isNeedLoadBar = true;
    private List<FinanceProductEntity> mList;
    private List<FinanceProductEntity> mListDebt;
    private FinanceProductListAdapter mAdp;
    private DebtFinanceProductListAdapter debtFinanceProductListAdapter;
    private  Drawable image_gray_down, image_red_up, image_gray_up, image_red_down ;
    // 页码
    private int PageIndex = 1;
    //是否散标
    private boolean isSanBiao = true;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean hasLoadedOnce = false;
    private View popView;
    //排序类型  1.发布时间 2.折扣系数 3.剩余时间
    private int orderType = 2;
    //排序状态  1.正序 2.倒序
    private int ordereStatus = 2;
    private Drawable image_up_gray;
    private Drawable image_down_gray;
    private Drawable image_up_red;
    private Drawable image_down_red;

    //第一次点击是否正序
    private boolean discountRate = true;
    private boolean surplusTime = true;
    private boolean issueTime = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finance_product, container, false);
        popView = inflater.inflate(R.layout.popupwindow_hk_register_new, container, false);
        x.view().inject(this, view);
        initTopBar("项目", toptitleView, false);
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
        tvSanbiaolist.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvZhanranglist.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        EventBus.getDefault().register(this);
        lvProduct.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 2 && firstVisibleItem < 6) {
                    tvRegular.setVisibility(View.GONE);
                } else {
                    tvRegular.setVisibility(View.GONE);
                }
                if (firstVisibleItem >= 6) {
                    tvLittle.setVisibility(View.GONE);
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
        initImage();
        return view;
    }

    private void initImage() {
        image_up_gray = getActivity().getResources().getDrawable(R.mipmap.select_up_arrow);
        image_down_gray = getActivity().getResources().getDrawable(R.mipmap.select_down_arrow);
        image_up_red = getActivity().getResources().getDrawable(R.mipmap.select_up_red_arrow);
        image_down_red = getActivity().getResources().getDrawable(R.mipmap.select_down_red_arrow);
    }

    @Event(value = {R.id.btnRefresh, R.id.ivToTop, R.id.btnCheckNetwork, R.id.tv_sanbiaolist, R.id.tv_zhuanranglist,
            R.id.tvIssueTime, R.id.tvDiscountRate, R.id.tvSurplusTime })
    private void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tvIssueTime:
                tvIssueTime.setTextColor(getActivity().getResources().getColor(R.color.theme_color));
                tvDiscountRate.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                tvSurplusTime.setTextColor(getActivity().getResources().getColor(R.color.text_gray));

                orderType = 1;

                if (issueTime) {
                    ivIssueTimeUp.setImageDrawable(image_up_red);
                    ivIssueTimeDown.setImageDrawable(image_down_gray);
                    ordereStatus = 1;

                } else {
                    ivIssueTimeUp.setImageDrawable(image_up_gray);
                    ivIssueTimeDown.setImageDrawable(image_down_red);
                    ordereStatus = 2;
                }
                issueTime = !issueTime;
                ivSurplusTimeUp.setImageDrawable(image_up_gray);
                ivSurplusTimeDown.setImageDrawable(image_down_gray);
                ivDiscountRateDown.setImageDrawable(image_down_gray);
                ivDiscountRateUp.setImageDrawable(image_up_gray);


                if (mListDebt != null) {
                    mListDebt = null;
                }
                PageIndex = 1;
                hasLoadedOnce = false;
                isNeedLoadBar = true;
                requestDatasZhaizhuan();
                break;
            case R.id.tvDiscountRate:
                tvIssueTime.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                tvDiscountRate.setTextColor(getActivity().getResources().getColor(R.color.theme_color));
                tvSurplusTime.setTextColor(getActivity().getResources().getColor(R.color.text_gray));

                orderType = 2;

                if (discountRate) {
                    ivDiscountRateUp.setImageDrawable(image_up_red);
                    ivDiscountRateDown.setImageDrawable(image_down_gray);
                    ordereStatus = 1;

                } else {
                    ivDiscountRateUp.setImageDrawable(image_up_gray);
                    ivDiscountRateDown.setImageDrawable(image_down_red);
                    ordereStatus = 2;
                }
                discountRate = !discountRate;
                ivSurplusTimeUp.setImageDrawable(image_up_gray);
                ivSurplusTimeDown.setImageDrawable(image_down_gray);
                ivIssueTimeDown.setImageDrawable(image_down_gray);
                ivIssueTimeUp.setImageDrawable(image_up_gray);


                if (mListDebt != null) {
                    mListDebt = null;
                }
                PageIndex = 1;
                hasLoadedOnce = false;
                isNeedLoadBar = true;
                requestDatasZhaizhuan();
                break;
            case R.id.tvSurplusTime:
                tvIssueTime.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                tvDiscountRate.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
                tvSurplusTime.setTextColor(getActivity().getResources().getColor(R.color.theme_color));

                orderType = 3;

                if (surplusTime) {
                    ivSurplusTimeUp.setImageDrawable(image_up_red);
                    ivSurplusTimeDown.setImageDrawable(image_down_gray);
                    ordereStatus = 1;
                } else {
                    ivSurplusTimeUp.setImageDrawable(image_up_gray);
                    ivSurplusTimeDown.setImageDrawable(image_down_red);
                    ordereStatus = 2;
                }
                surplusTime = !surplusTime;
                ivIssueTimeUp.setImageDrawable(image_up_gray);
                ivIssueTimeDown.setImageDrawable(image_down_gray);
                ivDiscountRateDown.setImageDrawable(image_down_gray);
                ivDiscountRateUp.setImageDrawable(image_up_gray);


                if (mListDebt != null) {
                    mListDebt = null;
                }
                PageIndex = 1;
                hasLoadedOnce = false;
                isNeedLoadBar = true;
                requestDatasZhaizhuan();
                break;
            case R.id.tv_sanbiaolist:
                tvZhanranglist.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvSanbiaolist.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                isSanBiao = true;
                if (mList != null) {
                    mList = null;
                }
                //lvProduct.autoRefresh();
                PageIndex = 1;
                hasLoadedOnce = false;
                isNeedLoadBar = true;
                requestDatas();
                break;
            case R.id.tv_zhuanranglist:
                tvSanbiaolist.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvZhanranglist.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                isSanBiao = false;
                if (mListDebt != null) {
                    mListDebt = null;
                }
                //lvProduct.autoRefresh();
                PageIndex = 1;
                hasLoadedOnce = false;
                isNeedLoadBar = true;
                requestDatasZhaizhuan();
                break;
            //刷新
            case R.id.btnRefresh:
                if (mList != null) {
                    mList = null;
                }
                if (mListDebt != null) {
                    mListDebt = null;
                }
                PageIndex = 1;
                hasLoadedOnce = false;
                isNeedLoadBar = true;
                if (isSanBiao) {
                    requestDatas();
                } else {
                    PageIndex = 1;
                    requestDatasZhaizhuan();
                }
                break;
            //检查网络
            case R.id.btnCheckNetwork:
                AppUtils.checkNetwork(getActivity());
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

    private void requestDatasZhaizhuan() {
        llQueryOrder.setVisibility(View.VISIBLE);
        if (!isPrepared || hasLoadedOnce || !isVisible) {
            return;
        }
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_DEBT);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_GET_BUY_ENTRANSFER_BORROW_LIST);
            obj.put(ParamsKeys.PAGE_QTY, String.valueOf(StaticMembers.PAGE_SIZE));
            obj.put(ParamsKeys.CURRENT_PAGE, String.valueOf(PageIndex));
            obj.put(ParamsKeys.STATUS, "1");
            obj.put(ParamsKeys.ORDER_TYPE, String.valueOf(orderType));
            obj.put(ParamsKeys.ORDER_STATUS, String.valueOf(ordereStatus));
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
                                if (mListDebt == null) {
                                    StaticMembers.aCache.put(ParamsKeys.PRODUCT_ENTITY_DEBT, entity);
                                    mListDebt = entity.getData();
                                    //    setTitleData();
                                    mAdp = new FinanceProductListAdapter(getActivity(), mListDebt);
                                    lvProduct.setAdapter(mAdp);
                                } else {
                                    mListDebt.addAll(entity.getData());
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
                                if (mListDebt == null) {
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
                            entity = (ReturnResultEntity<FinanceProductEntity>) AppUtils.fail2SetData(ParamsKeys.PRODUCT_ENTITY_DEBT);
                            if (entity != null) {
                                if (mListDebt == null) {
                                    mListDebt = entity.getData();
                                    // setTitleData();
                                    mAdp = new FinanceProductListAdapter(getActivity(), mListDebt);
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
                        ReturnResultEntity<FinanceProductEntity> entity = (ReturnResultEntity<FinanceProductEntity>) AppUtils.fail2SetData(ParamsKeys.PRODUCT_ENTITY_DEBT);
                        if (entity != null) {
                            if (mListDebt == null) {
                                mListDebt = entity.getData();
                                //    setTitleData();
                                mAdp = new FinanceProductListAdapter(getActivity(), mListDebt);
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
                if (isSanBiao) {
                    requestDatas();
                } else {
                    mListDebt = null;
                    requestDatasZhaizhuan();
                }
            }
        }
    }

    public void onEventMainThread(EventMessage eventMessage) {
        if (eventMessage != null && eventMessage.getType() == EventMessage.REFRESH_FINANCE_PRODUCT) {
            if (mList != null) {
                mList = null;
            }
            if (mListDebt != null) {
                mListDebt = null;
            }
            PageIndex = 1;
            hasLoadedOnce = false;
            isNeedLoadBar = true;
            if (isSanBiao) {
                requestDatas();
            } else {
                requestDatasZhaizhuan();
            }

        } else if (eventMessage != null && eventMessage.getType() == EventMessage.POPUWINDOWN_FINANCE_PRODUCT) {
            //TODO    DFF
            PopupWindow mPop = AppUtils.createHKPop(popView, getActivity());
            mPop.showAtLocation(flMain, Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void requestDatas() {
        llQueryOrder.setVisibility(View.GONE);
        if (!isPrepared || hasLoadedOnce || !isVisible) {
            return;
        }
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_PRODUCT);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_GET_PRODUCT_LIST);
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
                                   // setTitleData();
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
                            //    setTitleData();
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
        if (isSanBiao) {
            requestDatas();
        } else {
            requestDatasZhaizhuan();
        }
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

        PageIndex = 1;
        hasLoadedOnce = false;
        if (isSanBiao) {
            if (mList != null) {
                mList = null;
                isNeedLoadBar = false;
            } else {
                isNeedLoadBar = true;
            }
            requestDatas();
        } else {
            if (mListDebt != null) {
                mListDebt = null;
                isNeedLoadBar = false;
            } else {
                isNeedLoadBar = true;
            }
            requestDatasZhaizhuan();
        }
    }
}
