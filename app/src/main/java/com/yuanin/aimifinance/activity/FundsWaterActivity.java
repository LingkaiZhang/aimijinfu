package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.ExchangeWaterListAdapter;
import com.yuanin.aimifinance.adapter.PopDownRecycleViewAdapter;
import com.yuanin.aimifinance.base.BaseListActivity;
import com.yuanin.aimifinance.entity.FundsWaterEntity;
import com.yuanin.aimifinance.entity.FundsWaterMapEntity;
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
import java.util.ArrayList;
import java.util.List;


public class FundsWaterActivity extends BaseListActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.llTop)
    private LinearLayout llTop;
    @ViewInject(R.id.llMain)
    private LinearLayout llMain;
    @ViewInject(R.id.tvTime)
    private TextView tvTime;
    @ViewInject(R.id.tvType)
    private TextView tvType;
    @ViewInject(R.id.tvStatus)
    private TextView tvStatus;
    @ViewInject(R.id.lvFundsWater)
    private XListView lvFundsWater;
    @ViewInject(R.id.tvNoContent)
    private TextView tvNoContent;
    @ViewInject(R.id.llNoNet)
    private LinearLayout llNoNet;
    @ViewInject(R.id.viewRemind)
    private View viewRemind;
    @ViewInject(R.id.viewLoading)
    private View viewLoading;
    @ViewInject(R.id.ivTimeArrow)
    private ImageView ivTimeArrow;
    @ViewInject(R.id.ivTypeArrow)
    private ImageView ivTypeArrow;
    @ViewInject(R.id.ivStatusArrow)
    private ImageView ivStatusArrow;
    @ViewInject(R.id.imageLoading)
    private ImageView loadingImage;
    @ViewInject(R.id.tvNoNet)
    private TextView tvNoNet;


    private RecyclerView lvType,lvTime, lvStatus;
    // 页码
    private int PageIndex = 1;
    private List<FundsWaterMapEntity> mMapList;
    private List<FundsWaterEntity> mList;
    private ExchangeWaterListAdapter mAdp;
    private List<String> mTypePopList, mTimePopList, mStatusPopList;
    private PopDownRecycleViewAdapter mTypeAdp, mTimeAdp, mStatusAdp;
    private PopupWindow typePop,timePop, statusPop;
    private View popTypeView, popTimeView, popStatusView;
    private int currentTimeType = 0;
    private int currentFundType = 0;
    private int getCurrentTradeType = 0;
    private int currentStatus = 0;
    private boolean isNeedLoadBar = true;
    private Animation downAnimation;
    private Animation upAnimation;
    private Context context = FundsWaterActivity.this;
    private boolean isFresh = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funds_water);
        popTypeView = getLayoutInflater().inflate(R.layout.popupwindow_down_list_select, null);
        popTimeView = getLayoutInflater().inflate(R.layout.popupwindow_down_list_select, null);
        popStatusView = getLayoutInflater().inflate(R.layout.popupwindow_down_list_select, null);
        initRecycleView();

        x.view().inject(this);
        initTopBar(getResources().getString(R.string.FundsWater), toptitleView, true);
        downAnimation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
        upAnimation = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);
        super.setListViewFunction(lvFundsWater, true, true);
        lvFundsWater.setXListViewListener(this);
        initTypePopData();
        initTimePopData();
        initStatusPopData();
        initListener();

        Intent intent = getIntent();
        if (intent != null) {
            currentStatus = intent.getIntExtra("currentStatus", 0);
            currentFundType = intent.getIntExtra("currentFundType", 0);
            if (currentFundType != 0) {
                tvType.setText(mTypePopList.get(currentFundType));
            }
            if (currentStatus != 0) {
                tvStatus.setText(mStatusPopList.get(currentStatus));
            }
        }

        requestDatas();
    }

    private void initRecycleView() {
        lvType = (RecyclerView) popTypeView.findViewById(R.id.lvDownList);
        lvTime = (RecyclerView) popTimeView.findViewById(R.id.lvDownList);
        lvStatus = (RecyclerView) popStatusView.findViewById(R.id.lvDownList);
        lvType.setLayoutManager(new GridLayoutManager(this,3));
        lvType.setItemAnimator(new DefaultItemAnimator());
        lvTime.setLayoutManager(new GridLayoutManager(this, 3));
        lvTime.setItemAnimator(new DefaultItemAnimator());
        lvStatus.setLayoutManager(new GridLayoutManager(this, 3));
        lvStatus.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onRefresh() {
        clearData();
        PageIndex = 1;
        isNeedLoadBar = false;
        isFresh = true;
        requestDatas();
    }

    @Override
    public void onLoadMore() {
        PageIndex += 1;
        isNeedLoadBar = false;
        requestDatas();
    }

    private void requestDatas() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_FUND);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_FUND_LOG_NEW);
            obj.put(ParamsKeys.PAGE_QTY, String.valueOf(StaticMembers.PAGE_SIZE));
            obj.put(ParamsKeys.CURRENT_PAGE, String.valueOf(PageIndex));
            obj.put(ParamsKeys.TIME_TYPE, String.valueOf(currentTimeType));
            obj.put(ParamsKeys.FUND_TYPE, String.valueOf(currentFundType));
            obj.put(ParamsKeys.STATUS, String.valueOf(currentStatus));
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<FundsWaterMapEntity>>() {
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
                        ReturnResultEntity<FundsWaterMapEntity> entity = (ReturnResultEntity<FundsWaterMapEntity>) object;
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                //count记录总条数
                                int count = 0;
                                for (int i = 0; i < entity.getData().size(); i++) {
                                    count += entity.getData().get(i).getList().size();
                                }
                                if (count < StaticMembers.PAGE_SIZE) {
                                    lvFundsWater.setPullLoadEnable(false);
                                } else {
                                    lvFundsWater.setPullLoadEnable(true);
                                }
                                if (mList == null) {
                                    formatList(entity.getData());
                                    mAdp = new ExchangeWaterListAdapter(context, mList);
                                    lvFundsWater.setAdapter(mAdp);
                                } else {
                                    formatList(entity.getData());
                                    mAdp.notifyDataSetChanged();
                                }
                                lvFundsWater.setVisibility(View.VISIBLE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.GONE);
                            } else {
                                if (mList == null) {
                                    lvFundsWater.setVisibility(View.GONE);
                                    tvNoContent.setVisibility(View.VISIBLE);
                                    llNoNet.setVisibility(View.GONE);
                                } else {
                                    AppUtils.showNoMore(context);
                                }
                                lvFundsWater.setPullLoadEnable(false);
                            }
                        } else {
                            if (PageIndex > 1) {
                                PageIndex -= 1;
                            }
                            if (mList == null && !isFresh) {
                                lvFundsWater.setVisibility(View.GONE);
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
                            lvFundsWater.setVisibility(View.GONE);
                            llNoNet.setVisibility(View.VISIBLE);
                            tvNoContent.setVisibility(View.GONE);
                        } else {
                            AppUtils.showConectFail(context);
                        }
                    }
                }
        );

    }

    private void formatList(List<FundsWaterMapEntity> list) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        if (mMapList == null) {
            mMapList = list;
        } else {
            if (mMapList.get(mMapList.size() - 1).getYear().equals(list.get(0).getYear())) {
                mList.addAll(list.get(0).getList());
                list.remove(0);
            }
            mMapList.addAll(list);
        }
        for (int i = 0; i < list.size(); i++) {
            FundsWaterEntity fundsWaterEntity = new FundsWaterEntity();
            fundsWaterEntity.setStyle(1);
            fundsWaterEntity.setCreated(list.get(i).getYear());
            mList.add(fundsWaterEntity);
            for (int j = 0; j < list.get(i).getList().size(); j++) {
                list.get(i).getList().get(j).setStyle(2);
                mList.add(list.get(i).getList().get(j));
            }
        }
    }

    private void initListener() {

        mTypeAdp.setOnItemClickListener(new PopDownRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mTypeAdp != null) {
                    mTypeAdp.setCurrentSelect(position);
                    mTypeAdp.notifyDataSetChanged();
                }
                String typeStr = String.valueOf(mTypePopList.get(position));
                if (position == 0) {
                    tvType.setText("类型");
                } else {
                    tvType.setText(typeStr);
                }
                if (typePop != null && typePop.isShowing()) {
                    typePop.dismiss();
                }
                clearData();
                currentFundType = position;
                PageIndex = 1;
                requestDatas();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        mTimeAdp.setOnItemClickListener(new PopDownRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mTimeAdp != null) {
                    mTimeAdp.setCurrentSelect(position);
                    mTimeAdp.notifyDataSetChanged();
                }
                String timeStr = String.valueOf(mTimePopList.get(position));
                if (position == 0) {
                    tvTime.setText(getResources().getString(R.string.add_up_earnings_time));
                } else {
                    tvTime.setText(timeStr);
                }
                if (timePop != null && timePop.isShowing()) {
                    timePop.dismiss();
                }
                clearData();
                currentTimeType = position;
                PageIndex = 1;
                requestDatas();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        mStatusAdp.setOnItemClickListener(new PopDownRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mStatusAdp != null) {
                    mStatusAdp.setCurrentSelect(position);
                    mStatusAdp.notifyDataSetChanged();
                }

                String statusStr = String.valueOf(mStatusPopList.get(position));
                if (position == 0) {
                    tvStatus.setText(getResources().getString(R.string.funds_water_status));
                } else {
                    tvStatus.setText(statusStr);
                }
                if (statusPop != null && statusPop.isShowing()) {
                    statusPop.dismiss();
                }
                clearData();
                currentStatus = position;
                PageIndex = 1;
                requestDatas();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

        });
    }

    private void clearData() {
        if (mList != null) {
            mList = null;
        }
        if (mMapList != null) {
            mMapList = null;
        }
        if (mAdp != null) {
            mAdp = null;
        }
    }
    //初始化类型pop
    private void initTypePopData(){
        if (mTypePopList == null){
            mTypePopList = new ArrayList<String>();
            mTypePopList.add("全部");
            mTypePopList.add("充值");
            mTypePopList.add("提现");
            mTypePopList.add("出借本金");
            mTypePopList.add("收益");
            mTypePopList.add("回收本金");
            mTypePopList.add("手续费");
            mTypePopList.add("平台奖励");
            mTypePopList.add("其他");
            mTypeAdp = new PopDownRecycleViewAdapter(mTypePopList, this);
            mTypeAdp.setCurrentSelect(0);
            lvType.setAdapter(mTypeAdp);
        }
    }

    //初始化交易状态pop
    private void initStatusPopData() {
        if (mStatusPopList == null) {
            mStatusPopList = new ArrayList<String>();
            mStatusPopList.add(getResources().getString(R.string.funds_water_all));
            mStatusPopList.add(getResources().getString(R.string.funds_water_finish));
            mStatusPopList.add(getResources().getString(R.string.funds_water_no_finish));
            mStatusPopList.add(getResources().getString(R.string.funds_water_chuli));
            mStatusAdp = new PopDownRecycleViewAdapter(mStatusPopList, this);
            mStatusAdp.setCurrentSelect(0);
            lvStatus.setAdapter(mStatusAdp);
        }
    }

    //初始化时间pop
    private void initTimePopData() {
        if (mTimePopList == null) {
            mTimePopList = new ArrayList<String>();
            mTimePopList.add(getResources().getString(R.string.add_up_earnings_all_2));
            mTimePopList.add(getResources().getString(R.string.add_up_earnings_seven_day));
            mTimePopList.add(getResources().getString(R.string.add_up_earnings_one_month));
            mTimePopList.add(getResources().getString(R.string.add_up_earnings_three_month));
            mTimePopList.add(getResources().getString(R.string.add_up_earnings_six_month));
            mTimePopList.add(getResources().getString(R.string.add_up_earnings_one_year));
            mTimePopList.add(getResources().getString(R.string.add_up_earnings_one_year_more));
            mTimeAdp = new PopDownRecycleViewAdapter(mTimePopList, this);
            mTimeAdp.setCurrentSelect(0);
            lvTime.setAdapter(mTimeAdp);
        }
    }


    @Event(value = {R.id.rlType,R.id.rlTime, R.id.rlStatus, R.id.btnRefresh, R.id.btnCheckNetwork})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //类型
            case R.id.rlType:
                showTypePop();
                break;
            //时间
            case R.id.rlTime:
                showTimePop();
                break;
            //交易状态
            case R.id.rlStatus:
                showStatusPop();
                break;
            //刷新
            case R.id.btnRefresh:
                clearData();
                PageIndex = 1;
                isNeedLoadBar = true;
                requestDatas();
                break;
            //检查网络
            case R.id.btnCheckNetwork:
               /* Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);*/
               AppUtils.checkNetwork(context);
                break;

        }
    }

    private void showTypePop(){
        if (typePop == null) {
            typePop = AppUtils.createPop(popTypeView, R.style.PopupWindowLeftAnimation);
            if (Build.VERSION.SDK_INT >= 24) {
                Rect visibleFrame  = new Rect();
                llTop.getGlobalVisibleRect(visibleFrame);
                int height = llTop.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                typePop.setHeight(height);
                typePop.showAsDropDown(llTop);
            } else {
                typePop.showAsDropDown(llTop);
            }
        } else {
            if (typePop.isShowing()) {
                typePop.dismiss();
            } else {
                if (Build.VERSION.SDK_INT >= 24) {
                    Rect visibleFrame  = new Rect();
                    llTop.getGlobalVisibleRect(visibleFrame);
                    int height = llTop.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                    typePop.setHeight(height);
                    typePop.showAsDropDown(llTop);
                } else {
                    typePop.showAsDropDown(llTop);
                }
            }
        }
        if (timePop != null && timePop.isShowing()) {
            timePop.dismiss();
        }
        if (statusPop != null && statusPop.isShowing()) {
            statusPop.dismiss();
        }

        if (typePop.isShowing()) {
            ivTypeArrow.startAnimation(downAnimation);
        }
        typePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivTypeArrow.startAnimation(upAnimation);
            }
        });
    }


    private void showStatusPop() {
        if (statusPop == null) {
            statusPop = AppUtils.createPop(popStatusView, R.style.PopupWindowCenterAnimation);
            if (Build.VERSION.SDK_INT >= 24) {
                Rect visibleFrame  = new Rect();
                llTop.getGlobalVisibleRect(visibleFrame);
                int height = llTop.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                statusPop.setHeight(height);
                statusPop.showAsDropDown(llTop);
            } else {
                statusPop.showAsDropDown(llTop);
            }
        } else {
            if (statusPop.isShowing()) {
                statusPop.dismiss();
            } else {
                if (Build.VERSION.SDK_INT >= 24) {
                    Rect visibleFrame  = new Rect();
                    llTop.getGlobalVisibleRect(visibleFrame);
                    int height = llTop.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                    statusPop.setHeight(height);
                    statusPop.showAsDropDown(llTop);
                } else {
                    statusPop.showAsDropDown(llTop);
                }
            }
        }
        if (typePop != null && typePop.isShowing()) {
            typePop.dismiss();
        }
        if (timePop != null && timePop.isShowing()) {
            timePop.dismiss();
        }

        if (statusPop.isShowing()) {
            ivStatusArrow.startAnimation(downAnimation);
        }
        statusPop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                ivStatusArrow.startAnimation(upAnimation);
            }
        });
    }

    private void showTimePop() {
        if (timePop == null) {
            timePop = AppUtils.createPop(popTimeView, R.style.PopupWindowRightAnimation);
            if (Build.VERSION.SDK_INT >= 24) {
                Rect visibleFrame  = new Rect();
                llTop.getGlobalVisibleRect(visibleFrame);
                int height = llTop.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                timePop.setHeight(height);
                timePop.showAsDropDown(llTop);
            } else {
                timePop.showAsDropDown(llTop);
            }
        } else {
            if (timePop.isShowing()) {
                timePop.dismiss();
            } else {
                if (Build.VERSION.SDK_INT >= 24) {
                    Rect visibleFrame  = new Rect();
                    llTop.getGlobalVisibleRect(visibleFrame);
                    int height = llTop.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                    timePop.setHeight(height);
                    timePop.showAsDropDown(llTop);
                } else {
                    timePop.showAsDropDown(llTop);
                }
            }
        }
        if (typePop != null && typePop.isShowing()) {
            typePop.dismiss();
        }
        if (statusPop != null && statusPop.isShowing()) {
            statusPop.dismiss();
        }
        if (timePop.isShowing()) {
            ivTimeArrow.startAnimation(downAnimation);
        }
        timePop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                ivTimeArrow.startAnimation(upAnimation);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回键关闭pop
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (statusPop != null && statusPop.isShowing()) {
                statusPop.dismiss();
                return true;
            }
            if (timePop != null && timePop.isShowing()) {
                timePop.dismiss();
                return true;
            }
            if (typePop != null && typePop.isShowing()) {
                typePop.dismiss();
                return true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
