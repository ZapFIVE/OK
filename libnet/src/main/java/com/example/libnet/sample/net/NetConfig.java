package com.example.libnet.sample.net;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.libnet.ICache;
import com.example.libnet.IConverter;
import com.example.libnet.INetConfig;
import com.example.libnet.R;
import com.example.libnet.converter.impl.GsonConverter;

/**
 * Created by whr on 2016/10/8.
 *
 * 网络默认配置，单例
 */
public enum NetConfig implements INetConfig {
    INSTANCE;

    /**
     * Gson转换类
     */
    private IConverter mGsonConverter = new GsonConverter();

    private Context mContext = null;

    /**
     * 设置Context
     *
     * @param context
     */
    public void setContext(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public Context getContext() {
        return mContext;
    }

    @NonNull
    @Override
    public String getBaseUrl() {
        return "https://api.github.com";
    }

    @Override
    public boolean isUIResponse() {
        return false;
    }

    @Override
    public boolean isFromCache() {
        return false;
    }

    @Override
    public ICache getCacheStrategy() {
        return null;
    }

    @NonNull
    @Override
    public IConverter getConverterStrategy() {
        return mGsonConverter;
    }

    @NonNull
    @Override
    public final String getLibNet() {
        return getContext().getString(R.string.lib_net);
    }


}
