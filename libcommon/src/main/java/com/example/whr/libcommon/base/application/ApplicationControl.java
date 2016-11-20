package com.example.whr.libcommon.base.application;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.libutils.orm.sp.SharePreferenceUtils;

/**
 * Created by whr on 2016/11/20.
 *
 * 全局操作控制类
 *
 */
public enum  ApplicationControl implements IApplicationControl{
    INSTANCE;

    /**
     * ApplicationControl实际操作类
     */
    private IApplicationControl mInstace;

    /**
     * 初始化 控制类
     *
     * @param instance
     */
    protected void init(IApplicationControl instance) {
        mInstace = instance;
    }

    /**
     * 检查初始化工作状态
     */
    private void checkInitStatus() {
        if (mInstace == null) {
            throw new NullPointerException("applicaton control is not init. please check your application.");
        }
    }


    @Override
    public boolean isDev() {
        checkInitStatus();
        return mInstace.isDev();
    }

    @Override
    public void optOpenDebug() {
        checkInitStatus();
        mInstace.optOpenDebug();
    }

    @NonNull
    @Override
    public Context getAppContext() {
        checkInitStatus();
        return mInstace.getAppContext();
    }

    @NonNull
    @Override
    public SharePreferenceUtils getSharePreference() {
        checkInitStatus();
        return mInstace.getSharePreference();
    }

    @Override
    public Object getAppService(@NonNull String name) {
        checkInitStatus();
        return mInstace.getAppService(name);
    }
}
