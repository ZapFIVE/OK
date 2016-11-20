package com.example.whr.libcommon.mvp.impl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whr.libcommon.mvp.BaseManager;

/**
 * Created by kinger on 2016/11/9.
 *
 * 视图统一管理类
 */
public class DelegateManager extends BaseManager<BaseDelegate> {

    /**
     * presenter
     */
    private PresenterManager mPresenter = new PresenterManager();

    @Override
    public void addItems(BaseDelegate item) {
        super.addItems(item);

        mPresenter.addItems(item.init());
    }

    @Override
    public void clear() {
        super.clear();

        mPresenter.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, View root) {
        super.onCreate(savedInstanceState, root);

        mPresenter.onCreate(savedInstanceState, root);
    }

    @Override
    public void onStart() {
        super.onStart();

        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        mPresenter.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mPresenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPresenter.onDestroy();

        // 清空数据
        clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mPresenter.onSaveInstanceState(outState);
    }

    @Override
    public Object getPresenter(Class className) {
        return mPresenter.getPresenter(className);
    }
}
