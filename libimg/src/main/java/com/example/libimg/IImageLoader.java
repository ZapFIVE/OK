package com.example.libimg;

/**
 * Created by kinger on 2016/11/24.
 *
 * ImageLoader
 */
public interface IImageLoader {

    /**
     * 获取当前图片缓存的大小
     *
     * @return
     */
    long size();

    /**
     * 清空所有的缓存
     *
     * 注意 不要在主线程上执行 这样的耗时操作
     */
    void clear();

    /**
     * ImageLoaderBuilder
     *
     * 默认是Application Context
     *
     * @return IImageLoaderBuilder
     */
    IImageLoaderBuilder with();
}
