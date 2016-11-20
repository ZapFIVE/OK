package com.example.whr.libcommon.mvp.impl;

import android.support.annotation.NonNull;

import com.example.whr.libcommon.mvp.BaseClass;
import com.example.whr.libcommon.mvp.IHostControl;

/**
 * Created by kinger on 2016/11/9.
 *
 * 视图代理基类
 * @param <P>    presenter
 */
public abstract class BaseDelegate<P extends BasePresenter> extends BaseClass implements IHostControl{

    private P mPresenter;

    public BaseDelegate(@NonNull IHostControl control) {
        super(control);
    }

    /**
     * 获取presenter
     *
     * @return presenter
     */
    protected abstract P init();

    /**
     * 设置Presenter
     *
     * @param presenter
     */
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }


    /**
     * 获取Presenter
     *
     * @return
     */
    public P getPresenter() {
        return mPresenter;
    }
}
