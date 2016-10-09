package com.example.libnet;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.libnet.cache.ICache;

/**
 * Created by whr on 2016/10/8.
 *
 * 网络通信配置类
 */
public interface INetConfig {

    /**
     * 获取Context 最好是ApplicationContext 常用于初始化
     *
     * @return Context
     */
    @NonNull
    Context getContext();

    /**
     * 获取默认Url 不允许为null 或empty
     *
     * @return 默认URL
     */
    @NonNull
    String getBaseUrl();
    /**
     * 是否默认开启缓存
     *
     * @return 是否开启缓存
     */
    boolean isEnableCache();

    /**
     * 是否需要在主线程上回调
     *
     * @return 是否在主线程上回调
     */
    boolean isUIResponse();

    /**
     * 获取缓存策略
     *
     * @return 缓存策略
     */
    ICache getCacheStrategy();

    /**
     * 获取转换策略
     *
     * @return
     */
    @NonNull
    IConverter getConverterStrategy();
    /**
     * 获取执行网络操作的封装库
     *
     * @return
     */
    @NonNull
    String getLibNet();
}
