package com.example.libimg.impl;

import android.app.Application;
import android.content.Context;

import com.example.libimg.IImageLoader;
import com.example.libimg.IImageLoaderBuilder;
import com.example.libimg.R;

import java.lang.reflect.Constructor;


/**
 * Created by kinger on 2016/11/24.
 *
 * ImageLoader 实现子例
 */
public class ImageLoader implements IImageLoader {
    /**
     * Application 的context
     */
    private Context mApplicationContext;
    /**
     * 是否已经初始化过
     */
    private boolean mIsInit = false;

    /**
     * 代理ImageLoader
     */
    private IImageLoader mProxyImg = null;

    public ImageLoader(Application application) {
        init(application);
    }

    /**
     * 初始化配置
     *
     * @param application    Application  全局context
     */
    public synchronized void init(Application application) {
        if (!mIsInit) {
            mIsInit = true;

            mApplicationContext = application;

            try {
                Class cls = Class.forName(BaseProxyImageLoader.class.getPackage().getName() + ".Proxy" + mApplicationContext.getString(R.string.lib_img));
                // 方法传入的类型
                Class[] paramTypes = {Context.class};
                // 方法传入的参数
                Object[] params = {mApplicationContext};
                // 创建构造器
                Constructor constructor = cls.getConstructor(paramTypes);
                mProxyImg = (IImageLoader) constructor.newInstance(params);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalStateException("ProxyImg init fail, check it; can not be empty or null or illegal.");
            }
        }
    }

    /**
     * 检查当前的初始化状态
     */
    private void checkStatus() {
        if (mProxyImg == null) {
            throw  new NullPointerException("ProxyImg is empty, please init it first");
        }
    }

    @Override
    public long size() {
        checkStatus();
        return mProxyImg.size();
    }

    @Override
    public void clear() {
        checkStatus();
        mProxyImg.clear();
    }

    @Override
    public IImageLoaderBuilder with() {
        checkStatus();
        return mProxyImg.with();
    }
}
