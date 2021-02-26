package com.cash.hunterchartdemo.test;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;

/**
 * Created by tao.liang on 2016/9/28.
 *
 * @param <T>
 */
public abstract class HbFundLineChartBase<T extends BarLineScatterCandleBubbleData<? extends
        IBarLineScatterCandleBubbleDataSet<? extends Entry>>> extends BarLineChartBase<T> {

    /**
     * if true, can response single or double finger touch event
     */
    private boolean mCustomFingerTouchEnable = false;

    /**
     * if true, land status can execute double touch function
     */
    private boolean mCanDoubleFingerTouchable = false;

    /**
     * 绘制灰白背景 true:表示绘制, false:无灰白背景
     */
    private boolean mCustomGridBgStyle;

    public boolean ismCustomGestureSingleTap() {
        return mCustomGesutreSingleTap;
    }

    public void setmCustomGestureSingleTap(boolean mCustomSingleTap) {
        this.mCustomGesutreSingleTap = mCustomSingleTap;
    }

    private boolean mCustomGesutreSingleTap = false;


    /**
     * 绘制灰白背景条数默认7个
     */
    private int mCeilCount = 7;


    public HbFundLineChartBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // initAttr(attrs);
    }

    public HbFundLineChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        //initAttr(attrs);
    }

    public HbFundLineChartBase(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();
        mChartTouchListener = new HbLineChartTouchListener(this, mViewPortHandler.getMatrixTouch(), 3f);
        notifyDrawChart();
    }

    private void notifyDrawChart() {
        mXAxisRenderer = new HbFundXAxisRenderer(mViewPortHandler, mXAxis, mLeftAxisTransformer,
                mCustomGridBgStyle, mCeilCount);
    }


    private IHbFundLineChartListener touchListener = null;

    public void setFingerTouchListener(IHbFundLineChartListener listener) {
        this.touchListener = listener;
    }

    public IHbFundLineChartListener getFingerTouchListener() {
        return this.touchListener;
    }

    public boolean ismCustomFingerTouchEnable() {
        return mCustomFingerTouchEnable;
    }


    public boolean ismCanDoubleFingerTouchable() {
        return mCanDoubleFingerTouchable;
    }

    /**
     * 设置背景样式
     *
     * @param mCustomGridBgStyle true:表示绘制灰白相间背景,false:可以通过设置基本属性绘制
     */
    public void setCustomGridBgStyle(boolean mCustomGridBgStyle) {
        this.mCustomGridBgStyle = mCustomGridBgStyle;
        notifyDrawChart();
    }

}