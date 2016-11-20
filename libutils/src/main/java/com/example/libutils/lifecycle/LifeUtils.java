package com.example.libutils.lifecycle;

import android.app.Application;

/**
 * Created by kinger on 2016/11/5.
 *
 * 生命周期实现类
 *
 */
public class LifeUtils implements ILifeControl {

    /**
     * Application实例
     */
    private final Application mApplication;

    public LifeUtils(Application application) {
        mApplication = application;
    }

    @Override
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        mApplication.registerActivityLifecycleCallbacks(callback);
    }

    @Override
    public void unregisterActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        mApplication.unregisterActivityLifecycleCallbacks(callback);
    }
}
