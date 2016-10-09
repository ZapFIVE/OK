package com.example.libnet.cache.impl;

import com.example.libnet.cache.ICache;

import org.json.JSONObject;

/**
 * Created by whr on 2016/10/8.
 */
public class JsonCache implements ICache<String, JSONObject> {
    @Override
    public JSONObject get(String key) {
        return null;
    }

    @Override
    public void put(String key, JSONObject value) {

    }
}
