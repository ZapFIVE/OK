package com.example.libutils.orm.sp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kinger on 2016/10/31.
 * <p>
 * SharePreference工具类
 */
public class SharePreferenceUtils {
    private SharedPreferences mSp;

    /**
     * SharePreference的名字
     */
    private String mSpName;
    /**
     * SharePreference的mode
     */
    private int mSpMode;

    /**
     * Context
     */
    private Context mContext;

    public SharePreferenceUtils(Context context) {
        this(context, getDefaultSharedPreferencesName(context));
    }


    public SharePreferenceUtils(Context context, String fileName) {
        mContext = context;
        mSpName = fileName;
        mSpMode = Context.MODE_PRIVATE;

        mSp = mContext.getSharedPreferences(mSpName, mSpMode);
    }

    /**
     * 获取默认的SharePreference的名字 (保持与PreferenceManager中的一致)
     *
     * @param context
     * @return
     */
    public static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    /**
     * 获取SharePreference的名字
     *
     * @return
     */
    public String getSharePreferenceName() {
        return mSpName;
    }

    /**
     * 获取SharePreference的mode
     *
     * @return
     */
    public int getSharePreferenceMode() {
        return mSpMode;
    }

    /**
     * 检查初始化状态
     */
    private void checkInitStatus() {
        if (mSp == null) {
            throw new NullPointerException("SharePreference is null, please check your application");
        }
    }

    /**
     * 从sharepreferecen中取值 (仅支持int ,long , boolean ,float, String)
     *
     * @param resId    resId
     * @param defValue 默认值
     * @return 实际值
     */
    public Object get(int resId, Object defValue) {
        return get(mContext.getString(resId), defValue);
    }

    /**
     * 从sharepreferecen中取值 (仅支持int ,long , boolean ,float, String)
     *
     * @param resId      resId
     * @param defValue   默认值
     * @param formatArgs 允许支持自定义参数
     * @return 实际值
     */
    public Object get(int resId, Object defValue, Object... formatArgs) {
        return get(mContext.getString(resId, formatArgs), defValue);
    }

    /**
     * 从sharepreferecen中取值 (仅支持int ,long , boolean ,float, String)
     *
     * @param key      获取key值
     * @param defValue 默认值
     * @return 实际值
     */
    private Object get(String key, Object defValue) {
        if (defValue==null){
            throw new NullPointerException("defValue can not be null. ");
        }
        checkInitStatus();

        if (defValue instanceof Integer) {
            return mSp.getInt(key, (int) defValue);
        } else if (defValue instanceof Long) {
            return mSp.getLong(key, (long) defValue);
        } else if (defValue instanceof Boolean) {
            return mSp.getBoolean(key, (boolean) defValue);
        } else if (defValue instanceof Float) {
            return mSp.getFloat(key, (float) defValue);
        } else if (defValue instanceof String) {
            return mSp.getString(key, (String) defValue);
        }

        return null;
    }

    /**
     * 从SharePreference中删除键值对 （默认是异步 apply）
     *
     * @param resId 需要删除的resid
     */
    public void remove(int resId) {
        remove(resId, false);
    }

    /**
     * 从SharePreference中删除键值对 （默认是异步 apply）
     *
     * @param resId      需要删除的resid
     * @param formatArgs 允许支持自定义参数
     */
    public void remove(int resId, Object... formatArgs) {
        remove(resId, false, formatArgs);
    }

    /**
     * 从SharePreference中删除键值对
     *
     * @param resId  需要删除的resid
     * @param isSync 是否同步运行
     */
    public void remove(int resId, boolean isSync) {
        remove(mContext.getString(resId), isSync);
    }

    /**
     * 从SharePreference中删除键值对
     *
     * @param resId      需要删除的resid
     * @param isSync     是否同步运行
     * @param formatArgs 允许支持自定义参数
     */
    public void remove(int resId, boolean isSync, Object... formatArgs) {
        remove(mContext.getString(resId, formatArgs), isSync);
    }

    /**
     * 从SharePreference中删除键值对
     *
     * @param key    需要删除的key值
     * @param isSync 是否同步运行
     */
    private void remove(String key, boolean isSync) {
        checkInitStatus();

        SharedPreferences.Editor edit = mSp.edit();
        edit.remove(key);

        if (isSync) {
            edit.commit();
        } else {
            edit.apply();
        }
    }

    /**
     * 将key-value放入SharePreference (默认是异步的 apply) (仅支持int ,long , boolean ,float, String)
     *
     * @param resId 键
     * @param value 值
     */
    public void put(int resId, Object value) {
        put(resId, value, false);
    }

    /**
     * 将key-value放入SharePreference (默认是异步的 apply) (仅支持int ,long , boolean ,float, String)
     *
     * @param resId      键
     * @param value      值
     * @param formatArgs 允许支持自定义参数
     */
    public void put(int resId, Object value, Object... formatArgs) {
        put(resId, value, false, formatArgs);
    }

    /**
     * 将key-value放入SharePreference (仅支持int ,long , boolean ,float, String)
     *
     * @param resId  键
     * @param value  值
     * @param isSync 是否同步运行
     */
    public void put(int resId, Object value, boolean isSync) {
        put(mContext.getString(resId), value, isSync);
    }

    /**
     * 将key-value放入SharePreference (仅支持int ,long , boolean ,float, String)
     *
     * @param resId      键
     * @param value      值
     * @param isSync     是否同步运行
     * @param formatArgs 允许支持自定义参数
     */
    public void put(int resId, Object value, boolean isSync, Object... formatArgs) {
        put(mContext.getString(resId, formatArgs), value, isSync);
    }

    /**
     * 将key-value放入SharePreference (仅支持int ,long , boolean ,float, String)
     *
     * @param key    键
     * @param value  值
     * @param isSync 是否同步运行
     */
    private void put(String key, Object value, boolean isSync) {
        checkInitStatus();

        SharedPreferences.Editor edit = mSp.edit();
        if (value instanceof Integer) {
            edit.putInt(key, (int) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (long) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (float) value);
        } else if (value instanceof String) {
            edit.putString(key, (String) value);
        }

        if (isSync) {
            edit.commit();
        } else {
            edit.apply();
        }
    }
}
