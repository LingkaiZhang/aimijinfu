package com.yuanin.aimifinance.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.dialog.DownloadDialog;
import com.yuanin.aimifinance.dialog.GeneralDialog;
import com.yuanin.aimifinance.entity.AboutOurEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.entity.VersionUpdateEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 关于我们
 */
public class AboutOurActivity extends BaseActivity {

    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.tvVersion)
    private TextView tvVersion;
    @ViewInject(R.id.tvWebSite)
    private TextView tvWebSite;
    @ViewInject(R.id.tvWeChat)
    private TextView tvWeChat;
    @ViewInject(R.id.tvEmail)
    private TextView tvEmail;
    @ViewInject(R.id.tvServeTel)
    private TextView tvServeTel;
    @ViewInject(R.id.tvCopyright)
    private TextView tvCopyright;
    @ViewInject(R.id.tvAddress)
    private TextView tvAddress;
    @ViewInject(R.id.tvNoContent)
    private TextView tvNoContent;
    @ViewInject(R.id.llNoNet)
    private LinearLayout llNoNet;
    @ViewInject(R.id.svContent)
    private ScrollView svContent;
    @ViewInject(R.id.viewRemind)
    private View viewRemind;
    @ViewInject(R.id.viewLoading)
    private View viewLoading;
    @ViewInject(R.id.imageLoading)
    private ImageView loadingImage;
    @ViewInject(R.id.tvNoNet)
    private TextView tvNoNet;

    private GeneralDialog dialog;
    private DownloadDialog generalDialog;
    private Context context = AboutOurActivity.this;
    private static final int INSTALL_PERMISS_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_our);
        x.view().inject(this);
        initTopBar(getResources().getString(R.string.AboutOur), toptitleView, true);
        requestData();
    }


    @Event(value = {R.id.btnRefresh, R.id.rlSafe, R.id.rlHelp, R.id.rlServe, R.id.rlYijian, R.id.rlWebsite, R.id.rlWeChat, R.id.rlEmail, R.id.rlVersion})
    private void onViewClicked(View v) {
        switch (v.getId()) {
            //刷新
            case R.id.btnRefresh:
                requestData();
                break;
            //安全保障
            case R.id.rlSafe:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(ParamsKeys.TYPE, ParamsValues.SAFE);
                startActivity(intent);
                break;
            //帮助中心
            case R.id.rlHelp:
                Intent intent1 = new Intent(this, WebViewActivity.class);
                intent1.putExtra(ParamsKeys.TYPE, ParamsValues.HELP);
                startActivity(intent1);
                break;
            //联系客服
            case R.id.rlServe:
                dialog = new GeneralDialog(this, true, "联系客服", "客服电话：" + ParamsValues.TEL, "取消", "拨打", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Acp.getInstance(AboutOurActivity.this).request(new AcpOptions.Builder()
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
                                        AppUtils.showToast(AboutOurActivity.this, permissions.toString() + "权限拒绝");
                                    }
                                });
                    }
                });
                break;
            //网址
            case R.id.rlWebsite:
                ClipboardManager cmb1 = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cmb1.setText(tvWebSite.getText().toString());
                AppUtils.showToast(context, "复制成功");
                break;
            //微信
            case R.id.rlWeChat:
                ClipboardManager cmb2 = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cmb2.setText(tvWeChat.getText().toString());
                AppUtils.showToast(context, "复制成功");
                break;
            //邮件
            case R.id.rlEmail:
                ClipboardManager cmb3 = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cmb3.setText(tvEmail.getText().toString());
                AppUtils.showToast(context, "复制成功");
                break;
            //意见反馈
            case R.id.rlYijian:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.rlVersion:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                    if(!haveInstallPermission){
                        //权限没有打开则提示用户去手动打开
                        //此方法需要API>=26才能使用
                        toInstallPermissionSettingIntent();
                    }else {
                        requestUpdate();
                    }
                } else{
                    requestUpdate();
                }
//                requestUpdate();
               break;
        }
    }

    /**
     * 开启安装未知来源权限
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toInstallPermissionSettingIntent() {
        Uri packageURI = Uri.parse("package:"+getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
        startActivityForResult(intent, INSTALL_PERMISS_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == INSTALL_PERMISS_CODE) {
            Toast.makeText(this,"已允许本APP安装未知应用",Toast.LENGTH_SHORT).show();
            requestUpdate();
        }
    }

    private void requestUpdate() {
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_OTHER);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_VERSION);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<VersionUpdateEntity>>() {
        }.getType();
        NetUtils.request(this, obj, mType, new IHttpRequestCallBack() {
            @Override
            public void onSuccess(Object object) {
                final ReturnResultEntity<VersionUpdateEntity> entity = (ReturnResultEntity<VersionUpdateEntity>) object;
                if (entity.isSuccess(context)) {
                    generalDialog = new DownloadDialog(context, false, "发现新版本", entity.getData().get(0), "下次再说", "立即更新", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            generalDialog.dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            generalDialog.startAnAnimation();
                        }
                    });
                } else {
                    AppUtils.showToast(context, entity.getRemark());
                }
            }

            @Override
            public void onFailure() {
                AppUtils.showConectFail(context);
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void requestData() {
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_OTHER);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_ABOUT);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<AboutOurEntity>>() {
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
                        ReturnResultEntity<AboutOurEntity> entity = (ReturnResultEntity<AboutOurEntity>) object;
                        if (entity.isSuccess(context)) {
                            if (entity.isNotNull()) {
                                tvVersion.setText(ParamsValues.VERSION_CODE);
                                tvWebSite.setText(entity.getData().get(0).getWebsite());
                                tvWeChat.setText(entity.getData().get(0).getWeixin());
                                tvServeTel.setText(ParamsValues.TEL);
                                tvEmail.setText(entity.getData().get(0).getEmail());
                                tvCopyright.setText(entity.getData().get(0).getCompanyname());
                                tvAddress.setText(entity.getData().get(0).getAddress());
                                svContent.setVisibility(View.VISIBLE);
                                tvNoContent.setVisibility(View.GONE);
                                llNoNet.setVisibility(View.GONE);
                            } else {
                                svContent.setVisibility(View.GONE);
                                tvNoContent.setVisibility(View.VISIBLE);
                                llNoNet.setVisibility(View.GONE);
                            }
                        } else {
                            svContent.setVisibility(View.GONE);
                            tvNoContent.setVisibility(View.GONE);
                            llNoNet.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure() {
                        svContent.setVisibility(View.GONE);
                        tvNoContent.setVisibility(View.GONE);
                        llNoNet.setVisibility(View.VISIBLE);
                    }
                }
        );
    }
}
