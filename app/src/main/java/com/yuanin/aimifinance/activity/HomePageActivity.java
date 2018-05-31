package com.yuanin.aimifinance.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.ViewPagerFragmentAdapter;
import com.yuanin.aimifinance.base.BaseFragmentActivity;
import com.yuanin.aimifinance.dialog.DownloadDialog;
import com.yuanin.aimifinance.entity.EventMessage;
import com.yuanin.aimifinance.entity.ReturnResultEntity;
import com.yuanin.aimifinance.entity.TabIndicatorEntity;
import com.yuanin.aimifinance.entity.VersionUpdateEntity;
import com.yuanin.aimifinance.fragment.FinanceProductFragment;
import com.yuanin.aimifinance.fragment.MineFragment;
import com.yuanin.aimifinance.fragment.NewIndexFragment;
import com.yuanin.aimifinance.inter.IHttpRequestCallBack;
import com.yuanin.aimifinance.utils.AppManager;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.LogUtils;
import com.yuanin.aimifinance.utils.NetUtils;
import com.yuanin.aimifinance.utils.ParamsKeys;
import com.yuanin.aimifinance.utils.ParamsValues;
import com.yuanin.aimifinance.utils.ViewPagerUtils;
import com.yuanin.aimifinance.view.MyViewPager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 主页
 */
public class HomePageActivity extends BaseFragmentActivity {
    @ViewInject(R.id.tvIndex)
    private TextView tvIndex;
    @ViewInject(R.id.tvFinance)
    private TextView tvFinance;
    @ViewInject(R.id.tvMine)
    private TextView tvMine;
    @ViewInject(R.id.viewPager)
    private MyViewPager mViewPager;
    private List<TextView> textViews;
    // 双击back退出程序
    private long mLastBackTime = 0;
    private long TIME_DIFF = 2 * 1000;
    private int currentIndex = 0;
    private DownloadDialog generalDialog;
    private static final int INSTALL_PERMISS_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        x.view().inject(this);
        //透明状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // Translucent status bar
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        currentIndex = getIntent().getIntExtra("currentIndex", 0);
        EventBus.getDefault().register(this);
        if (textViews == null) {
            textViews = new ArrayList<TextView>();
        }
        textViews.add(tvIndex);
        textViews.add(tvFinance);
        textViews.add(tvMine);
        // 初始化ViewPager
        initViewPager();
        //适配Android 8.0 版本更新
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean haveInstallPermission = HomePageActivity.this.getPackageManager().canRequestPackageInstalls();
            if(!haveInstallPermission){
                //权限没有打开则提示用户去手动打开
                //此方法需要API>=26才能使用
                toInstallPermissionSettingIntent();
            }else {
                requestUpdate();
            }
        } else{
            requestUpdate();
        }*/
        requestUpdate();
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
            Toast.makeText(this,"安装应用",Toast.LENGTH_SHORT).show();
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
            LogUtils.d(this,ParamsValues.MOTHED_VERSION + ",obj = " + obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Type mType = new TypeToken<ReturnResultEntity<VersionUpdateEntity>>() {
        }.getType();
        NetUtils.request(obj, mType, 1000 * 10, new IHttpRequestCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(Object object) {
                final ReturnResultEntity<VersionUpdateEntity> entity = (ReturnResultEntity<VersionUpdateEntity>) object;
                LogUtils.d(this,ParamsValues.MOTHED_VERSION + "-----" + entity.toString());
                if (entity.isSuccess(HomePageActivity.this)) {
                    generalDialog = new DownloadDialog(HomePageActivity.this, false, "发现新版本", entity.getData().get(0), "下次再说", "立即更新", new View.OnClickListener() {
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
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    //接收通信数据
    public void onEventMainThread(EventMessage eventMessage) {
        if (eventMessage != null) {
            if (eventMessage.getType() == EventMessage.HOMEPAGE_JUMP_TAB) {
                int current = (int) eventMessage.getObject();
                currentIndex = current;
                mViewPager.setCurrentItem(current, false);
                ViewPagerUtils.changeTextViewStyle_Main(HomePageActivity.this, current, textViews);
            }
        }
    }

    // 点击事件
    @Event(value = {R.id.tvIndex, R.id.tvFinance, R.id.tvMine})
    private void onViewClicked(View view) {
        switch (view.getId()) {
            //首页
            case R.id.tvIndex:
                currentIndex = 0;
                mViewPager.setCurrentItem(0, false);
                ViewPagerUtils.changeTextViewStyle_Main(HomePageActivity.this, 0, textViews);
                break;
            //项目
            case R.id.tvFinance:
                currentIndex = 1;
                mViewPager.setCurrentItem(1, false);
                ViewPagerUtils.changeTextViewStyle_Main(HomePageActivity.this, 1, textViews);
                break;
            //我的
            case R.id.tvMine:
                currentIndex = 2;
                mViewPager.setCurrentItem(2, false);
                ViewPagerUtils.changeTextViewStyle_Main(HomePageActivity.this, 2, textViews);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initViewPager() {
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(3);
        // 包含4个fragment界面
        List<TabIndicatorEntity> list = ViewPagerUtils.getTabIndicator(3);
        // 4个fragment界面封装
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new NewIndexFragment());
        fragmentList.add(new FinanceProductFragment());
        fragmentList.add(new MineFragment());
        // 设置ViewPager适配器
        ViewPagerFragmentAdapter viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), list, fragmentList);
        mViewPager.setAdapter(viewPagerFragmentAdapter);
        currentIndex = 0;
        mViewPager.setCurrentItem(0, false);
        ViewPagerUtils.changeTextViewStyle_Main(HomePageActivity.this, currentIndex, textViews);
    }

    @Override
    public void onPageSelected(int position) {
        ViewPagerUtils.changeTextViewStyle_Main(HomePageActivity.this, position, textViews);
    }

    /**
     * 以下的几个方法用来，让fragment能够监听touch事件
     */
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        boolean onTouch(MotionEvent ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 双击back,退出程序
            long now = new Date().getTime();
            if (now - mLastBackTime < TIME_DIFF) {
                AppManager.getAppManager().AppExit(this);
                return super.onKeyDown(keyCode, event);
            } else {
                mLastBackTime = now;
                Toast.makeText(this, getResources().getString(R.string.double_click), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
