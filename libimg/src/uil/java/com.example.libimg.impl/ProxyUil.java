package com.example.libimg.impl;

import android.content.Context;
import android.widget.ImageView;

import com.example.libimg.IImageLoaderBuilder;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;


/**
 * Created by kinger on 2016/11/24.
 *
 * Uil的代理
 */
public class ProxyUil extends BaseProxyImageLoader{

    public ProxyUil(Context context) {
        super(context);

        File cacheDir = StorageUtils.getCacheDirectory(context);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(false)                               //启用内存缓存
            .cacheOnDisk(true)                                 //启用外存缓存
            .considerExifParams(true)                          //启用EXIF和JPEG图像格式
            .displayer(new RoundedBitmapDisplayer(20))         //设置显示风格这里是圆角矩形
            .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
            .memoryCacheExtraOptions(480, 800)
            .threadPoolSize(3)// 线程池内加载的数量
            .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
            .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You
            .memoryCacheSize(2 * 1024 * 1024)
            .discCacheFileNameGenerator(new Md5FileNameGenerator())// 将保存的时候的URI名称用MD5
            .tasksProcessingOrder(QueueProcessingType.LIFO)
            .discCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
            .defaultDisplayImageOptions(options)
            .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
            .writeDebugLogs() // Remove for release app
            .build();// 开始构建


        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

    @Override
    public long size() {
        File cacheDir =  com.nostra13.universalimageloader.core.ImageLoader.getInstance().getDiskCache().getDirectory();
        if (cacheDir != null && cacheDir.isDirectory()) {
            return cacheDir.getTotalSpace();
        }

        return 0L;
    }

    @Override
    public void clear() {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().clearDiskCache();
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
            DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(mPlaceHolderId).showImageOnFail(mErrorId).build();
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(mUrl, targetView, options);
        }
    }
}
