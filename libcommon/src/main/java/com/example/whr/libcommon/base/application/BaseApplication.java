package com.example.whr.libcommon.base.application;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.example.libutils.orm.sp.SharePreferenceUtils;
import com.example.libutils.utils.ProcessUtils;
import com.example.whr.libcommon.helper.AppServiceHelper;

/**
 * Created by kinger on 2016/10/31.
 * <p>
 * Application 基类 继承自MultiDexApplication
 */
public abstract class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        if (ProcessUtils.isMainProcess(this)) {
            init();
        }
    }

    /**
     * 初始化任务
     */
    public void init() {
        // 初始化控制类 基类
        ApplicationControl.INSTANCE.init(getApplicationControl());
    }

    /**
     * 获取Application控制类
     *
     * @return
     */
    protected IApplicationControl getApplicationControl() {
        return new ControlImpl();
    }

    /**
     * 实际获取服务
     *
     * @param name
     * @return
     */
    protected Object getRealAppService(@NonNull String name) {
        return AppServiceHelper.INSTANCE.get(this, name);
    }


    /**
     * 实际操作的控制类 (利用内部类 持有外部调用类 来实现)
     */
    protected class ControlImpl implements IApplicationControl {

        /**
         * SharePreference
         */
        private final SharePreferenceUtils mSharePreferenceUtils;

        public ControlImpl() {
            mSharePreferenceUtils = new SharePreferenceUtils(getApplicationContext());
        }

        @Override
        public boolean isDev() {
            return false;
        }

        @Override
        public void optOpenDebug() {
        }

        @Override
        @NonNull
        public Context getAppContext() {
            return getApplicationContext();
        }

        @Override
        @NonNull
        public SharePreferenceUtils getSharePreference() {
            return mSharePreferenceUtils;
        }

        @Override
        public Object getAppService(@NonNull String name) {
            return getRealAppService(name);
        }
    }
}
