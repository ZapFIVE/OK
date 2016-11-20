package com.example.whr.libcommon.mvp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.libutils.orm.sp.SharePreferenceUtils;
import com.example.whr.libcommon.base.application.ApplicationControl;
import com.example.whr.libcommon.base.application.IApplicationControl;
import com.example.whr.libcommon.mvp.impl.DelegateManager;

/**
 * Created by kinger on 2016/11/9.
 *
 * 所有操作的基类
 */
public class BaseClass implements IHostStatus,IHostControl, IApplicationControl{

    /**
     * HostControl
     */
    @NonNull
    private IHostControl mHostControl;

    public BaseClass(@NonNull IHostControl control) {
        mHostControl = control;
    }


    @Override
    public void onCreate(Bundle savedInstanceState, View root) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public DelegateManager getDelegateManager() {
        return mHostControl.getDelegateManager();
    }

    @Override
    public Context getActivityContext() {
        return mHostControl.getActivityContext();
    }

    @Override
    public boolean isActivityFinishing() {
        return mHostControl.isActivityFinishing();
    }

    @Override
    public Object getPresenter(Class className) {
        return mHostControl.getPresenter(className);
    }

    /**
     * getHostControl
     *
     * @return
     */
    public IHostControl getHostControl() {
        return mHostControl;
    }


    @Override
    public boolean isDev() {
        return ApplicationControl.INSTANCE.isDev();
    }

    @Override
    public void optOpenDebug() {
        ApplicationControl.INSTANCE.optOpenDebug();
    }

    @NonNull
    @Override
    public Context getAppContext() {
        return ApplicationControl.INSTANCE.getAppContext();
    }

    @NonNull
    @Override
    public SharePreferenceUtils getSharePreference() {
        return ApplicationControl.INSTANCE.getSharePreference();
    }

    @Override
    public Object getAppService(@NonNull String name) {
        return ApplicationControl.INSTANCE.getAppService(name);
    }
}
