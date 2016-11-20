package com.example.libutils.lifecycle;

import android.app.Application;

/**
 * Created by kinger on 2016/11/5.
 *
 * 特殊的内容控制 (生命周期)
 */
public interface ILifeControl {
    /**
     * 注册Activity生命周期变化监听
     *
     * @param callback
     */
    void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback);

    /**
     * 注销Activity生命周期变化监听
     *
     * @param callback
     */
    void unregisterActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback);
}
