package com.example.whr.libcommon.base.show;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.whr.libcommon.mvp.IHostControl;
import com.example.whr.libcommon.mvp.impl.DelegateManager;

/**
 * Created by kinger on 2016/11/3.
 *
 * 基类 Fragment
 */
public abstract class BaseFragment extends Fragment implements IHostControl{
    /**
     * DelegateManager
     */
    private DelegateManager mDelegate = new DelegateManager();

    @Override
    public DelegateManager getDelegateManager() {
        return mDelegate;
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public boolean isActivityFinishing() {
        return getActivity().isFinishing();
    }

    @Override
    public Object getPresenter(Class className) {
        return mDelegate.getPresenter(className);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 初始化Delegate
        initDelegate();

        mDelegate.onCreate(savedInstanceState, getView());
    }

    @Override
    public void onStart() {
        super.onStart();

        mDelegate.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        mDelegate.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        mDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mDelegate.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mDelegate.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        mDelegate.onSaveInstanceState(outState);
    }

    /**
     * 初始化delegate
     */
    public abstract void initDelegate();
}
