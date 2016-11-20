package com.example.whr.libcommon.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by kinger on 2016/11/9.
 *
 * Host 状态变化
 */
public interface IHostStatus {
    /**
     * create
     *
     * @param savedInstanceState
     * @param root 根视图
     */
    void onCreate(Bundle savedInstanceState, View root);


    /**
     * start
     */
    void onStart();

    /**
     * stop
     */
    void onStop();

    /**
     * resume
     */
    void onResume();

    /**
     * pause
     */
    void onPause();


    /**
     * destroy
     */
    void onDestroy();

    /**
     * activity result
     *
     * @param requestCode 请在{@link IRequestConstant}中定义requestCode
     * @param resultCode  请在{@link IResultConstant}中定义resultCode
     * @param data data
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * saveInstanceState
     *
     * @param outState
     */
    void onSaveInstanceState(Bundle outState);
}
