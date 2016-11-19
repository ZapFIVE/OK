package com.example.libnet;

import android.content.Context;
import android.support.annotation.NonNull;

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
     * 是否需要在主线程上回调
     *
     * @return 是否在主线程上回调
     */
    boolean isUIResponse();

    /**
     * 是否从缓存中获取
     *
     * @return
     */
    boolean isFromCache();

    /**
     * 获取缓存策略
     *
     * @return
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
     * 获取拦截器
     *
     * @return
     */
    IInterceptor getInterceptor();
}
