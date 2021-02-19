package com.cash.hunterchartdemo;

import java.io.Serializable;
import java.util.List;

public class ChartResponseInfo implements Serializable {

    public List<ChartBean> data;
    public int code;
    public String msg;

    @Override
    public String toString() {
        return "ChartResponseInfo{" +
                "data=" + data +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ChartBean> getData() {
        return data;
    }

    public void setData(List<ChartBean> data) {
        this.data = data;
    }
}