package com.example.libnet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.libnet.exception.net.NoNetException;
import com.example.libnet.exception.opt.IllegalLibNetException;
import com.example.libnet.exception.opt.NoneCacheException;
import com.example.libnet.exception.opt.NullPointerCacheException;
import com.example.libnet.helper.BaseProxyNet;
import com.example.libnet.http.EnumPriority;
import com.example.libnet.http.EnumProtocolStatus;
import com.example.libnet.http.HttpRequest;
import com.example.libnet.http.HttpResponse;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by whr on 2016/10/10.
 *
 * 网络操作帮助类
 */
public enum  NetHelper implements com.example.libnet.IProxyNet {
    INSTANCE;

    /**
     * 回调池
     */
    private final ConcurrentHashMap<String, BaseProtocolCallbackWrapper> mCallbackCache = new ConcurrentHashMap<>();


    /**
     * 优先级线程池
     */
    private HashMap<EnumPriority, com.example.libnet.IProxyNet> mProxyNets;

    /**
     * 线程池
     */
    protected static final ExecutorService mCacheExecutor = Executors.newCachedThreadPool();
    /**
     * 主线程Handler
     */
    protected static final Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 是否已经初始化过
     */
    private boolean mIsInit = false;

    /**
     * 初始化配置
     *
     * @param config  网络配置
     */
    public synchronized void init(com.example.libnet.INetConfig config) {
        if (!mIsInit) {
            mIsInit = true;

            mProxyNets = new HashMap<>();

            try {
                Class cls = Class.forName(BaseProxyNet.class.getPackage().getName() + ".Proxy" + config.getContext().getString(R.string.lib_net));
                // 方法传入的类型
                Class[] paramTypes = {com.example.libnet.INetConfig.class};
                // 方法传入的参数
                Object[] params = {config};
                // 创建构造器
                Constructor constructor = cls.getConstructor(paramTypes);

                // 生成对应优先级的线程池
                for (EnumPriority priority : EnumPriority.values()) {
                    com.example.libnet.IProxyNet proxyNet = (com.example.libnet.IProxyNet) constructor.newInstance(params);
                    mProxyNets.put(priority, proxyNet);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalLibNetException("getLibNet required, check it; can not be empty or null or illegal.");
            }
        }
    }


    @Override
    public void cancel(com.example.libnet.BaseProtocol protocol) {
        // 加入回调池
        BaseProtocolCallbackWrapper call = mCallbackCache.remove(protocol.getProtocolCacheKey());
        if (call != null && !call.isExecuted() && !call.isCanceled()) {
            call.cancel(protocol);
        }

        mProxyNets.get(protocol.getPriority()).cancel(protocol);
    }

    @Override
    public void request(final com.example.libnet.BaseProtocol protocol, final HttpRequest request, final com.example.libnet.IProtocolCallback callback) {
        // 防止耗时操作
        mCacheExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // 拦截器处理
                if (!requestFromInterceptor(protocol, request, callback)) {
                    // 从缓存中获取
                    if (protocol.isFromCache()) {
                        requestFromCache(protocol, request, callback);
                    } else {
                        requestFromNet(protocol, request, callback);
                    }
                }
            }
        });
    }

    /**
     * 从拦截器中操作
     *
     * @param protocol
     * @param request
     * @param callback
     * @return 是否被拦截器拦截
     */
    private boolean requestFromInterceptor(final com.example.libnet.BaseProtocol protocol, final HttpRequest request, final com.example.libnet.IProtocolCallback callback) {
        com.example.libnet.IInterceptor interceptor = protocol.getInterceptor();

        if (interceptor != null) {
            BaseProtocolCallbackWrapper wrapper = new BaseProtocolCallbackWrapper(callback);
            // 加入回调池
            mCallbackCache.put(protocol.getProtocolCacheKey(), wrapper);
            // 通知请求开始了
            wrapper.onStatusChange(protocol, EnumProtocolStatus.PROTOCOL_BEGIN);
            interceptor.request(protocol, request, wrapper);

            return true;
        }
        return false;
    }

    /**
     * 从缓存中获取
     *
     * @param protocol 协议实例
     * @param request  请求类型
     * @param callback 请求回调
     */
    private void requestFromCache(final com.example.libnet.BaseProtocol protocol, final HttpRequest request, final com.example.libnet.IProtocolCallback callback) {
        ICache cache = protocol.getCacheStrategy();
        BaseProtocolCallbackWrapper wrapper = new BaseProtocolCallbackWrapper(callback);

        // 加入回调池
        mCallbackCache.put(protocol.getProtocolCacheKey(), wrapper);
        // 通知请求开始了
        wrapper.onStatusChange(protocol, EnumProtocolStatus.PROTOCOL_BEGIN);

        // 没有缓存策略
        if (cache == null) {
            wrapper.onError(protocol, new NullPointerCacheException("ICache is null, cannot from cache"));
        } else {
            HttpResponse response = cache.get(request);
            // 没有缓存
            if (response == null) {
                wrapper.onError(protocol, new NoneCacheException("none cache"));
            } else {
                wrapper.onResponse(protocol, response);
            }
        }
    }
    /**
     * 从网络中获取
     *
     * @param protocol 协议实例
     * @param request  请求类型
     * @param callback 请求回调
     */
    private void requestFromNet(final com.example.libnet.BaseProtocol protocol, final HttpRequest request, final com.example.libnet.IProtocolCallback callback) {
        // 检查网络代理类
        if (mProxyNets == null || mProxyNets.isEmpty()) {
            throw new IllegalLibNetException("error, check it; not found proxy net");
        }

        CacheBaseProtocolCallbackWrapper wrapper = new CacheBaseProtocolCallbackWrapper(request, callback);
        // 加入回调池
        mCallbackCache.put(protocol.getProtocolCacheKey(), wrapper);
        // 通知请求开始了
        wrapper.onStatusChange(protocol, EnumProtocolStatus.PROTOCOL_BEGIN);

        // 检测网络
        ConnectivityManager cm = (ConnectivityManager) protocol.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            wrapper.onError(protocol, new NoNetException());
            return;
        }

        // 发起请求
        mProxyNets.get(protocol.getPriority()).request(protocol, request, wrapper);
    }

    private class CacheBaseProtocolCallbackWrapper extends BaseProtocolCallbackWrapper {
        private final HttpRequest mRequest;
        public CacheBaseProtocolCallbackWrapper(HttpRequest request, com.example.libnet.IProtocolCallback callback) {
            super(callback);
            mRequest = request;
        }

        @Override
        public void onResponse(com.example.libnet.BaseProtocol protocol, final HttpResponse response) {
            super.onResponse(protocol, response);
            final ICache cache = protocol.getCacheStrategy();
            // 缓存
            if (cache != null) {
                // 防止耗时操作
                if (isThreadInUI()) {
                    mCacheExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            cache.put(mRequest, response);
                        }
                    });
                } else {
                    cache.put(mRequest, response);
                }
            }
        }
    }

    /**
     * 协议操作的回调包装
     */
    private class BaseProtocolCallbackWrapper implements com.example.libnet.IProtocolCallback {

        /**
         * 回调
         */
        private final com.example.libnet.IProtocolCallback mCallback;

        /**
         * 是否已经取消
         */
        protected boolean mIsCanceled = false;

        /**
         * 是否已经执行了
         */
        protected boolean mIsExecuted = false;

        public BaseProtocolCallbackWrapper(com.example.libnet.IProtocolCallback callback) {
            mCallback = callback;
        }

        @Override
        public void onResponse(final com.example.libnet.BaseProtocol protocol, final HttpResponse response) {
            // 直接运行
            if (isRunDirect(protocol)) {
                onRealOnResponse(protocol, response);
            }
            // 在UI上运行
            else if (protocol.isUIResponse() && !isThreadInUI()) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onRealOnResponse(protocol, response);
                    }
                });
            }
            // 在线程上运行
            else {
                mCacheExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        onRealOnResponse(protocol, response);
                    }
                });
            }
        }

        @Override
        public void onError(final com.example.libnet.BaseProtocol protocol, final Throwable throwable) {
            // 直接运行
            if (isRunDirect(protocol)) {
                onRealOnError(protocol, throwable);
            }
            // 在UI上运行
            else if (protocol.isUIResponse() && !isThreadInUI()) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onRealOnError(protocol, throwable);
                    }
                });
            }
            // 在线程上运行
            else {
                mCacheExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        onRealOnError(protocol, throwable);
                    }
                });
            }
        }

        @Override
        public void onStatusChange(final com.example.libnet.BaseProtocol protocol, final EnumProtocolStatus status) {
            // 直接运行
            if (isRunDirect(protocol)) {
                onRealStatusChange(protocol, status);
            }
            // 在UI上运行
            else if (protocol.isUIResponse() && !isThreadInUI()) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onRealStatusChange(protocol, status);
                    }
                });
            }
            // 在线程上运行
            else {
                mCacheExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        onRealStatusChange(protocol, status);
                    }
                });
            }
        }

        /**
         * 是否已经取消
         *
         * @return
         */
        public boolean isCanceled() {
            synchronized (this) {
                return mIsCanceled;
            }
        }

        /**
         * 是否已经执行了
         *
         * @return
         */
        public boolean isExecuted() {
            synchronized (this) {
                return mIsExecuted;
            }
        }

        /**
         * 设置已经执行了
         * @return 是否第一次执行成功
         */
        private boolean setExecuted() {
            synchronized (this) {
                if (mIsCanceled || mIsExecuted) {
                    return false;
                }

                this.mIsExecuted = true;

                return true;
            }
        }

        /**
         * 设置已经取消了
         * @return 是否第一次取消成功
         */
        private boolean setCancel() {
            synchronized (this) {
                if (mIsExecuted || mIsCanceled) {
                    return false;
                }
                this.mIsCanceled = true;

                return true;
            }
        }

        /**
         * 设置取消
         * @param protocol  协议
         */
        public void cancel(final com.example.libnet.BaseProtocol protocol) {
            if (setCancel()) {
                // 通知协议结束
                onStatusChange(protocol, EnumProtocolStatus.PROTOCOL_CANCEL);
            }
        }

        /**
         * 实际执行的正确回调
         *
         * @param protocol
         * @param response
         */
        private void onRealOnResponse(final com.example.libnet.BaseProtocol protocol, final HttpResponse response){
            if (setExecuted()) {
                // 从回调池中清除
                mCallbackCache.remove(protocol.getProtocolCacheKey());
                mCallback.onResponse(protocol, response);

                // 通知协议结束
                onStatusChange(protocol, EnumProtocolStatus.PROTOCOL_END);
            }
        }

        /**
         * 实际执行的错误回调
         *
         * @param protocol
         * @param throwable
         */
        private void onRealOnError(final com.example.libnet.BaseProtocol protocol, final Throwable throwable) {
            if (setExecuted()) {
                // 从回调池中清除
                mCallbackCache.remove(protocol.getProtocolCacheKey());

                mCallback.onError(protocol, throwable);
                // 通知协议结束
                onStatusChange(protocol, EnumProtocolStatus.PROTOCOL_END);
            }
        }

        /**
         * 实际执行的状态回调
         *
         * @param protocol
         * @param status
         */
        private void onRealStatusChange(final com.example.libnet.BaseProtocol protocol, final EnumProtocolStatus status) {

            Log.d("net_test", protocol.getPath() + "," + status.toString());
            mCallback.onStatusChange(protocol, status);
        }

        /**
         * 是否线程运行在主线程上
         *
         * @return
         */
        protected boolean isThreadInUI() {
            return Thread.currentThread() == Looper.getMainLooper().getThread();
        }

        /**
         * 是否直接运行
         *
         * @param protocol
         * @return
         */
        protected boolean isRunDirect(final com.example.libnet.BaseProtocol protocol) {
            boolean isOkInUI = isThreadInUI() && protocol.isUIResponse();
            boolean isOkInNonUI = !isThreadInUI() && !protocol.isUIResponse();

            if (isOkInUI || isOkInNonUI) {
                return true;
            }

            return false;
        }
    }
}
