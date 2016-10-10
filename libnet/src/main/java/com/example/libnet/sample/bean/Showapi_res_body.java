package com.example.libnet.sample.bean;

import java.util.List;

/**
 * Created by whr on 2016/10/10.
 */
public class Showapi_res_body {
    private int ret_code;

    private List<String> list ;

    public void setRet_code(int ret_code){
        this.ret_code = ret_code;
    }
    public int getRet_code(){
        return this.ret_code;
    }
    public void setString(List<String> list){
        this.list = list;
    }
    public List<String> getString(){
        return this.list;
    }

    @Override
    public String toString() {
        return "ret_code:" + ret_code + (list == null ? ",list:null" : list.toString());
    }
}