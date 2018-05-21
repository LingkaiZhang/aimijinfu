package com.yuanin.aimifinance.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuanin.aimifinance.R;
import com.yuanin.aimifinance.adapter.ArrayWheelAdapter;
import com.yuanin.aimifinance.entity.WheelOneModel;
import com.yuanin.aimifinance.entity.WheelThreeModel;
import com.yuanin.aimifinance.entity.WheelTwoModel;
import com.yuanin.aimifinance.inter.IWheelSetTextCallBack;
import com.yuanin.aimifinance.listener.OnWheelChangedListener;
import com.yuanin.aimifinance.utils.StaticMembers;
import com.yuanin.aimifinance.utils.XmlParserHandler;
import com.yuanin.aimifinance.view.WheelView;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author huangxin
 * @date 2015/11/9
 * @desc 省市选择dialog
 */
public class WheelDialog implements View.OnClickListener,
        OnWheelChangedListener {

    private AlertDialog dialog;
    private Context context;
    private WheelView mViewOne;
    private WheelView mViewTwo;
    private WheelView mViewThree;
    private LinearLayout llConfirm;
    private View view;
    private int howWheel, showItemCount;
    private String dataName, title;
    private IWheelSetTextCallBack wheelSetTextCallBack;
    /**
     * 所有省
     */
    protected String[] mOneDatas;
    /**
     * key - 一级节点 value - 二级节点
     */
    protected Map<String, String[]> mTwoDatasMap = new HashMap<String, String[]>();
    /**
     * key - 二级节点 values - 三级节点
     */
    protected Map<String, String[]> mThreeDatasMap = new HashMap<String, String[]>();


    /**
     * 当前一级节点名称
     */
    protected String mCurrentOneName;
    /**
     * 当前二级节点名称
     */
    protected String mCurrentTwoName;
    /**
     * 当前三级名称
     */
    protected String mCurrentThreeName = "";


    public WheelDialog(Context context, int howWheel, String title, String dataName,
                       int showItemCount, IWheelSetTextCallBack wheelSetTextCallBack) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_wheel, null);
        this.context = context;
        this.howWheel = howWheel;
        this.dataName = dataName;
        this.showItemCount = showItemCount;
        this.title = title;
        this.wheelSetTextCallBack = wheelSetTextCallBack;
        initViews();
        initDialog();
        initProvinceDatas();
        initShowWhat();
        setUpListener();
        setUpData();
    }

    private void initShowWhat() {
        switch (howWheel) {
            case 1:
                mViewTwo.setVisibility(View.GONE);
                mViewThree.setVisibility(View.GONE);
                break;
            case 2:
                mViewThree.setVisibility(View.GONE);
                break;
            case 3:
                break;
        }
    }

    private void initDialog() {
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 非常重要：设置对话框弹出的位置
        window.setContentView(view);
        window.setWindowAnimations(R.style.BottomDialog); // 添加动画
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = StaticMembers.SCREEN_WIDTH; // 设置宽度铺满全屏
        window.setAttributes(lp);
    }


    private void initViews() {
        mViewOne = (WheelView) view.findViewById(R.id.wheelOne);
        mViewTwo = (WheelView) view.findViewById(R.id.wheelTwo);
        mViewThree = (WheelView) view.findViewById(R.id.wheelThree);
        llConfirm = (LinearLayout) view.findViewById(R.id.llConfirm);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
    }

    private void setUpListener() {
        // 添加change事件
        mViewOne.addChangingListener(this);
        // 添加change事件
        mViewTwo.addChangingListener(this);
        // // 添加change事件
        mViewThree.addChangingListener(this);
        // 添加onclick事件
        llConfirm.setOnClickListener(this);
    }

    private void setUpData() {
        mViewOne.setViewAdapter(new ArrayWheelAdapter<String>(
                context, mOneDatas));
        // 设置可见条目数量
        mViewOne.setVisibleItems(showItemCount);
        mViewTwo.setVisibleItems(showItemCount);
        mViewThree.setVisibleItems(showItemCount);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewOne) {
            updateCities();
        } else if (wheel == mViewTwo) {
            updateAreas();
        } else if (wheel == mViewThree) {
            mCurrentThreeName =
                    mThreeDatasMap.get(mCurrentTwoName)[newValue];
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewTwo.getCurrentItem();
        if (howWheel != 1) {
            mCurrentTwoName = mTwoDatasMap.get(mCurrentOneName)[pCurrent];
        }
        if (howWheel == 3) {
            String[] areas = mThreeDatasMap.get(mCurrentTwoName);
            if (areas == null) {
                areas = new String[]{""};
            }
            mViewThree.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
            mViewThree.setCurrentItem(0);
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewOne.getCurrentItem();
        mCurrentOneName = mOneDatas[pCurrent];
        String[] cities = mTwoDatasMap.get(mCurrentOneName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewTwo.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
        mViewTwo.setCurrentItem(0);
        updateAreas();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llConfirm:
                showSelectedResult();
                break;
            default:
                break;
        }
    }

    private void showSelectedResult() {
        switch (howWheel) {
            case 1:
                wheelSetTextCallBack.onNotify(mCurrentOneName);
                break;
            case 2:
                wheelSetTextCallBack.onNotify(mCurrentOneName, mCurrentTwoName);
                break;
            case 3:
                wheelSetTextCallBack.onNotify(mCurrentOneName, mCurrentTwoName, mCurrentThreeName);
                break;
        }
        dialog.dismiss();
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas() {
        List<WheelOneModel> oneList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open(dataName);
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            oneList = handler.getDataList();
            //*/ 初始化默认选中
            if (oneList != null && !oneList.isEmpty()) {
                mCurrentOneName = oneList.get(0).getName();
                List<WheelTwoModel> twoList = oneList.get(0).getWheelTwoList();
                if (twoList != null && !twoList.isEmpty()) {
                    mCurrentTwoName = twoList.get(0).getName();
                    List<WheelThreeModel> threeList = twoList.get(0).getWheelThreeList();
                    if (threeList != null && !threeList.isEmpty()) {
                        mCurrentThreeName = threeList.get(0).getName();
                    }
                }
            }
            //*/
            mOneDatas = new String[oneList.size()];
            for (int i = 0; i < oneList.size(); i++) {
                // 遍历所一级节点的数据
                mOneDatas[i] = oneList.get(i).getName();
                List<WheelTwoModel> cityList = oneList.get(i).getWheelTwoList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<WheelThreeModel> districtList = cityList.get(j).getWheelThreeList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    WheelThreeModel[] distrinctArray = new WheelThreeModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        WheelThreeModel wheelThreeModel = new WheelThreeModel(districtList.get(k).getName());
                        distrinctArray[k] = wheelThreeModel;
                        distrinctNameArray[k] = wheelThreeModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mThreeDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mTwoDatasMap.put(oneList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }
}
