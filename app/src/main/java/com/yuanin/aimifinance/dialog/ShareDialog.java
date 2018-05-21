package com.yuanin.aimifinance.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.ShareDialogGridAdapter;
import com.yuanin.aimifinance.entity.ShareContentEntity;
import com.yuanin.aimifinance.entity.ShareGridEntity;
import com.yuanin.aimifinance.utils.StaticMembers;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


public class ShareDialog implements PlatformActionListener {

    private AlertDialog dialog;
    private Context context;
    private ShareContentEntity mEntity;

    public ShareDialog(final Context context, ShareContentEntity entity, List<ShareGridEntity> shareList, int gridNum) {
        this.mEntity = entity;
        this.context = context;
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 非常重要：设置对话框弹出的位置
        window.setContentView(R.layout.dialog_share);
        window.setWindowAnimations(R.style.BottomDialog); // 添加动画
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = StaticMembers.SCREEN_WIDTH; // 设置宽度铺满全屏
        window.setAttributes(lp);
        GridView gridView = (GridView) window.findViewById(R.id.share_gridView);
        gridView.setNumColumns(gridNum);
        TextView cancelButton = (TextView) window.findViewById(R.id.btnCancel);
        ShareDialogGridAdapter mAdp = new ShareDialogGridAdapter(shareList, context);
        gridView.setAdapter(mAdp);

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mEntity != null) {
                    Toast.makeText(context, context.getResources().getString(R.string.sharing), Toast.LENGTH_SHORT).show();
                    //2、设置分享内容
                    Platform.ShareParams sp = new Platform.ShareParams();
                    String share2Who = "";
                    switch (position) {
                        case 0:
                            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                            // url：仅在微信（包括好友和朋友圈）中使用
                            sp.setUrl(mEntity.getUrl());   //网友点进链接后，可以看到分享的详情
                            share2Who = Wechat.NAME;
                            break;
                        case 1:
                            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                            // url：仅在微信（包括好友和朋友圈）中使用
                            sp.setUrl(mEntity.getUrl());   //网友点进链接后，可以看到分享的详情
                            share2Who = WechatMoments.NAME;
                            break;
                        case 2:
                            sp.setTitleUrl(mEntity.getTitleUrl());  //网友点进链接后，可以看到分享的详情
                            share2Who = QQ.NAME;
                            break;
                        case 3:
                            sp.setTitleUrl(mEntity.getTitleUrl());  //网友点进链接后，可以看到分享的详情
                            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                            sp.setComment(mEntity.getComment());
                            // site是分享此内容的网站名称，仅在QQ空间使用
                            sp.setSite(mEntity.getSite());
                            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                            sp.setSiteUrl(mEntity.getSiteUrl());
                            share2Who = QZone.NAME;
                            break;
                    }
                    sp.setTitle(mEntity.getTitle());
                    // text是分享文本：所有平台都需要这个字段
                    sp.setText(mEntity.getText());  //最多40个字符
//                    // imagePath是图片的本地路径：除Linked-In以外的平台都支持此参数
                    if (mEntity.getImagePath() != null && mEntity.getImagePath().length() > 0) {
                        sp.setImagePath(mEntity.getImagePath());//确保SDcard下面存在此张图片
                    } else if (mEntity.getImageUrl() != null && mEntity.getImageUrl().length() > 0) {
                        //网络图片的url：所有平台
                        sp.setImageUrl(mEntity.getImageUrl());//网络图片rul
                    }
                    //3、非常重要：获取平台对象
                    Platform plat = ShareSDK.getPlatform(share2Who);
                    plat.setPlatformActionListener(ShareDialog.this); // 设置分享事件回调
                    // 执行分享
                    plat.share(sp);
                }

            }
        });
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Toast.makeText(context, context.getResources().getString(R.string.share_completed), Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Toast.makeText(context, context.getResources().getString(R.string.share_failed) + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(Platform platform, int i) {
//        Toast.makeText(getActivity(), "取消分享", Toast.LENGTH_LONG).show();
    }

//    public void setCancelButtonOnClickListener(OnClickListener Listener) {
//        cancelButton.setOnClickListener(Listener);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        gridView.setOnItemClickListener(listener);
//    }
//
//
//    /**
//     * 关闭对话框
//     */
//    public void dismiss() {
//        dialog.dismiss();
//    }
}