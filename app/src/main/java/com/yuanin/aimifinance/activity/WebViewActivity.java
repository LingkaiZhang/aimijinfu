package com.yuanin.aimifinance.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.just.library.AgentWeb;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.entity.InviteFriendTopEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.entity.ShareGridEntity;
import com.yuanin.aimifinance.entity.ShareRedPacketEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.LogUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.StaticMembers;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WebViewActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    //    @ViewInject(R.id.wvHref)
//    private WebView wvHref;
    @ViewInject(R.id.btnBottom)
    private Button btnBottom;
    @ViewInject(R.id.tvNoContent)
    private TextView tvNoContent;
    @ViewInject(R.id.llNoNet)
    private LinearLayout llNoNet;
    @ViewInject(R.id.viewRemind)
    private View viewRemind;
    @ViewInject(R.id.viewLoading)
    private View viewLoading;
    @ViewInject(R.id.mainFL)
    private FrameLayout mainFL;
    @ViewInject(R.id.imageLoading)
    private ImageView loadingImage;
    @ViewInject(R.id.tvNoNet)
    private TextView tvNoNet;

    private String url, type;
    private InviteFriendTopEntity inviteFriendTopEntity;
    private ShareRedPacketEntity shareRedPacketEntity;
    private GeneralDialog dialog;
    private Context context = WebViewActivity.this;
    private String invest_id;
    private AgentWeb agentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        x.view().inject(this);
        viewLoading.setVisibility(View.GONE);
        viewRemind.setVisibility(View.GONE);
        type = getIntent().getStringExtra(ParamsKeys.TYPE);
        if (type != null) {
            if (type.equals(ParamsValues.HELP)) {
                initTopBarWithRightText(getResources().getString(R.string.more_help_center), toptitleView, null, "");
                url = ParamsValues.NET_URL + "html/help.php";
                setUrl(url, new FrameLayout.LayoutParams(-1, -1));
            } else if (type.equals(ParamsValues.BUY_PROTOCOL)) {
                initTopBarWithRightText(getResources().getString(R.string.more_buy_protocol), toptitleView, null, "");
                url = ParamsValues.NET_URL + "html/buyprotocols.php";
                setUrl(url, new FrameLayout.LayoutParams(-1, -1));
            } else if (type.equals(ParamsValues.DATA_REPORT)) {
                initTopBarWithRightText(getResources().getString(R.string.index_data_report), toptitleView, null, "");
                url = ParamsValues.NET_URL + "html/datareport.php";
                setUrl(url, new FrameLayout.LayoutParams(-1, -1));
            } else if (type.equals(ParamsValues.AUTO_INVEST_PROTOCOL)) {
                initTopBarWithRightText("自动投标介绍", toptitleView, null, "");
                url = ParamsValues.NET_URL + "html/wtrule.php";
                setUrl(url, new FrameLayout.LayoutParams(-1, -1));
            } else if (type.equals(ParamsValues.REGISTER_PROTOCOL)) {
                initTopBarWithRightText(getResources().getString(R.string.more_register_protocol), toptitleView, null, "");
                url = ParamsValues.NET_URL + "html/userprotocol.php";
                setUrl(url, new FrameLayout.LayoutParams(-1, -1));
            } else if (type.equals(ParamsValues.SAFE)) {
                initTopBarWithRightText(getResources().getString(R.string.more_safe), toptitleView, null, "");
                url = ParamsValues.NET_URL + "html/security.php";
                setUrl(url, new FrameLayout.LayoutParams(-1, -1));
            } else if (type.equals(ParamsValues.RULE)) {
                initTopBarWithRightText(getResources().getString(R.string.invite_friend), toptitleView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (inviteFriendTopEntity != null) {
                            Intent intent2 = new Intent(WebViewActivity.this, InviteFriendActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("inviteFriendTopEntity", inviteFriendTopEntity);
                            intent2.putExtras(bundle);
                            startActivity(intent2);
                        } else {
                            AppUtils.showToast(context, "加载失败，重试中...");
                            requestData();
                        }
                    }
                }, "邀请列表");
                requestData();
                url = ParamsValues.NET_URL + "html/inviterule.php";
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -1);
                lp.setMargins(0, 0, 0, AppUtils.dip2px(this, 48));
                setUrl(url, lp);
            } else if (type.equals(ParamsValues.ADD_BANK)) {
                initTopBarWithRightText(getResources().getString(R.string.more_add_bank), toptitleView, null, "");
                url = ParamsValues.NET_URL + "html/bandbankcard.php";
                setUrl(url, new FrameLayout.LayoutParams(-1, -1));
            } else if (type.equals(ParamsValues.NOTICE_DETAIL)) {
                initTopBarWithRightText(getResources().getString(R.string.notice_detail), toptitleView, null, "");
                String entityID = getIntent().getStringExtra("entityID");
                url = ParamsValues.NET_URL + "html/noticedetail.php?noticeid=" + entityID;
                setUrl(url, new FrameLayout.LayoutParams(-1, -1));
            } else if (type.equals(ParamsValues.SHARE_RED_PACKET)) {
                initTopBarWithRightText(getResources().getString(R.string.share_red_packet), toptitleView, null, "");
                requestRedPacketData();
            } else if (type.equals(ParamsValues.SHARE_NEW_RED_PACKET)) {
                initTopBarWithRightText(getResources().getString(R.string.share_red_packet), toptitleView, null, "");
                invest_id = getIntent().getStringExtra("invest_id");
                requestNewRedPacketData();
            } else if (type.equals(ParamsValues.QUESTION_NAIRE)) {
                initTopBarWithRightText(getResources().getString(R.string.mine_questionnaire), toptitleView, null, "");
                String user_id = getIntent().getStringExtra(ParamsKeys.USER_ID);
                url = ParamsValues.NET_URL + "survey/showsurvey.php?userid=" + user_id;
                setUrl(url, new FrameLayout.LayoutParams(-1, -1));
            } else if (type.equals(ParamsValues.MODEL_LOAN_CONTRACT)) {
                initTopBarWithRightText(getResources().getString(R.string.model_loan_contract), toptitleView, null, "");
                url = ParamsValues.NET_URL + "html/generic/web/viewer.html?file=pdf/MI20180313005N.pdf";
                setUrl(url, new FrameLayout.LayoutParams(-1, -1));
            } else if (type.equals(ParamsValues.BANK_DEPOSITORY)) {
                initTopBarWithRightText("银行存管", toptitleView, null, "");
                url = ParamsValues.NET_URL + "html/banner_lanmao_online.html";
                setUrl(url,new FrameLayout.LayoutParams(-1, -1));
            }
        }
    }

    private void requestNewRedPacketData() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_OTHER);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_GET_SHARE_RED_INFO_NEW);
            obj.put(ParamsKeys.INVEST_ID, invest_id);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<ShareRedPacketEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, new IHttpRequestCallBack() {
            @Override
            public void onStart() {
                ((Animatable) loadingImage.getDrawable()).start();
                viewRemind.setVisibility(View.GONE);
                viewLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                if (AppUtils.isNetworkConnected(context)) {
                    tvNoNet.setText("o(╯□╰)o请求失败了，请再试一次");
                } else {
                    tvNoNet.setText("o(╯□╰)o网络异常，请检查网络设置");
                }
                ((Animatable) loadingImage.getDrawable()).stop();
                viewRemind.setVisibility(View.VISIBLE);
                viewLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(Object object) {
                final ReturnResultEntity<ShareRedPacketEntity> entity = (ReturnResultEntity<ShareRedPacketEntity>) object;
                if (entity.isSuccess(context)) {
                    if (entity.isNotNull()) {
                        shareRedPacketEntity = entity.getData().get(0);
                        url = shareRedPacketEntity.getRed_html();
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -1);
                        lp.setMargins(0, 0, 0, AppUtils.dip2px(WebViewActivity.this, 48));
                        setUrl(url, lp);
                        btnBottom.setVisibility(View.VISIBLE);
                        btnBottom.setText("立即分享");
                        btnBottom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                List<ShareGridEntity> shareList = new ArrayList<ShareGridEntity>();
                                shareList.add(StaticMembers.SHARE_LIST.get(0));
                                shareList.add(StaticMembers.SHARE_LIST.get(1));
                                InviteFriendTopEntity inviteFriendTopEntity = new InviteFriendTopEntity();
                                inviteFriendTopEntity.setImg(entity.getData().get(0).getImg());
                                inviteFriendTopEntity.setSharedescript(entity.getData().get(0).getSharedescript());
                                inviteFriendTopEntity.setSharelogo(entity.getData().get(0).getSharelogo());
                                inviteFriendTopEntity.setSharetitle(entity.getData().get(0).getSharetitle());
                                inviteFriendTopEntity.setShareurlurl(entity.getData().get(0).getShareurlurl());
                                AppUtils.shareInviteFrient(inviteFriendTopEntity, context, shareList, 2);
                            }
                        });
                        mainFL.setVisibility(View.VISIBLE);
                        tvNoContent.setVisibility(View.GONE);
                        llNoNet.setVisibility(View.GONE);
                    } else {
                        mainFL.setVisibility(View.GONE);
                        tvNoContent.setVisibility(View.VISIBLE);
                        llNoNet.setVisibility(View.GONE);
                    }
                } else {
                    mainFL.setVisibility(View.GONE);
                    tvNoContent.setVisibility(View.GONE);
                    llNoNet.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure() {
                mainFL.setVisibility(View.GONE);
                tvNoContent.setVisibility(View.GONE);
                llNoNet.setVisibility(View.VISIBLE);
            }
        });
    }

    private void requestRedPacketData() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_OTHER);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_GET_SHARE_RED_STATUS);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<ShareRedPacketEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, new IHttpRequestCallBack() {
            @Override
            public void onStart() {
                ((Animatable) loadingImage.getDrawable()).start();
                viewRemind.setVisibility(View.GONE);
                viewLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                if (AppUtils.isNetworkConnected(context)) {
                    tvNoNet.setText("o(╯□╰)o请求失败了，请再试一次");
                } else {
                    tvNoNet.setText("o(╯□╰)o网络异常，请检查网络设置");
                }
                ((Animatable) loadingImage.getDrawable()).stop();
                viewRemind.setVisibility(View.VISIBLE);
                viewLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(Object object) {
                ReturnResultEntity<ShareRedPacketEntity> entity = (ReturnResultEntity<ShareRedPacketEntity>) object;
                if (entity.isSuccess(context)) {
                    if (entity.isNotNull()) {
                        shareRedPacketEntity = entity.getData().get(0);
                        url = shareRedPacketEntity.getRed_html();
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -1);
                        lp.setMargins(0, 0, 0, AppUtils.dip2px(WebViewActivity.this, 48));
                        setUrl(url, lp);
                        switch (shareRedPacketEntity.getStatus()) {
                            case 0:
                                btnBottom.setText(getString(R.string.Immediately_lend));
                                btnBottom.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        WebViewActivity.this.finish();
                                    }
                                });
                                break;
                            case 1:
                                btnBottom.setText("生成红包");
                                btnBottom.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog = new GeneralDialog(context, true, "提示", "在投金额越多，生成的红包金额越多，您确定现在就要生成红包吗？", "取消", "生成", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        }, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                requestPacketInfoData();
                                                dialog.dismiss();
                                            }
                                        });

                                    }
                                });
                                break;
                            case 2:
                                btnBottom.setText("立即分享");
                                btnBottom.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        requestPacketInfoData();
                                    }
                                });
                                break;
                        }
                        mainFL.setVisibility(View.VISIBLE);
                        tvNoContent.setVisibility(View.GONE);
                        llNoNet.setVisibility(View.GONE);
                    } else {
                        mainFL.setVisibility(View.GONE);
                        tvNoContent.setVisibility(View.VISIBLE);
                        llNoNet.setVisibility(View.GONE);
                    }
                } else {
                    mainFL.setVisibility(View.GONE);
                    tvNoContent.setVisibility(View.GONE);
                    llNoNet.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure() {
                mainFL.setVisibility(View.GONE);
                tvNoContent.setVisibility(View.GONE);
                llNoNet.setVisibility(View.VISIBLE);
            }
        });
    }

    private void requestPacketInfoData() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_OTHER);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_GET_SHARE_RED_INFO);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<InviteFriendTopEntity>>() {
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
                        ReturnResultEntity<InviteFriendTopEntity> entity = (ReturnResultEntity<InviteFriendTopEntity>) object;
                        if (entity.isSuccess(context)) {
                            List<ShareGridEntity> shareList = new ArrayList<ShareGridEntity>();
                            shareList.add(StaticMembers.SHARE_LIST.get(0));
                            shareList.add(StaticMembers.SHARE_LIST.get(1));
                            AppUtils.shareInviteFrient(entity.getData().get(0), context, shareList, 2);
                        } else {
                            AppUtils.showToast(context, entity.getRemark());
                        }
                    }

                    @Override
                    public void onFailure() {
                        AppUtils.showConectFail(context);
                    }
                }
        );
    }

    // 点击事件
    @Event(value = {R.id.btnRefresh})
    private void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnRefresh:
                if (type.equals(ParamsValues.RULE)) {
                    requestData();
                } else if (type.equals(ParamsValues.SHARE_RED_PACKET)) {
                    requestRedPacketData();
                } else if (type.equals(ParamsValues.SHARE_NEW_RED_PACKET)) {
                    requestNewRedPacketData();
                }
                break;
        }
    }

    private void requestData() {
        JSONObject obj = AppUtils.getPublicJsonObject(true);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_OTHER);
            obj.put(ParamsKeys.MOTHED, ParamsValues.GET_SHARE_INVITE_FRIENDS);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
            LogUtils.d(this,"obj = " + obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<InviteFriendTopEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, new IHttpRequestCallBack() {
            @Override
            public void onStart() {
                ((Animatable) loadingImage.getDrawable()).start();
                viewRemind.setVisibility(View.GONE);
                viewLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                if (AppUtils.isNetworkConnected(context)) {
                    tvNoNet.setText(context.getResources().getString(R.string.connect_fail));
                } else {
                    tvNoNet.setText(context.getResources().getString(R.string.network_fail));
                }
                ((Animatable) loadingImage.getDrawable()).stop();
                viewRemind.setVisibility(View.VISIBLE);
                viewLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(Object object) {
                ReturnResultEntity<InviteFriendTopEntity> entity = (ReturnResultEntity<InviteFriendTopEntity>) object;
                LogUtils.d(this,"ReturnResultEntity = " + entity.toString());
                LogUtils.d(this,"InviteFriendTopEntity = " + entity.getData().get(0).toString());
                if (entity.isSuccess(context)) {
                    if (entity.isNotNull()) {
                        inviteFriendTopEntity = entity.getData().get(0);
                        btnBottom.setText("邀请好友");
                        btnBottom.setVisibility(View.VISIBLE);
                        btnBottom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AppUtils.shareInviteFrient(inviteFriendTopEntity, context, StaticMembers.SHARE_LIST, 4);
                            }
                        });
                        mainFL.setVisibility(View.VISIBLE);
                        tvNoContent.setVisibility(View.GONE);
                        llNoNet.setVisibility(View.GONE);
                    } else {
                        mainFL.setVisibility(View.GONE);
                        tvNoContent.setVisibility(View.VISIBLE);
                        llNoNet.setVisibility(View.GONE);
                    }
                } else {
                    mainFL.setVisibility(View.GONE);
                    tvNoContent.setVisibility(View.GONE);
                    llNoNet.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure() {
                mainFL.setVisibility(View.GONE);
                tvNoContent.setVisibility(View.GONE);
                llNoNet.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setUrl(String url, FrameLayout.LayoutParams lp) {
        //传入Activity
//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
// 使用默认进度条
// 使用默认进度条颜色
// 接受证书
//
        agentWeb = AgentWeb.with(WebViewActivity.this)//传入Activity
                .setAgentWebParent(mainFL, lp)//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .setWebViewClient(new WebViewClient() {
                    @Override
                    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                        handler.proceed(); // 接受证书
                    }
                })
                .createAgentWeb()//
                .ready()
                .go(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (agentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
