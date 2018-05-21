package com.yuanin.aimifinance.dialog;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.activity.HomePageActivity;
import com.yuanin.aimifinance.entity.VersionUpdateEntity;
import com.yuanin.aimifinance.inter.IXUtilsDownloadFileCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.Rotate3dAnimation;
import com.yuanin.aimifinance.utils.StaticMembers;

import org.xutils.common.Callback;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;


/**
 * @author huangxin
 * @date 2015/11/10
 * @desc 通用dialog
 */
public class DownloadDialog {
    private Context context;
    private AlertDialog dialog;
    private View view;
    private TextView tvTitle, tvVersion, tvSize, tvDesc, tvProgress;
    private Button btnCancel, btnConfirm, btnCancelDownLoad;
    private LinearLayout llMain, llDownloadTips;
    private RelativeLayout llDownload;
    private ProgressBar progressBar;
    private VersionUpdateEntity versionUpdateEntity;
    private Callback.Cancelable cancelable;
    private static final int REQUEST_CODE_UNKNOWN_APP = 2001;

    public DownloadDialog(final Context context, boolean isCancelable, String title,
                          VersionUpdateEntity versionUpdateEntity, String leftStr, String rightStr,
                          View.OnClickListener leftListener, View.OnClickListener rightListener) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_download, null);
        this.context = context;
        this.versionUpdateEntity = versionUpdateEntity;
        dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(isCancelable);
        dialog.setView(new EditText(context));
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 非常重要：设置对话框弹出的位置
        window.setContentView(view);
        window.setWindowAnimations(R.style.BottomDialog); // 添加动画
        initViews();
        tvTitle.setText(title);
        tvVersion.setText(versionUpdateEntity.getVersion_num());
        tvSize.setText(versionUpdateEntity.getSize());
        tvDesc.setText(Html.fromHtml(versionUpdateEntity.getDescription()));
        btnCancel.setText(leftStr);
        btnConfirm.setText(rightStr);
        btnCancel.setOnClickListener(leftListener);
        btnConfirm.setOnClickListener(rightListener);
    }


    private void initViews() {
        tvVersion = (TextView) view.findViewById(R.id.tvVersion);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvSize = (TextView) view.findViewById(R.id.tvSize);
        tvDesc = (TextView) view.findViewById(R.id.tvDesc);
        tvProgress = (TextView) view.findViewById(R.id.tvProgress);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        btnCancelDownLoad = (Button) view.findViewById(R.id.btnCancelDownLoad);
        progressBar = (ProgressBar) view.findViewById(R.id.pbDownload);
        llMain = (LinearLayout) view.findViewById(R.id.llMain);
        llDownloadTips = (LinearLayout) view.findViewById(R.id.llDownloadTips);
        llDownload = (RelativeLayout) view.findViewById(R.id.llDownload);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) llMain.getLayoutParams();
        lp.width = StaticMembers.SCREEN_WIDTH;
        llMain.setLayoutParams(lp);

        btnCancelDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = btnCancelDownLoad.getText().toString();
                if (text.equals("取消下载")) {
                    if (cancelable != null) {
                        cancelable.cancel();
                        btnCancelDownLoad.setText("继续下载");
                    }
                } else if (text.equals("继续下载")) {
                    Acp.getInstance(context).request(new AcpOptions.Builder()
                                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    .build(),
                            new AcpListener() {
                                @Override
                                public void onGranted() {
                                    startDownload();
                                }

                                @Override
                                public void onDenied(List<String> permissions) {
                                    AppUtils.showToast(context, permissions.toString() + "权限拒绝");
                                }
                            });
                    btnCancelDownLoad.setText("取消下载");
                }
            }
        });
    }

    public void dismiss() {
        dialog.dismiss();
    }


    public void startAnAnimation() {
        LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) llDownload.getLayoutParams();
        lp2.height = llDownloadTips.getHeight();
        llDownload.setLayoutParams(lp2);
        float centerX = llMain.getWidth() / 2f;
        float centerY = llMain.getHeight() / 2f;
        Rotate3dAnimation animation = new Rotate3dAnimation(0, 90, centerX, centerY,
                310.0f, true);
        // 动画持续时间500毫秒
        animation.setDuration(200);
        // 动画完成后保持完成的状态
        animation.setInterpolator(new AccelerateInterpolator());
        // 设置动画的监听器
        animation.setFillAfter(true);
        llMain.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llDownloadTips.setVisibility(View.GONE);
                llDownload.setVisibility(View.VISIBLE);
                float centerX = llMain.getWidth() / 2f;
                float centerY = llMain.getHeight() / 2f;
                // 构建3D旋转动画对象，旋转角度为270到360度，这使得ImageView将会从不可见变为可见
                Rotate3dAnimation rotation = new Rotate3dAnimation(270, 360, centerX, centerY,
                        310.0f, false);
                // 动画持续时间500毫秒
                rotation.setDuration(100);
                // 动画完成后保持完成的状态
                rotation.setFillAfter(true);
                rotation.setInterpolator(new AccelerateInterpolator());
                llMain.startAnimation(rotation);
                rotation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Acp.getInstance(context).request(new AcpOptions.Builder()
                                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .build(),
                                new AcpListener() {
                                    @Override
                                    public void onGranted() {
                                        startDownload();
                                    }

                                    @Override
                                    public void onDenied(List<String> permissions) {
                                        AppUtils.showToast(context, permissions.toString() + "权限拒绝");
                                    }
                                });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startDownload() {
        String fileFolderPath = StaticMembers.ROOT_PATH + "/" + StaticMembers.DOWN_PATH;
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        if(fileFolder != null ){
            fileFolder.delete();
        }
        final String savePath = fileFolderPath + "/" + context.getResources().getString(R.string.app_name) + ".apk";
        cancelable = NetUtils.downloadFile(context, versionUpdateEntity.getDown_url(), savePath, new IXUtilsDownloadFileCallBack() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess() {
                dialog.dismiss();
                //打开APK
                File targetFile = new File(savePath);
                if (targetFile.exists()){
                    /*Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(targetFile), "application/vnd.android.package-archive");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);*/
                    openFile(new File(savePath),context);
                }else{
                    AppUtils.showToast(context, "apk 文件不存在");
                }
                AppUtils.showToast(context, "下载完成");
            }

            @Override
            public void onFailure() {
                AppUtils.showToast(context, "下载失败，请到应用市场进行更新。");
                btnCancelDownLoad.setText("继续下载");
            }

            @Override
            public void onLoading(int total, int current) {
                NumberFormat numberFormat = NumberFormat.getInstance();
                numberFormat.setMaximumFractionDigits(0);
                progressBar.setMax(total);
                progressBar.setProgress(current);
                tvProgress.setText(numberFormat.format((float) current / (float) total * 100) + "%");
            }
        });

    }


    public void openFile(File var0, Context var1) {
        Intent var2 = new Intent();
        var2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        var2.setAction(Intent.ACTION_VIEW);
        /*if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = var1.getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    Toast.makeText(var1, "没有打开此APP安装未知应用权限", Toast.LENGTH_SHORT).show();
                }
            }
        }*/
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            Uri uriForFile = FileProvider.getUriForFile(var1, var1.getApplicationContext().getPackageName() + ".provider", var0);
            var2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            var2.setDataAndType(uriForFile, "application/vnd.android.package-archive");
        } else {
            var2.setDataAndType(Uri.fromFile(var0),  "application/vnd.android.package-archive");
        }
        try {
            var1.startActivity(var2);
        } catch (Exception var5) {
            var5.printStackTrace();
            Toast.makeText(var1, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }
    }
}
