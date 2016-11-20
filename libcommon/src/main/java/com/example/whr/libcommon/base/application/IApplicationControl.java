package com.example.whr.libcommon.base.application;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.libutils.orm.sp.SharePreferenceUtils;

/**
 * Created by whr on 2016/11/20.
 *
 * ApplicationControl 控制类接口
 */
public interface IApplicationControl {
    /**
     * 是否开启调试模式
     *
     * @return
     */
    boolean isDev();

    /**
     * 操作debug页面
     */
    void optOpenDebug();

    /**
     * 获取应用的Application的Context
     *
     * @return ApplicationContext
     */
    @NonNull
    Context getAppContext();

    /**
     * 获取全局的SharePrefrence
     *
     * @return
     */
    @NonNull
    SharePreferenceUtils getSharePreference();
    /**
     * 获取系统服务
     *
     * @param name
     * @return
     */
    Object getAppService(@NonNull String name);
}
