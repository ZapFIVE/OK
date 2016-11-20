package com.example.whr.libcommon.mvp.impl;

import android.support.annotation.NonNull;

import com.example.whr.libcommon.mvp.BaseClass;
import com.example.whr.libcommon.mvp.IHostControl;

/**
 * Created by kinger on 2016/11/9.
 * Presenter 代理基类
 * @param <V>
 */
public abstract class BasePresenter<V extends BaseDelegate> extends BaseClass {

    /**
     * 视图
     */
    private V mView;

    public BasePresenter(@NonNull IHostControl control, V view) {
        super(control);
        mView = view;
        initView();
    }

    @SuppressWarnings("unchecked")
    private void initView() {
        getView().setPresenter(this);
    }

    /**
     * 获取视图层
     *
     * @return
     */
    public V getView() {
        return mView;
    }
}
