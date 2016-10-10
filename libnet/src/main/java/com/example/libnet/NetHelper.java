package com.example.libnet;

import android.os.Handler;
import android.os.Looper;

import com.example.libnet.exception.opt.IllegalLibNetException;
import com.example.libnet.exception.opt.NoneCacheException;
import com.example.libnet.exception.opt.NullPointerCacheException;
import com.example.libnet.helper.BaseProxyNet;
import com.example.libnet.http.EnumPriority;
import com.example.libnet.http.HttpRequest;
import com.example.libnet.http.HttpResponse;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by whr on 2016/10/10.
 *
 * 网络操作帮助类
 */
public enum  NetHelper implements IProxyNet{
    INSTANCE;


    /**
     * 优先级线程池
     */
    private HashMap<EnumPriority, IProxyNet> mProxyNets;

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
    public synchronized void init(INetConfig config) {
        if (!mIsInit) {
            mIsInit = true;

            mProxyNets = new HashMap<>();

            try {
                Class cls = Class.forName(BaseProxyNet.class.getPackage().getName() + ".Proxy" + config.getLibNet());
                // 方法传入的类型
                Class[] paramTypes = {INetConfig.class};
                // 方法传入的参数
                Object[] params = {config};
                // 创建构造器
                Constructor constructor = cls.getConstructor(paramTypes);

                // 生成对应优先级的线程池
                for (EnumPriority priority : EnumPriority.values()) {
                    IProxyNet proxyNet = (IProxyNet) constructor.newInstance(params);
                    mProxyNets.put(priority, proxyNet);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalLibNetException("getLibNet required, check it; can not be empty or null or illegal.");
            }
        }
    }


    @Override
    public void cancel(BaseProtocol protocol) {
        mProxyNets.get(protocol.getPriority()).cancel(protocol);
    }

    @Override
    public void request(final BaseProtocol protocol, final HttpRequest request, final IProtocolCallback callback) {
        // 防止耗时操作
        mCacheExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // 从缓存中获取
                if (protocol.isFromCache()) {
                    requestFromCache(protocol, request, callback);
                } else {
                    requestFromNet(protocol, request, callback);
                }
            }
        });
    }

    /**
     * 从缓存中获取
     *
     * @param protocol 协议实例
     * @param request  请求类型
     * @param callback 请求回调
     */
    private void requestFromCache(final BaseProtocol protocol, final HttpRequest request, final IProtocolCallback callback) {
        ICache cache = protocol.getCacheStrategy();
        BaseProtocolCallbackWrapper wrapper = new BaseProtocolCallbackWrapper(callback);
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
    private void requestFromNet(final BaseProtocol protocol, final HttpRequest request, final IProtocolCallback callback) {
        // 检查网络代理类
        if (mProxyNets == null || mProxyNets.isEmpty()) {
            throw new IllegalLibNetException("error, check it; not found proxy net");
        }

        // 发起请求
        mProxyNets.get(protocol.getPriority()).request(protocol, request, new CacheBaseProtocolCallbackWrapper(request, callback));
    }

    private class CacheBaseProtocolCallbackWrapper extends BaseProtocolCallbackWrapper {
        private final HttpRequest mRequest;
        public CacheBaseProtocolCallbackWrapper(HttpRequest request, IProtocolCallback callback) {
            super(callback);
            mRequest = request;
        }

        @Override
        public void onResponse(BaseProtocol protocol, final HttpResponse response) {
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
    private class BaseProtocolCallbackWrapper implements IProtocolCallback{

        private final IProtocolCallback mCallback;

        public BaseProtocolCallbackWrapper(IProtocolCallback callback) {
            mCallback = callback;
        }

        @Override
        public void onResponse(final BaseProtocol protocol, final HttpResponse response) {
            // 直接运行
            if (isRunDirect(protocol)) {
                mCallback.onResponse(protocol, response);
            }
            // 在UI上运行
            else if (protocol.isUIResponse() && !isThreadInUI()) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onResponse(protocol, response);
                    }
                });
            }
            // 在线程上运行
            else {
                mCacheExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onResponse(protocol, response);
                    }
                });
            }
        }

        @Override
        public void onError(final BaseProtocol protocol, final Throwable throwable) {
            // 直接运行
            if (isRunDirect(protocol)) {
                mCallback.onError(protocol, throwable);
            }
            // 在UI上运行
            else if (protocol.isUIResponse() && !isThreadInUI()) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onError(protocol, throwable);
                    }
                });
            }
            // 在线程上运行
            else {
                mCacheExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onError(protocol, throwable);
                    }
                });
            }
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
        protected boolean isRunDirect(final BaseProtocol protocol) {
            boolean isOkInUI = isThreadInUI() && protocol.isUIResponse();
            boolean isOkInNonUI = !isThreadInUI() && !protocol.isUIResponse();

            if (isOkInUI || isOkInNonUI) {
                return true;
            }

            return false;
        }
    }
}
