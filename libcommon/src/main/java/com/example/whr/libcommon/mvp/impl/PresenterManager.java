package com.example.whr.libcommon.mvp.impl;


import com.example.whr.libcommon.mvp.BaseManager;

/**
 * Created by kinger on 2016/11/9.
 *
 * Presenter 统一管理类
 */
public class PresenterManager extends BaseManager<BasePresenter> {

    @Override
    public Object getPresenter(Class className) {
        for (BasePresenter presenter : mArrays) {
            if (presenter.getClass().equals(className)) {
                return presenter;
            }
        }
        return null;
    }
}
