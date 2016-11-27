package com.example.libimg;

import android.widget.ImageView;

/**
 * Created by kinger on 2016/11/24.
 *
 * ImageLoaderBuilder
 */
public interface IImageLoaderBuilder {

    /**
     * 需要加载的图片地址
     *
     * @param url 图片地址
     * @return
     */
    IImageLoaderBuilder load(String url);

    /**
     * 设置 PlaceHolder 占位符
     *
     * @param resId 资源文件id
     * @return IImageLoader
     */
    IImageLoaderBuilder setPlaceHolder(int resId);

    /**
     * 设置加载失败的 占位符
     *
     * @param errId 资源文件id
     * @return IImageLoader
     */
    IImageLoaderBuilder setErrorHolder(int errId);


    /**
     * 将设置加载进配置View
     *
     * @param targetView 目标ImageView视图
     */
    void into(ImageView targetView);
}
