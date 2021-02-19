package com.cash.hunterchartdemo;

public class ChartBean {

    /**
     * ask : 2447.96
     * bid : 2447.36
     * epoch : 1613700086
     */

    private double ask;
    private double bid;
    private long epoch;

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public long getEpoch() {
        return epoch;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }

    @Override
    public String toString() {
        return "ChartBean{" +
                "ask=" + ask +
                ", bid=" + bid +
                ", epoch=" + epoch +
                '}';
    }
}
