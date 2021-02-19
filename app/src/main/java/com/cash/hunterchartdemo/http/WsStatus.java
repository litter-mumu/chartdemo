package com.cash.hunterchartdemo.http;

public class WsStatus {

    public final static int CONNECTED = 1;//连接
    public final static int CONNECTING = 0;//断开
    public final static int RECONNECT = 2;//重新连接
    public final static int DISCONNECTED = -1;//不连接

    class CODE {
        public final static int NORMAL_CLOSE = 1000;//正常关闭
        public final static int ABNORMAL_CLOSE = 1001;//异常关闭
    }

    class TIP {
        public final static String NORMAL_CLOSE = "normal close";//正常关闭
        public final static String ABNORMAL_CLOSE = "abnormal close";//异常关闭
    }

}
