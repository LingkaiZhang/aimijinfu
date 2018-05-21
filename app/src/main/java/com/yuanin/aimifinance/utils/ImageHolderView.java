package com.yuanin.aimifinance.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.inter.Holder;


public class ImageHolderView implements Holder<String> {
    private SimpleDraweeView imageView;
    /**
     * 图片缓存
     */
    private GenericDraweeHierarchyBuilder builder;
    private GenericDraweeHierarchy hierarchy;

    @Override
    public View createView(Context context) {
        imageView = new SimpleDraweeView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (builder == null) {
            builder = new GenericDraweeHierarchyBuilder(context.getResources());
        }
        if (hierarchy == null) {
            hierarchy = builder.setFadeDuration(300).build();
            hierarchy.setPlaceholderImage(new BitmapDrawable(context.getResources(), AppUtils.getBitmap(context, R.mipmap.image_loading)));
//            hierarchy.setPlaceholderImage(R.mipmap.image_loading);
        }
        return imageView;
    }

    @Override
    public void UpdateUI(final Context context, final int position, String data) {
        imageView.setHierarchy(hierarchy);
        imageView.setImageURI(Uri.parse(data));
    }
}