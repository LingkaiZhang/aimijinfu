package com.yuanin.aimifinance.entity;

import android.content.Context;
import android.content.Intent;

import com.yuanin.aimifinance.activity.LoginActivity;
import com.yuanin.aimifinance.activity.LoginRegisterActivity;
import com.yuanin.aimifinance.utils.AppUtils;
import com.yuanin.aimifinance.utils.StaticMembers;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangxin
 * @date 2015/10/26
 * @desc 接受数据类
 */
public class ReturnResultEntity<T> implements Serializable {

    //接口执行结果状态, 1:成功，0:失败
    private int result;
    //成功/失败原因
    private String remark;

    //接口执行成功时，返回的数据内容
    public List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public int getResult() {
        return result;
    }

    public String getRemark() {
        return remark;
    }

    //获取到对象后，必须要判断是否获取成功
    public boolean isSuccess(Context context) {
        if (result == 1) {
            return true;
        } else {
            if (result == 400 && !StaticMembers.IS_NEED_LOGIN) {
                AppUtils.exitLogin(context);
                context.startActivity(new Intent(context, LoginRegisterActivity.class));
                AppUtils.showToast(context, remark);
            }
            return false;
        }
    }

    //获取到对象后，判断RecordDetail是否为null，为null则没有数据
    public boolean isNotNull() {
        return data != null && data.size() > 0;
    }

    @Override
    public String toString() {
        return "ReturnResultEntity{" +
                "result=" + result +
                ", remark='" + remark + '\'' +
                ", data=" + data +
                '}';
    }
}
