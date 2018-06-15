package com.yuanin.aimifinance.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.ParamsValues;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import de.greenrobot.event.EventBus;


public class HKRegisterWebActivity extends BaseActivity {
    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.llMain)
    private LinearLayout llMain;

    private Context context = HKRegisterWebActivity.this;
    private GeneralDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hk_register);
        x.view().inject(this);
        String url = getIntent().getStringExtra("url");
        initTopBar("加载中...", toptitleView, true);
        if (url != null && url.length() > 0) {
            AgentWeb mAgentWeb = AgentWeb.with(this)//传入Activity
                    .setAgentWebParent(llMain, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                    .useDefaultIndicator()// 使用默认进度条
                    .defaultProgressBarColor() // 使用默认进度条颜色
                    .setWebViewClient(new WebViewClient() {
                        @Override
                        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                            handler.proceed(); // 接受证书
                        }
                    })
                    .setReceivedTitleCallback(new ChromeClientCallbackManager.ReceivedTitleCallback() {
                        @Override
                        public void onReceivedTitle(WebView view, String title) {
                            initTopBar(title, toptitleView, true);
                        }
                    }) //设置 Web 页面的 title 回调
                    .createAgentWeb()//
                    .ready()
                    .go(url);
            mAgentWeb.getJsInterfaceHolder().addJavaObject("jsObj", getHtmlObject());
        }
    }

    private Object getHtmlObject() {
        Object insertObj = new Object() {
            @JavascriptInterface
            public void HtmlcallJava() {
//                Toast.makeText(SinaPayActivity.this, "接受到调用 ", Toast.LENGTH_SHORT).show();
            }

            @JavascriptInterface
            public void HtmlcallJava2(String param) {
                /*if (param.equals("1")) {
                    //刷新个人中心
                    EventMessage eventMessage = new EventMessage();
                    eventMessage.setType(EventMessage.REFRESH_MINE);
                    EventBus.getDefault().post(eventMessage);
                    AppUtils.showToast(HKRegisterWebActivity.this, "操作成功");
                } else {
                    AppUtils.showToast(HKRegisterWebActivity.this, "操作失败，请重试");
                }*/
                //刷新个人中心
                EventMessage eventMessage = new EventMessage();
                eventMessage.setType(EventMessage.REFRESH_MINE);
                EventBus.getDefault().post(eventMessage);

                if (param.equals("1")) {
                    startActivity(new Intent(context,PayInputMoneyActivity.class));
                } else if (param.equals("2")) {
                    Intent intent2 = new Intent(context, HomePageActivity.class);
                    //通知homepage跳到产品
                    intent2.putExtra("currentIndex",1);
                    startActivity(intent2);
                } else if (param.equals("3")) {
                    contactCustomerService();
                } else if (param.equals("4")) {
                    startActivity(new Intent(context,PayInputMoneyActivity.class));
                } else if (param.equals("5")) {
                    //返回账户

                } else if (param.equals("6")) {
                    Intent intent1 = new Intent(context, FundsWaterActivity.class);
                    intent1.putExtra("currentFundType",2);
                    startActivity(intent1);
                } else if (param.equals("7")) {
                    contactCustomerService();
                } else if (param.equals("8")) {
                    startActivity(new Intent(context,WithdrawActivity.class));
                } else {
                    AppUtils.showToast(context, "操作失败，请重试");
                }
                HKRegisterWebActivity.this.finish();
            }
        };
        return insertObj;
    }

    private void contactCustomerService() {
        dialog = new GeneralDialog(context, true, "联系客服", "客服电话：" + ParamsValues.TEL, "取消", "拨打", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Acp.getInstance(context).request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.CALL_PHONE)
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                //用intent启动拨打电话
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ParamsValues.TEL));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                AppUtils.showToast(context, permissions.toString() + "权限拒绝");
                            }
                        });
            }
        });
    }
}
