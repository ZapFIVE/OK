package com.example.libnet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.libnet.exception.opt.NullPointerUrlException;
import com.example.libnet.http.EnumPriority;
import com.example.libnet.http.EnumRequest;
import com.example.libnet.http.HttpRequest;

import java.util.HashMap;

/**
 * Created by whr on 2016/10/8.
 *
 *  协议基类
 * @param <T>
 */
public abstract class BaseProtocol<T> implements INetConfig{
    /**
     * 默认配置
     */
    private final INetConfig mConfig;

    /**
     * 本次协议的标签
     */
    private String mTag = "";

    /**
     * 请求表头列表
     */
    private HashMap<String, String> mHeadMaps = null;
    /**
     * 请求参数列表
     */
    private HashMap<String, String> mParamMaps = null;

    /**
     * 请求body 对象，File文件...
     */
    private T mBody = null;

    /**
     * 是否从缓存中获取
     */
    protected boolean mIsFromCache;
    /**
     * 是否在主线程回调
     */
    protected boolean mIsUiResponse;

    /**
     * 默认构造函数
     *
     * @param config 默认网络配置
     */
    public BaseProtocol(INetConfig config) {
        if (config == null) {
            throw new NullPointerException("INetConfig can not be null!");
        }
        mConfig = config;

        mIsFromCache = mConfig.isFromCache();
        mIsUiResponse = mConfig.isUIResponse();

        NetHelper.INSTANCE.init(config);
    }

    @NonNull
    @Override
    public final Context getContext() {
        return mConfig.getContext();
    }

    @NonNull
    @Override
    public String getBaseUrl() {
        return mConfig.getBaseUrl();
    }

    @Override
    public boolean isUIResponse() {
        return mIsUiResponse;
    }

    @NonNull
    @Override
    public IConverter getConverterStrategy() {
        return mConfig.getConverterStrategy();
    }

    @NonNull
    @Override
    public final String getLibNet() {
        return mConfig.getLibNet();
    }

    @Override
    public boolean isFromCache() {
        return mIsFromCache;
    }

    @Override
    public ICache getCacheStrategy() {
        return mConfig.getCacheStrategy();
    }

    /**
     * 设置是否从缓存中获取
     *
     * @param isFromCache
     */
    public void setFromCache(boolean isFromCache) {
        this.mIsFromCache = isFromCache;
    }

    /**
     * 设置是否在主线程上回调
     *
     * @param isUiResponse
     */
    public void setUiResponse(boolean isUiResponse) {
        this.mIsUiResponse = isUiResponse;
    }

    /**
     * 获取本次协议的标签
     *
     * @return 默认为""
     */
    public String getTag() {
        if (TextUtils.isEmpty(mTag)) {
            return "";
        }
        return mTag;
    }

    /**
     * 设置本次协议的标签
     *
     * @param tag
     */
    public void setTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        mTag = tag;
    }

    /**
     * 添加 http 头
     *
     * @param key http头 key
     * @param value http头 value
     * @return 是否添加成功
     */
    public boolean putHead(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return false;
        }
        if (mHeadMaps == null) {
            mHeadMaps = new HashMap<>();
        }

        mHeadMaps.put(key, value);

        return true;
    }

    /**
     * 删除http 头
     *
     * @param key http头 key
     */
    public void removeHead(String key) {
        if (mHeadMaps != null) {
            mHeadMaps.remove(key);
        }
    }

    /**
     * 添加查询params
     *
     * @param key params key
     * @param value params value
     * @return 是否添加成功
     */
    public boolean putParams(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return false;
        }
        if (mParamMaps == null) {
            mParamMaps = new HashMap<>();
        }

        mParamMaps.put(key, value);

        return true;
    }

    /**
     * 删除params
     *
     * @param key params key
     */
    public void removeParams(String key) {
        if (mParamMaps != null) {
            mParamMaps.remove(key);
        }
    }

    /**
     * 设置 http body
     *
     * @param body 对象，File文件...
     */
    public void setBody(T body) {
        mBody = body;
    }

    /**
     * 获取 http body
     *
     * @return body对象，File文件...
     */
    public T getBody() {
        return mBody;
    }

    /**
     * 是否处于高优先级
     *
     * @return 是否处于高优先级
     */
    public EnumPriority getPriority() {
        return EnumPriority.NORMAL;
    }

    /**
     * 获取协议的CacheKey
     *
     * @return
     */
    public String getProtocolCacheKey() {
        return this.getClass().getName() + "$" + this.hashCode();
    }

    /**
     * 请求路径
     *
     * @return
     */
    public abstract String  getPath();

    /**
     * 取消请求
     */
    public void cancel() {
        NetHelper.INSTANCE.cancel(this);
    }

    /**
     * 发起请求
     * @param request 请求类型 get post ...
     * @param callback  请求回调
     */
    public void request(EnumRequest request, BaseProtocolCallback callback) {
        String baseUrl = mConfig.getBaseUrl();
        if (TextUtils.isEmpty(baseUrl)) {
            throw new NullPointerUrlException("BaseUrl can not be null or empty");
        }
        request(request, mConfig.getBaseUrl() + getPath(), callback);
    }

    /**
     * 发起请求
     *
     * @param request 请求类型 get post ...
     * @param url 请求地址
     * @param callback    请求回调
     */
    public void request(final EnumRequest request, final String url, final BaseProtocolCallback callback) {
        requestFromProxyNet(request, url, callback);
    }

    /**
     * 从网络上获取
     *
     * @param request 请求类型 get post ...
     * @param url 请求地址
     * @param callback    请求回调
     */
    private void requestFromProxyNet(EnumRequest request, String url, BaseProtocolCallback callback) {
        HttpRequest opt = new HttpRequest.Builder(request, url)
            .setHeadMaps(mHeadMaps)
            .setParamMaps(mParamMaps)
            .setBody(mConfig.getConverterStrategy().requestBodyConverter(mBody))
            .build();

        NetHelper.INSTANCE.request(this, opt, callback);
    }
}
