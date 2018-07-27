package com.yuanin.aimifinance.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.ViewPagerAdapter;
import com.yuanin.aimifinance.base.BaseActivity;
import com.yuanin.aimifinance.entity.AdvertisementEntity;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.StaticMembers;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * 引导界面(加载或者导航页)
 */
public class GuidePageActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    @ViewInject(R.id.ll)
    private LinearLayout linearLayout;

    //定义ViewPager适配器
    private ViewPagerAdapter vpAdapter;

    //定义一个ArrayList来存放View
    private ArrayList<View> views;

    //引导图片资源
    private static final int[] pics = {R.mipmap.guide_01_600, R.mipmap.guide_02_600};

    //底部小点的图片
    private ImageView[] points;

    //记录当前选中位置
    private int currentIndex;

    private boolean isNeedDot = false;

    private boolean isRunAd = false;//是否跳到广告页
    private AdvertisementEntity advertisementEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        x.view().inject(this);
        //透明状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // Translucent status bar
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        initView();
        //需要导航页
        if (StaticMembers.IS_NEED_GUDIE) {
            isNeedDot = true;
            initGuideData();
            AppUtils.save2SharedPreferences(this, ParamsKeys.GUIDE_FILE, ParamsKeys.GUIDE_KEY, ParamsValues.GUIDE_VALUE);
            StaticMembers.IS_NEED_GUDIE = false;
            StaticMembers.aCache.clear();
        } else {
            //欢迎页
            isNeedDot = false;
            initWelComeData();
        }
    }

    private void setRightGestureTip() {
        SharedPreferences sharedPreferences = getSharedPreferences(ParamsKeys.TIPS_FILE,
                Activity.MODE_PRIVATE);
        Set<String> tipSet = sharedPreferences.getStringSet(ParamsKeys.TIPS_GESTURE, null);
        Iterator<String> tipIterator = tipSet.iterator();
        String currentTime = AppUtils.dateToStr(new Date(), "yyyyMMdd");
        while (tipIterator.hasNext()) {
            String[] strs = tipIterator.next().split("\\|");
            if (strs[0].equals(currentTime)) {
                StaticMembers.GESTURE_TIP = strs[1];
            }
        }
    }


    /**
     * 初始化欢迎加载界面
     */

    private void initWelComeData() {
//        linearLayout.setVisibility(View.GONE);
        ImageView iv = new ImageView(this);
        iv.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        //此方法更节省内存 防止OOM
        BitmapDrawable bd = new BitmapDrawable(getResources(), AppUtils.getBitmap(this, R.mipmap.init_page));
        iv.setBackgroundDrawable(bd);
        // iv.setImageResource(R.mipmap.init_page);
        views.add(iv);
        //请求服务器查询是否有广告
        requestAd();
        //设置数据
        viewPager.setAdapter(vpAdapter);
        //设置监听
        viewPager.setOnPageChangeListener(this);
    }

    private void requestAd() {
        final String adCode = AppUtils.getFromSharedPreferences(this, ParamsKeys.AD_FILE, ParamsKeys.AD_CODE);
        JSONObject obj = AppUtils.getPublicJsonObject(false);
        try {
            obj.put(ParamsKeys.MODULE, ParamsValues.MODULE_OTHER);
            obj.put(ParamsKeys.MOTHED, ParamsValues.MOTHED_INTRO);
            String token = AppUtils.getMd5Value(AppUtils.getToken(obj));
            obj.put(ParamsKeys.TOKEN, token);
            obj.remove(ParamsKeys.KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<AdvertisementEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, 2 * 1000, new IHttpRequestCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Object object) {
                final ReturnResultEntity<AdvertisementEntity> entity = (ReturnResultEntity<AdvertisementEntity>) object;
                if (entity.isSuccess(GuidePageActivity.this)) {
                    advertisementEntity = entity.getData().get(0);
                    if (adCode == null || adCode.length() == 0) {
                        AppUtils.save2SharedPreferences(GuidePageActivity.this, ParamsKeys.AD_FILE,
                                ParamsKeys.AD_CODE, advertisementEntity.getCode());
                        AppUtils.save2SharedPreferences(GuidePageActivity.this, ParamsKeys.AD_FILE,
                                ParamsKeys.AD_COUNT, "1");
                        isRunAd = true;
                    } else {
                        if (advertisementEntity.getCode().equals(adCode)) {
                            int adCount = Integer.parseInt(AppUtils.getFromSharedPreferences(GuidePageActivity.this,
                                    ParamsKeys.AD_FILE, ParamsKeys.AD_COUNT));
                            if (adCount < 3) {
                                adCount += 1;
                                AppUtils.save2SharedPreferences(GuidePageActivity.this, ParamsKeys.AD_FILE,
                                        ParamsKeys.AD_COUNT, String.valueOf(adCount));
                                isRunAd = true;
                            } else {
                                isRunAd = false;
                            }
                        } else {
                            AppUtils.save2SharedPreferences(GuidePageActivity.this, ParamsKeys.AD_FILE,
                                    ParamsKeys.AD_CODE, advertisementEntity.getCode());
                            AppUtils.save2SharedPreferences(GuidePageActivity.this, ParamsKeys.AD_FILE,
                                    ParamsKeys.AD_COUNT, "1");
                            isRunAd = true;
                        }
                    }
                } else {
                    isRunAd = false;
                }
                if (entity.isNotNull()) {
                    Set<String> tipSet = new HashSet<String>();
                    for (int i = 0; i < entity.getData().get(0).getList().size(); i++) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(entity.getData().get(0).getList().get(i).getTime());
                        sb.append("|");
                        sb.append(entity.getData().get(0).getList().get(i).getCon());
                        tipSet.add(sb.toString());
                    }
                    //实例化SharedPreferences对象（第一步）
                    SharedPreferences mySharedPreferences = getSharedPreferences(ParamsKeys.TIPS_FILE,
                            Activity.MODE_PRIVATE);
                    //实例化SharedPreferences.Editor对象（第二步）
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putStringSet(ParamsKeys.TIPS_GESTURE, tipSet);
                    editor.apply();
                }
                setRightGestureTip();
                //跳转
                if (isRunAd) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(GuidePageActivity.this, AdvertisementActivity.class);
                            intent.putExtra("advertisementEntity", advertisementEntity);
                            startActivity(intent);
                            GuidePageActivity.this.finish();
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (StaticMembers.IS_NEED_FINGER_PWD) {
                                Intent intent = new Intent(GuidePageActivity.this, FingerPasswordActivity.class);
                                startActivity(intent);
                            } else if (StaticMembers.IS_NEED_GUSTURE_PWD) {
                                Intent intent = new Intent(GuidePageActivity.this, GesturePasswordVerifyActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(GuidePageActivity.this, HomePageActivity.class);
                                startActivity(intent);
                            }
                            GuidePageActivity.this.finish();
                        }
                    }, 2000);
                }
            }

            @Override
            public void onFailure() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (StaticMembers.IS_NEED_GUSTURE_PWD) {
                            if (StaticMembers.IS_NEED_FINGER_PWD) {
                                Intent intent = new Intent(GuidePageActivity.this, FingerPasswordActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(GuidePageActivity.this, GesturePasswordVerifyActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(GuidePageActivity.this, HomePageActivity.class);
                            startActivity(intent);
                        }
                        GuidePageActivity.this.finish();
                    }
                }, 1000);
            }
        });
    }


    /**
     * 初始化组件
     */
    private void initView() {
        //实例化ArrayList对象
        views = new ArrayList<View>();

        //实例化ViewPager适配器
        vpAdapter = new ViewPagerAdapter(views);
    }

    /**
     * 初始化数据(导航页)
     */
    private void initGuideData() {
//        linearLayout.setVisibility(View.VISIBLE);
        //初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            if (i == pics.length - 1) {
                View view = LayoutInflater.from(this).inflate(R.layout.view_pager_guide_end, null);
                ImageButton btnStartApp = (ImageButton) view.findViewById(R.id.btnStartApp);
                btnStartApp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StaticMembers.IS_NEED_FINGER_PWD) {
                            Intent intent = new Intent(GuidePageActivity.this, FingerPasswordActivity.class);
                            startActivity(intent);
                        } else if (StaticMembers.IS_NEED_GUSTURE_PWD) {
                            Intent intent = new Intent(GuidePageActivity.this, GesturePasswordVerifyActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(GuidePageActivity.this, HomePageActivity.class);
                            startActivity(intent);
                        }
                        GuidePageActivity.this.finish();
                    }
                });
                views.add(view);
            } else {
                ImageView iv = new ImageView(this);
                iv.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                //此方法更节省内存 防止OOM
                BitmapDrawable bd = new BitmapDrawable(getResources(), AppUtils.getBitmap(this, pics[i]));
                iv.setBackgroundDrawable(bd);
//                iv.setImageResource(pics[i]);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                views.add(iv);
            }

        }
        //设置数据
        viewPager.setAdapter(vpAdapter);
        //设置监听
        viewPager.setOnPageChangeListener(this);

        //初始化底部小点
        initPoint();
    }

    /**
     * 初始化底部小点(导航页)
     */
    private void initPoint() {
        points = new ImageView[pics.length];

        //循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            //得到一个LinearLayout下面的每一个子元素
            points[i] = (ImageView) linearLayout.getChildAt(i);
            //默认都设为灰色
            points[i].setEnabled(true);
            //给每个小点设置监听
            points[i].setOnClickListener(this);
            //设置位置tag，方便取出与当前位置对应
            points[i].setTag(i);
        }

        //设置当面默认的位置
        currentIndex = 0;
        //设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
    }

    /**
     * 当滑动状态改变时调用
     */
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    /**
     * 当当前页面被滑动时调用
     */

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    /**
     * 当新的页面被选中时调用
     */

    @Override
    public void onPageSelected(int position) {
        if (isNeedDot) {
            //设置底部小点选中状态
            setCurDot(position);
        }
    }

    /**
     * 通过点击事件来切换当前的页面
     */
    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }

    /**
     * 设置当前页面的位置
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }
        points[positon].setEnabled(false);
        points[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

}
