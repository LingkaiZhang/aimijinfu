package com.yuanin.aimifinance.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.activity.BuyRegularActivity;
import com.yuanin.aimifinance.activity.CallBackWebActivity;
import com.yuanin.aimifinance.activity.FinanceProductDetailActivity;
import com.yuanin.aimifinance.activity.GetVerifyCodeActivity;
import com.yuanin.aimifinance.activity.HomePageActivity;
import com.yuanin.aimifinance.activity.HrefActivity;
import com.yuanin.aimifinance.activity.LoginActivity;
import com.yuanin.aimifinance.activity.LoginRegisterActivity;
import com.yuanin.aimifinance.activity.MessageCenterActivity;
import com.yuanin.aimifinance.activity.NoticeListActivity;
import com.yuanin.aimifinance.activity.OpenAccountActivity;
import com.yuanin.aimifinance.activity.WebViewActivity;
import com.yuanin.aimifinance.adapter.IndexProductListAdapter;
import com.yuanin.aimifinance.base.BaseFragment;
import com.yuanin.aimifinance.entity.BannerEntity;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.entity.IndexProductEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.CBViewHolderCreator;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.inter.IScrollCallBack;
import com.yuanin.aimifinance.listener.OnItemClickListener;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.FmtMicrometer;
import com.yuanin.aimifinance.utils.ImageHolderView;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.StaticMembers;
import com.yuanin.aimifinance.utils.ViewPagerUtils;
import com.yuanin.aimifinance.view.ConvenientBanner;
import com.yuanin.aimifinance.view.MyListView;
import com.yuanin.aimifinance.view.XScrollView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import anet.channel.util.StringUtils;
import de.greenrobot.event.EventBus;


/**
 * 首页
 */
public class NewIndexFragment extends BaseFragment implements XScrollView.IXScrollViewListener {
    @ViewInject(R.id.refresh_root)
    private XScrollView mPullDownScrollView;
    @ViewInject(R.id.rlTitle)
    private RelativeLayout rlTitle;
    @ViewInject(R.id.cyclicImageView)
    private ConvenientBanner convenientBanner;
    @ViewInject(R.id.lvList)
    private MyListView lvList;
    @ViewInject(R.id.viewRemindIndex)
    private View viewRemind;
    @ViewInject(R.id.viewLoadingIndex)
    private View viewLoading;
    @ViewInject(R.id.flipper)
    private ViewFlipper flipper;
    @ViewInject(R.id.llMain)
    private LinearLayout llMain;
    @ViewInject(R.id.imgRedPackets)
    private ImageView imgRedPackets;
    @ViewInject(R.id.imgLogo)
    private ImageView imgLogo;
    @ViewInject(R.id.imageLoading)
    private ImageView loadingImage;
    @ViewInject(R.id.tvNoNet)
    private TextView tvNoNet;
    @ViewInject(R.id.tvNoContent)
    private TextView tvNoContent;

    @ViewInject(R.id.tvNewRate)
    private TextView tvNewRate;
    @ViewInject(R.id.tvNewAmount)
    private TextView tvNewAmount;
    @ViewInject(R.id.tvNewTime)
    private TextView tvNewTime;
    @ViewInject(R.id.btnNewInvest)
    private Button btnNewInvest;
    @ViewInject(R.id.llNoNet)
    private LinearLayout llNoNet;
    @ViewInject(R.id.llLogin)
    private LinearLayout llLogin;
    @ViewInject(R.id.rlNoLogin)
    private RelativeLayout rlNoLogin;
    @ViewInject(R.id.tvTotalMoney)
    private TextView tvTotalMoney;
    @ViewInject(R.id.llContent)
    private LinearLayout llContent;

    @ViewInject(R.id.interest_rates)
    private TextView interestRates;
    @ViewInject(R.id.ll_new_guidelines)
    private LinearLayout llNewGuidelines;
    @ViewInject(R.id.btn_login_register)
    private Button btnLoginRegister;
    @ViewInject(R.id.iv_new_guidelines)
    private ImageView ivNewGuideLines;
    @ViewInject(R.id.llNewProduct)
    private LinearLayout llNewProduct;

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean hasLoadedOnce2 = false;

    private List<BannerEntity> list;
    private List<BannerEntity> noticeList;
    private View mainView;
    private List<IndexProductEntity> mList;
    private IndexProductListAdapter mAdp;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean hasLoadedOnce = false;
    private boolean isNeedLoad = true;
    private IndexProductEntity indexProductEntity;
    private View popView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pull_scrollview_index, container, false);
        mainView = inflater.inflate(R.layout.fragment_new_index, container, false);
        popView = inflater.inflate(R.layout.popupwindow_hk_register_new, container, false);
        x.view().inject(this, view);
        x.view().inject(this, mainView);
        init();
        initScroll();
        requestBannerData();
        return view;
    }


    @Event(value = {R.id.btnRefresh, R.id.llSafe, R.id.llHelp, R.id.llInvite, R.id.llData, R.id.ivMoreNotice,
            R.id.imgRedPackets, R.id.rlNoLogin, R.id.btnNewInvest, R.id.llBank_depository, R.id.btn_login_register, R.id.btnCheckNetwork, R.id.llNewProduct})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.llBank_depository:
                Intent intent5 = new Intent(getActivity(), WebViewActivity.class);
                intent5.putExtra(ParamsKeys.TYPE,ParamsValues.BANK_DEPOSITORY);
                startActivity(intent5);
                break;
            case R.id.llSafe:
                Intent intent1 = new Intent(getActivity(), WebViewActivity.class);
                intent1.putExtra(ParamsKeys.TYPE, ParamsValues.SAFE);
                startActivity(intent1);
                break;
            case R.id.llHelp:
                Intent intent2 = new Intent(getActivity(), WebViewActivity.class);
                intent2.putExtra(ParamsKeys.TYPE, ParamsValues.HELP);
                startActivity(intent2);
                break;
            case R.id.llInvite:
                if (StaticMembers.IS_NEED_LOGIN) {
                    startActivity(new Intent(getActivity(), LoginRegisterActivity.class));
                } else {
                    Intent intent3 = new Intent(getActivity(), WebViewActivity.class);
                    intent3.putExtra(ParamsKeys.TYPE, ParamsValues.RULE);
                    startActivity(intent3);
                }
                break;
            case R.id.llData:
                Intent intent4 = new Intent(getActivity(), WebViewActivity.class);
                intent4.putExtra(ParamsKeys.TYPE, ParamsValues.DATA_REPORT);
                startActivity(intent4);
                break;
            case R.id.btnRefresh:
                hasLoadedOnce2 = false;
                requestBannerData();
                mPullDownScrollView.autoRefresh();
                break;
            case R.id.btnCheckNetwork:
                AppUtils.checkNetwork(getActivity());
                break;
            case R.id.ivMoreNotice:
                startActivity(new Intent(getActivity(), NoticeListActivity.class));
                break;
            case R.id.btnNewInvest:
                if (StaticMembers.IS_NEED_LOGIN) {
                    startActivity(new Intent(getActivity(), LoginRegisterActivity.class));
                } else if (StaticMembers.HK_STATUS != 1) {
                    //TODO    DFF
                    PopupWindow mPop = AppUtils.createHKPop(popView, getActivity());
                    mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
                }else {
                    StaticMembers.isShowLastItem = false;
                    if (indexProductEntity != null) {
                        if (indexProductEntity.getType() == 2 || indexProductEntity.getType() == 3) {
                            StaticMembers.isShowLastItem = false;
                        } else {
                            StaticMembers.isShowLastItem = true;
                        }
                        Intent intent = new Intent(getActivity(), BuyRegularActivity.class);
                        intent.putExtra("entityID", indexProductEntity.getId());
                        startActivity(intent);
                    }
                }
                break;
            case R.id.imgRedPackets:
                if (StaticMembers.IS_NEED_LOGIN) {
                    startActivity(new Intent(getActivity(), LoginRegisterActivity.class));
                } else {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(ParamsKeys.TYPE, ParamsValues.SHARE_RED_PACKET);
                    startActivity(intent);
                }
                break;
            case R.id.rlNoLogin:
                Intent intent = new Intent(getActivity(), GetVerifyCodeActivity.class);
                intent.putExtra("where", 2);
                startActivity(intent);
                break;
            case R.id.btn_login_register:
                if (btnLoginRegister.getText().toString().equals(getString(R.string.loginRegister))) {
                    Intent intent3 = new Intent(getActivity(), LoginRegisterActivity.class);
                    startActivity(intent3);
                } else if (btnLoginRegister.getText().toString().equals(getString(R.string.openbankaccount))) {
                    getActivity().startActivity(new Intent(getActivity(), OpenAccountActivity.class));
                } else if (btnLoginRegister.getText().toString().equals(getString(R.string.activateAcount))) {
                    //TODO
                } else if (btnLoginRegister.getText().toString().equals(getString(R.string.Immediately_lend))) {
                    final HomePageActivity mainActivity = (HomePageActivity) getActivity();
                    mainActivity.setFragment2Fragment(new HomePageActivity.Fragment2Fragment() {
                        @Override
                        public void switchFragment(ViewPager viewPager) {
                            viewPager.setCurrentItem(1);
                        }
                    });
                    mainActivity.forSkip();
                }
                break;
            case R.id.llNewProduct:
                Intent intent6 = new Intent(getActivity(), FinanceProductDetailActivity.class);
                intent6.putExtra("entityID", indexProductEntity.getId());
                startActivity(intent6);
                break;
        }
    }

    private void init() {
        isPrepared = true;
        //注册EventBus
        EventBus.getDefault().register(this);
        //设置banner尺寸
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) convenientBanner.getLayoutParams();
        lp.width = StaticMembers.SCREEN_WIDTH;
        lp.height = StaticMembers.SCREEN_WIDTH * 11 / 25;
        convenientBanner.setLayoutParams(lp);
        lvList.setFocusable(false);
        initIsLogin();
    }

    //初始化是否登录
    private void initIsLogin() {
        if (StaticMembers.IS_NEED_LOGIN) {
            rlNoLogin.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);
            llNewGuidelines.setVisibility(View.VISIBLE);
            llNewProduct.setVisibility(View.VISIBLE);
            Bitmap newGuidleOne = AppUtils.getBitmap(getActivity(), R.mipmap.new_guide_one);
            ivNewGuideLines.setImageBitmap(newGuidleOne);
            btnLoginRegister.setText(getString(R.string.loginRegister));
        } else {
            rlNoLogin.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);
            tvTotalMoney.setText(StaticMembers.TOTAL_MONEY + "元");

            //是否开户
            String userIsOpenAccount = AppUtils.getFromSharedPreferences(getActivity(), ParamsKeys.USER_INFO_FILE, ParamsKeys.USER_IS_OPEN_ACCOUNT);
            StaticMembers.HK_STATUS = Integer.parseInt(userIsOpenAccount);
            String userIsAbleBuyNewProduct = AppUtils.getFromSharedPreferences(getActivity(), ParamsKeys.USER_INFO_FILE, ParamsKeys.USER_IS_ABLE_BUY_NEW_PRODUCT);

            if (userIsOpenAccount.equals("1") ) {
                //是否购买过新手标
                if (userIsAbleBuyNewProduct.equals("0")) {
                    llNewGuidelines.setVisibility(View.GONE);
                    llNewProduct.setVisibility(View.GONE);
                }else {
                    Bitmap newGuidleThree = AppUtils.getBitmap(getActivity(), R.mipmap.new_guide_three);
                    ivNewGuideLines.setImageBitmap(newGuidleThree);
                    btnLoginRegister.setText(getString(R.string.Immediately_lend));
                    llNewGuidelines.setVisibility(View.VISIBLE);
                    llNewProduct.setVisibility(View.VISIBLE);
                }
            } else {
                if (userIsOpenAccount.equals("0")) {
                    //getActivity().startActivity(new Intent(getActivity(), OpenAccountActivity.class));
                    Bitmap newGuidleTwo = AppUtils.getBitmap(getActivity(), R.mipmap.new_guide_two);
                    ivNewGuideLines.setImageBitmap(newGuidleTwo);
                    btnLoginRegister.setText(getString(R.string.openbankaccount));
                }else if (userIsOpenAccount.equals("2")){
                    btnLoginRegister.setText(getString(R.string.activateAcount));
                }
            }
        }
    }

    //初始化下拉刷新控件
    private void initScroll() {
        mPullDownScrollView.setPullLoadEnable(false);
        mPullDownScrollView.setAutoLoadEnable(true);
        mPullDownScrollView.setIXScrollViewListener(this);
        mPullDownScrollView.setView(mainView);
        mPullDownScrollView.setOnScrollCallBack(new IScrollCallBack() {
            @Override
            public void onNotify(float scrollY) {
                float alpha = 0;
                if (scrollY >= rlTitle.getHeight()) {
                    alpha = 1;
                } else {
                    alpha = scrollY / rlTitle.getHeight();
                }
                rlTitle.setAlpha(alpha);
            }
        });
    }


    //来源于登录成功和购买商品成功刷新页面数据
    public void onEventMainThread(EventMessage eventMessage) {
        if (eventMessage != null) {
            if (eventMessage.getType() == EventMessage.REFRESH_INDEX) {
                isVisible = true;
                mPullDownScrollView.autoRefresh();
            } else if (eventMessage.getType() == EventMessage.UPDATE_INDEX_LOGIN) {
                initIsLogin();
            } else if (eventMessage.getType() == EventMessage.UPDATE_INDEX_TOTAL) {
                tvTotalMoney.setText(String.valueOf(eventMessage.getObject()) + "元");
                initIsLogin();
            } else if (eventMessage.getType() == EventMessage.POPUWINDOWN_INDEXT) {
                //TODO    DFF
                PopupWindow mPop = AppUtils.createHKPop(popView, getActivity());
                mPop.showAtLocation(llMain, Gravity.CENTER, 0, 0);
            }
        }
    }

    private void initImageData() {
        Comparator comp = new Comparator() {
            public int compare(Object o1, Object o2) {
                BannerEntity p1 = (BannerEntity) o1;
                BannerEntity p2 = (BannerEntity) o2;
                if (p1.getSort() < p2.getSort())
                    return -1;
                else if (p1.getSort() == p2.getSort())
                    return 0;
                else if (p1.getSort() > p2.getSort())
                    return 1;
                return 0;
            }
        };
        Collections.sort(list, comp);
        List<String> imgList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            imgList.add(list.get(i).getSrc());
        }
        convenientBanner.setPages(
                new CBViewHolderCreator<ImageHolderView>() {
                    @Override
                    public ImageHolderView createHolder() {
                        return new ImageHolderView();
                    }
                }, imgList);
        if (imgList.size() > 1) {
            //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
            convenientBanner.setPageIndicator(new int[]{R.mipmap.small_indicator, R.mipmap.small_indicator_focused})
                    //设置指示器的方向
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
            convenientBanner.startTurning(3000);
        } else {
            convenientBanner.setManualPageable(false);//设置不能手动影响
        }
        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (list.get(position).getHref().contains("http")) {
                    if (list.get(position).getIs_red() == 1) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(list.get(position).getHref());
                        sb.append("?appid=" + ParamsValues.APP_ID);
                        if (!StaticMembers.IS_NEED_LOGIN) {
                            sb.append("&uid=" + StaticMembers.USER_ID);
                            sb.append("&token=" + AppUtils.getMd5Value(StaticMembers.USER_ID + StaticMembers.MOBILE));
                        }
                        Intent intent = new Intent(getActivity(), CallBackWebActivity.class);
                        intent.putExtra("url", sb.toString());
                        intent.putExtra("baseUrl", list.get(position).getHref());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), HrefActivity.class);
                        intent.putExtra("url", list.get(position).getHref());
                        startActivity(intent);
                    }
                } else {
                    switch (Integer.parseInt(list.get(position).getHref())) {
                        case 1:
                            //通知homepage跳到产品
                            EventMessage eventMessage = new EventMessage();
                            eventMessage.setType(EventMessage.HOMEPAGE_JUMP_TAB);
                            eventMessage.setObject(1);
                            EventBus.getDefault().post(eventMessage);
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }
                }
            }
        });
        //设置翻页的效果，不需要翻页效果可用不设
//        int m = (int) ((Math.random() * 16));
//        convenientBanner.setPageTransformer(listPageTransformer.get(m));    //集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            if (mPullDownScrollView != null) {
//                mPullDownScrollView.autoRefresh();
//            }
//        }
//    }

    private void requestIndexProductDatas() {
        if (!isPrepared || hasLoadedOnce) {
            return;
        }
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_PRODUCT);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_NEW_INDEX_PRODUCT);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<IndexProductEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, new IHttpRequestCallBack() {
                    @Override
                    public void onStart() {
                        if (isNeedLoad) {
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
                        ((Animatable) loadingImage.getDrawable()).stop();
                        viewRemind.setVisibility(View.VISIBLE);
                        viewLoading.setVisibility(View.GONE);
                        loadComplete();
                    }

                    @Override
                    public void onSuccess(Object object) {
                        ReturnResultEntity<IndexProductEntity> entity = (ReturnResultEntity<IndexProductEntity>) object;
                        if (entity.isSuccess(getActivity())) {
                            if (entity.isNotNull()) {
                                hasLoadedOnce = true;
                                if (entity.getData().get(0).getIs_red() == 1) {
                                    imgRedPackets.setVisibility(View.VISIBLE);
                                    imgLogo.setVisibility(View.VISIBLE);
                                    setImgAnimation();
                                } else {
                                    imgRedPackets.setVisibility(View.GONE);
                                    imgLogo.setVisibility(View.GONE);
                                }
                                StaticMembers.aCache.put(ParamsKeys.INDEX__ENTITY, entity);
                                mList = entity.getData();
                                setNewData(mList.get(0));
                                mList.remove(0);
                                mAdp = new IndexProductListAdapter(mList, getActivity());
                                lvList.setAdapter(mAdp);

                                lvList.setVisibility(View.VISIBLE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.GONE);
                            } else {
                                if (mList == null) {
                                    lvList.setVisibility(View.GONE);
                                    tvNoContent.setVisibility(View.VISIBLE);
                                    llNoNet.setVisibility(View.GONE);
                                } else {
                                    AppUtils.showNoMore(getActivity());
                                }
                            }
                            if (mList == null) {
                                setIsLan(0);
                            } else {
                                setIsLan(mList.size());
                            }
                        } else {
                            AppUtils.showToast(getActivity(), entity.getRemark());
                            entity = (ReturnResultEntity<IndexProductEntity>) AppUtils.fail2SetData(ParamsKeys.INDEX__ENTITY);
                            if (entity != null) {
                                if (mList == null) {
                                    mList = entity.getData();
                                    setNewData(mList.get(0));
                                    mList.remove(0);
                                    mAdp = new IndexProductListAdapter(mList, getActivity());
                                    lvList.setAdapter(mAdp);
                                    lvList.setVisibility(View.VISIBLE);
                                    tvNoContent.setVisibility(View.GONE);
                                    llNoNet.setVisibility(View.GONE);
                                }
                            } else {
                                if (isNeedLoad) {
                                    lvList.setVisibility(View.GONE);
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
                        ReturnResultEntity<IndexProductEntity> entity = (ReturnResultEntity<IndexProductEntity>) AppUtils.fail2SetData(ParamsKeys.INDEX__ENTITY);
                        if (entity != null) {
                            if (mList == null) {
                                mList = entity.getData();
                                setNewData(mList.get(0));
                                mList.remove(0);
                                mAdp = new IndexProductListAdapter(mList, getActivity());
                                lvList.setAdapter(mAdp);
                                lvList.setVisibility(View.VISIBLE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.GONE);
                            }
                            AppUtils.showConectFail(getActivity());
                        } else {
                            if (isNeedLoad) {
                                lvList.setVisibility(View.GONE);
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

    private void setNewData(IndexProductEntity data) {
        indexProductEntity = data;
//        tvNewRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(data.getAnnual())));
        tvNewAmount.setText(FmtMicrometer.format6(indexProductEntity.getAmount()) + "元");
        tvNewTime.setText(indexProductEntity.getTerm() + indexProductEntity.getUnit());
        if (indexProductEntity.getIsbuy() == 1) {
            btnNewInvest.setBackgroundResource(R.drawable.selector_index_new_button);
            btnNewInvest.setEnabled(true);
        } else {
            btnNewInvest.setBackgroundResource(R.drawable.selector_index_new_button_unclick);
            btnNewInvest.setEnabled(false);
        }
        btnNewInvest.setText(indexProductEntity.getStatusname());

        if (indexProductEntity.getOrgannual() ==  null || indexProductEntity.getExtannual() == null) {
            tvNewRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(data.getAnnual())));
            interestRates.setVisibility(View.GONE);
        } else {
            if (Double.valueOf(indexProductEntity.getExtannual()) > 0 && Double.valueOf(indexProductEntity.getOrgannual()) > 0 ) {
                interestRates.setText("+" + indexProductEntity.getExtannual() + "%");
                tvNewRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(data.getOrgannual())));
                interestRates.setVisibility(View.VISIBLE);
            } else {
                tvNewRate.setText(AppUtils.formatDouble("#.00", Double.valueOf(data.getAnnual())));
                interestRates.setVisibility(View.GONE);
            }
        }

    }

    private void loadComplete() {
        mPullDownScrollView.stopRefresh();
        mPullDownScrollView.setRefreshTime(new SimpleDateFormat(StaticMembers.DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
    }

    private void setIsLan(int size) {
        int lvHeight = lvList.getMeasuredHeight();
        int mainHeight = llMain.getHeight();
//        int loadingHeight = viewLoading.getHeight();
//        int loadingHeight2 = AppUtils.dip2px(getActivity(), 16);
        if (lvHeight == 0) {
            int llContentHeight = llContent.getHeight();
            mainHeight = llContentHeight + AppUtils.dip2px(getActivity(), 16) + AppUtils.dip2px(getActivity(), 90) * size;
        }
        if (mainHeight >= mPullDownScrollView.getHeight()) {
            mPullDownScrollView.setIsNeedLan(false);
        } else {
            mPullDownScrollView.setIsNeedLan(true);
        }
    }

    private void setImgAnimation() {
        //上下摇摆
        TranslateAnimation alphaAnimation2 = new TranslateAnimation(50f, 50F, 50F, 80F);  //同一个x轴 (开始结束都是50f,所以x轴保存不变)  y轴开始点50f  y轴结束点80f
        alphaAnimation2.setDuration(800);  //设置时间
        alphaAnimation2.setRepeatCount(Animation.INFINITE);  //为重复执行的次数。如果设置为n，则动画将执行n+1次。INFINITE为无限制播放
        alphaAnimation2.setRepeatMode(Animation.REVERSE);  //为动画效果的重复模式，常用的取值如下。RESTART：重新从头开始执行。REVERSE：反方向执行
        imgRedPackets.setAnimation(alphaAnimation2);
        alphaAnimation2.start();
    }

    //请求banner信息
    private void requestBannerData() {
        if (!isPrepared || !isVisible || hasLoadedOnce2) {
            return;
        }
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_OTHER);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_BANNER_NOTICE_NEW);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<BannerEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, 1000 * 15, new IHttpRequestCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Object object) {
                ReturnResultEntity<BannerEntity> entity = (ReturnResultEntity<BannerEntity>) object;
                if (entity.isSuccess(getActivity())) {
                    if (entity.isNotNull()) {
                        hasLoadedOnce2 = true;
                        setNotice(entity);
                        initImageData();
                        StaticMembers.aCache.put(ParamsKeys.BANNER_ENTITY, entity);
                    }
                } else {
                    AppUtils.showToast(getActivity(), entity.getRemark());
                    entity = (ReturnResultEntity<BannerEntity>) AppUtils.fail2SetData(ParamsKeys.BANNER_ENTITY);
                    if (entity != null) {
                        setNotice(entity);
                        initImageData();
                    }
                }
            }

            @Override
            public void onFailure() {
                ReturnResultEntity<BannerEntity> entity = (ReturnResultEntity<BannerEntity>) AppUtils.fail2SetData(ParamsKeys.BANNER_ENTITY);
                if (entity != null) {
                    setNotice(entity);
                    initImageData();
                }
                AppUtils.showConectFail(getActivity());
            }
        });
    }

    private void setNotice(ReturnResultEntity<BannerEntity> entity) {
        if (list != null) {
            list = null;
        }
        list = new ArrayList<>();
        list.addAll(entity.getData());
        if (noticeList != null) {
            noticeList = null;
        }
        noticeList = new ArrayList<>();
        for (int i = 0; i < entity.getData().size(); i++) {
            if (entity.getData().get(i).getSort() == 100) {
                noticeList.add(entity.getData().get(i));
                list.remove(entity.getData().get(i));
            }
        }
        for (int j = 0; j < noticeList.size(); j++) {
            final BannerEntity bannerEntity = noticeList.get(j);
            TextView tv = new TextView(getActivity());
            tv.setSingleLine();
            tv.setEllipsize(TextUtils.TruncateAt.END);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setTextColor(getResources().getColor(R.color.text_gray));
            tv.setTextSize(14);
            tv.setText(bannerEntity.getSrc());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("entityID", bannerEntity.getHref());
                    intent.putExtra(ParamsKeys.TYPE, ParamsValues.NOTICE_DETAIL);
                    startActivity(intent);
                }
            });
            flipper.addView(tv);
        }
        flipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewflipper_push_up_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.viewflipper_push_up_out));
//        flipper.startFlipping();
        //ViewFlipper停止循环显示第一条信息
        flipper.stopFlipping();
    }

    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        if (list != null && list.size() > 1) {
            convenientBanner.startTurning(3000);
        }
        if (isVisible) {
            mPullDownScrollView.autoRefresh();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        hasLoadedOnce = false;
        if (mList == null) {
            isNeedLoad = true;
        } else {
            isNeedLoad = false;
            mList = null;
        }
        requestIndexProductDatas();
        if (!hasLoadedOnce2) {
            requestBannerData();
        }
        mPullDownScrollView.setScrollY(0);
    }

    @Override
    public void onLoadMore() {

    }
}
