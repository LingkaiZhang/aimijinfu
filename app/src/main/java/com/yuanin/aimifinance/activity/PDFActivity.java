package com.yuanin.aimifinance.activity;



import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.inter.IXUtilsDownloadFileCallBack;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.StaticMembers;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.text.NumberFormat;

import static android.widget.Toast.*;

/**
 * Created by huangxin on 2018/3/12.
 */

public class PDFActivity extends BaseActivity {

    @ViewInject(R.id.includeTop)
    private View toptitleView;
    @ViewInject(R.id.includeTop2)
    private View toptitleView2;
    @ViewInject(R.id.pdfView)
    private PDFView pdfView;


    private String pdfUrl;
    private String pdfName;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private File file;
    private Callback.Cancelable cancelable;
    private String tvProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        x.view().inject(this);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initTopBar(getResources().getString(R.string.viewer_ontract), toptitleView, true);
        } else {*/
            toptitleView.setVisibility(View.GONE);
            toptitleView2.setVisibility(View.VISIBLE);
            initTopBarWithRightText(getResources().getString(R.string.viewer_ontract), toptitleView2, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayOther(file);
                }
            },"打开方式");
      //  }

        pdfUrl = getIntent().getStringExtra("pdfURL");
        //获取动态权限
        getPermission();

//        pdfUrl = "http://csres.yuanin.com/contract/2018_03/11927/20180312351500066578.pdf";
        int idx = pdfUrl.lastIndexOf("/");
        pdfName = pdfUrl.substring(idx + 1, pdfUrl.length());
        file = new File(StaticMembers.ROOT_PATH + "/" + StaticMembers.DOWN_PATH + "/" + pdfName);
        if (!file.exists()){
            /*OkHttpUtils.get(pdfUrl)
                    .tag(this)
                    .execute(new DownloadFileCallBack(Environment.getExternalStorageDirectory() +
                            "/temp", pdfName));*/
            startDownloadPDF(pdfName,pdfUrl);
        } else {
            display(file);
        }
    }

    private void startDownloadPDF(String pdfName,String pdfUrl) {
        String fileFolderPath = StaticMembers.ROOT_PATH + "/" + StaticMembers.DOWN_PATH;
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        final String pdfPath = fileFolderPath + "/" + pdfName;
        cancelable = NetUtils.downloadFile(this, pdfUrl, pdfPath, new IXUtilsDownloadFileCallBack() {
            Toast mToast;
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess() {
                File file = new File(pdfPath);
                display(file);
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onLoading(int total, int current) {
                NumberFormat numberFormat = NumberFormat.getInstance();
                numberFormat.setMaximumFractionDigits(0);
                tvProgress = numberFormat.format((float) current / (float) total * 100) + "%";

                if (mToast != null){
                    mToast.setText( "当前进度:" + tvProgress + ",加载中...");
                }else {
                    mToast = Toast.makeText(getApplicationContext(),
                            "当前进度:" + tvProgress + ",加载中...", LENGTH_SHORT);
                }
                mToast.show();
            }
        });
    }

    private void displayOther(File file) {
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent = Intent.createChooser(target, "选择打开方式");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uriForFile = FileProvider.getUriForFile(PDFActivity.this.getApplicationContext(), PDFActivity.this.getApplicationContext()
                    .getPackageName() + ".provider" , file);
            target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION );//增加读写权限
            target.setDataAndType(uriForFile,"application/pdf");
        } else {
            target.setDataAndType(Uri.fromFile(file),"application/pdf");
        }

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
            Toast.makeText(this, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private void getPermission() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(PDFActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(PDFActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(PDFActivity.this,
                        PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }

            ActivityCompat.requestPermissions(PDFActivity.this,
                    PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

        while ((ContextCompat.checkSelfPermission(PDFActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) != PackageManager.PERMISSION_GRANTED) {
        }
    }

    private void display( File file) {
        pdfView.fromFile(file)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .onDraw(new OnDrawListener() {
                    @Override
                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                    }
                })
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        makeText(getApplicationContext(), "加载完成", LENGTH_SHORT).show();
                    }
                })
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {

                    }
                })
                .onPageScroll(new OnPageScrollListener() {
                    @Override
                    public void onPageScrolled(int page, float positionOffset) {

                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
//                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                })
                .enableAnnotationRendering(false)
                .password(null)
                .scrollHandle(null)
                .load();
    }
}
