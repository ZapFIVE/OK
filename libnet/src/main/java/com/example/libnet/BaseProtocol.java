package com.example.libnet;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.libnet.cache.ICache;
import com.example.libnet.helper.BaseProxyNet;
import com.example.libnet.http.EnumPriority;
import com.example.libnet.http.EnumRequest;
import com.example.libnet.http.HttpRequest;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
     * 优先级线程池
     */
    private HashMap<EnumPriority, IProxyNet> mProxyNets;

    /**
     * 线程池
     */
    protected static final ExecutorService cacheExecutor = Executors.newCachedThreadPool();
    /**
     * 主线程Handler
     */
    protected static final Handler handler = new Handler(Looper.getMainLooper());

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

        mProxyNets = new HashMap<>();

        try {
            Class cls = Class.forName(BaseProxyNet.class.getPackage().getName() + ".Proxy" + mConfig.getLibNet());
            // 方法传入的类型
            Class[] paramTypes = {INetConfig.class};
            // 方法传入的参数
            Object[] params = {mConfig};
            // 创建构造器
            Constructor constructor = cls.getConstructor(paramTypes);

            // 生成对应优先级的线程池
            for (EnumPriority priority : EnumPriority.values()) {
                IProxyNet proxyNet = (IProxyNet) constructor.newInstance(params);
                mProxyNets.put(priority, proxyNet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("getLibNet required, check it; can not be empty or null or illegal.");
        }
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
    public boolean isEnableCache() {
        return mConfig.isEnableCache();
    }

    @Override
    public boolean isUIResponse() {
        return mConfig.isUIResponse();
    }

    @Override
    public ICache getCacheStrategy() {
        return mConfig.getCacheStrategy();
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
     * 请求路径
     *
     * @return
     */
    public abstract String  getPath();

    /**
     * 取消请求
     */
    public void cancel() {
    }

    /**
     * 发起请求
     * @param request 请求类型 get post ...
     * @param callback  请求回调
     */
    public void request(EnumRequest request, BaseProtocolCallback callback) {
        String baseUrl = mConfig.getBaseUrl();
        if (TextUtils.isEmpty(baseUrl)) {
            throw new NullPointerException("BaseUrl can not be null or empty");
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
    public void request(EnumRequest request, String url, BaseProtocolCallback callback) {
        // 如果有缓存,执行缓存


        // 检查网络代理类
        if (mProxyNets == null || mProxyNets.isEmpty() || !mProxyNets.containsKey(getPriority())) {
            throw new IllegalStateException("error, check it; not found proxy net");
        }

        HttpRequest opt = new HttpRequest.Builder(request, url)
            .setHeadMaps(mHeadMaps)
            .setParamMaps(mParamMaps)
            .setBody(mConfig.getConverterStrategy().requestBodyConverter(mBody))
            .build();

        // 发起请求
        mProxyNets.get(getPriority()).request(this, opt, callback);
    }
}
