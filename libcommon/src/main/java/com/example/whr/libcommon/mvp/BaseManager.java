package com.example.whr.libcommon.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by kinger on 2016/11/9.
 *
 * 统一管理类
 * @param <T>
 */
public abstract class BaseManager<T extends BaseClass> implements IHostStatus {
    /**
     * T数组
     */
    protected ArrayList<T> mArrays = new ArrayList<>();

    /**
     * 增加子项
     *
     * @param item
     */
    public void addItems(T item) {
        if (!mArrays.contains(item)) {
            mArrays.add(item);
        }
    }

    /**
     * 清空子项
     */
    public void clear() {
        mArrays.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, View root) {
        for (T item : mArrays) {
            item.onCreate(savedInstanceState, root);
        }
    }

    @Override
    public void onStart() {
        for (T item : mArrays) {
            item.onStart();
        }
    }

    @Override
    public void onStop() {
        for (T item : mArrays) {
            item.onStop();
        }
    }

    @Override
    public void onResume() {
        for (T item : mArrays) {
            item.onResume();
        }
    }

    @Override
    public void onPause() {
        for (T item : mArrays) {
            item.onPause();
        }
    }

    @Override
    public void onDestroy() {
        for (T item : mArrays) {
            item.onDestroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (T item : mArrays) {
            item.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        for (T item : mArrays) {
            item.onSaveInstanceState(outState);
        }
    }

    public abstract Object getPresenter(Class className);
}
