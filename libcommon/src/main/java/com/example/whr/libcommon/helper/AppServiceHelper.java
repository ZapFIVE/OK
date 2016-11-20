package com.example.whr.libcommon.helper;

import android.app.Application;
import android.support.annotation.NonNull;

import com.example.libutils.lifecycle.ILifeControl;
import com.example.libutils.lifecycle.LifeUtils;

import java.util.HashMap;

/**
 * Created by whr on 2016/11/20.
 *
 * 系统服务帮助类
 */
public enum  AppServiceHelper {
    INSTANCE;

    /**
     * Activity life cycle {@link com.example.libutils.lifecycle.ILifeControl}
     *
     * 最终获取Activity的生命周期实例
     */
    public static final String SERVICE_LIFT_CYCLE = "SERVICE_LIFT_CYCLE";

    /**
     * App服务
     */
    private HashMap<String, Object> mAppServices = new HashMap<>();
    /**
     * 获取系统服务
     *
     * @param application    application
     * @param name 服务名称
     * @return
     */
    public Object get(@NonNull Application application, @NonNull String name) {
        return mAppServices.containsKey(name) ? mAppServices.get(name) : init(application, name);
    }

    /**
     * 系统服务初始化
     *
     * @param application
     * @param name
     * @return
     */
    private Object init(Application application, String name) {
        if (SERVICE_LIFT_CYCLE.equals(name)) {
            initLifeCycle(application, SERVICE_LIFT_CYCLE);
        }

        return mAppServices.containsKey(name) ? mAppServices.get(name) : null;
    }

    /**
     * 初始化生命周期监控
     *
     * @param application
     * @param name
     */
    private void initLifeCycle(Application application, String name) {
        ILifeControl lifeControl = new LifeUtils(application);
        mAppServices.put(name, lifeControl);
    }

}
