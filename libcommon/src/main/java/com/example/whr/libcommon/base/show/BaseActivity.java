package com.example.whr.libcommon.base.show;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.example.whr.libcommon.mvp.IHostControl;
import com.example.whr.libcommon.mvp.impl.DelegateManager;

/**
 * Created by kinger on 2016/11/3.
 *
 * Activity 基类
 */
public abstract class BaseActivity extends FragmentActivity implements IHostControl{
    /**
     * DelegateManager
     */
    private DelegateManager mDelegate = new DelegateManager();

    /**
     * Handler
     */
    protected Handler mHandler = new Handler();

    @Override
    public DelegateManager getDelegateManager() {
        return mDelegate;
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public boolean isActivityFinishing() {
        return this.isFinishing();
    }

    @Override
    public Object getPresenter(Class className) {
        return mDelegate.getPresenter(className);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化Delegate
        initDelegate();

        // 延迟执行onCreate初始化 防止与setContentView 冲突
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDelegate.onCreate(savedInstanceState, getWindow().getDecorView());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDelegate.onStart();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDelegate.onStop();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDelegate.onResume();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDelegate.onPause();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDelegate.onDestroy();
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDelegate.onActivityResult(requestCode, resultCode, data);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDelegate.onSaveInstanceState(outState);
            }
        });
    }

    /**
     * 初始化delegate
     */
    public abstract void initDelegate();
}
