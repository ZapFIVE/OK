package com.example.libimg.impl;

import android.content.Context;

import com.example.libimg.IImageLoader;


/**
 * Created by kinger on 2016/11/24.
 *
 * 代理ImageLoaer 基类
 */
public abstract class BaseProxyImageLoader implements IImageLoader {

    /**
     * Application 的context
     */
    private Context mApplicationContext;

    public BaseProxyImageLoader(Context application) {
        mApplicationContext = application.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return
     */
    protected Context getAppContext() {
        return mApplicationContext;
    }
}
