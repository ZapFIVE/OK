package com.example.libimg.impl;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.libimg.IImageLoaderBuilder;

import java.io.File;


/**
 * Created by kinger on 2016/11/24.
 *
 * ProxyGlide 代理类
 */
public class ProxyGlide extends BaseProxyImageLoader {

    public ProxyGlide(Context application) {
        super(application);
    }

    @Override
    public long size() {
        File cacheDir = Glide.getPhotoCacheDir(getAppContext());
        if (cacheDir != null && cacheDir.isDirectory()) {
            return cacheDir.getTotalSpace();
        }

        return 0L;
    }

    @Override
    public void clear() {
        Glide.get(getAppContext()).clearDiskCache();
    }

    @Override
    public IImageLoaderBuilder with() {
        return new ImageLoaderBuilder();
    }

    /**
     * Builder
     */
    private class ImageLoaderBuilder implements IImageLoaderBuilder {

        /**
         * 图片地址
         */
        private String mUrl = null;

        /**
         * 占位ID
         */
        private int mPlaceHolderId = 0;

        /**
         * 错误id
         */
        private int mErrorId = 0;

        @Override
        public IImageLoaderBuilder load(String url) {
            mUrl = url;
            return this;
        }

        @Override
        public IImageLoaderBuilder setPlaceHolder(int resId) {
            mPlaceHolderId = resId;
            return this;
        }

        @Override
        public IImageLoaderBuilder setErrorHolder(int errId) {
            mErrorId = errId;
            return this;
        }

        @Override
        public void into(ImageView targetView) {
            Glide.with(getAppContext()).load(mUrl).crossFade().placeholder(mPlaceHolderId).error(mErrorId).into(targetView);
        }
    }
}
