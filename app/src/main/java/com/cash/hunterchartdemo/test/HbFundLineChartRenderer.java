package com.cash.hunterchartdemo.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cash.hunterchartdemo.R;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tao.liang on 2016/9/28.
 * 画阴影和高亮线, 双手指效果
 */

public class HbFundLineChartRenderer extends LineChartRenderer {
    private final HbFundLineChartBase mHbBarLineChartBase;
    private Paint mShadePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    ArrayList<Float> mTouchPointList = new ArrayList<>();
    RectF rectDoubleShade = new RectF();


    private float hLength = Utils.convertDpToPixel(15f);//横线长15dp
    private float vLength = Utils.convertDpToPixel(10f);//竖线长10dp
    private float rect= Utils.convertDpToPixel(8f);//矩形高低差/2
    private float textX= Utils.convertDpToPixel(2f);//文本x坐标偏移量
    private float textY= Utils.convertDpToPixel(3f);//文本y偏移量
    private float textSixe = 10f;//文字大小

    private Context mContext;
    private boolean isShowLabel = true;//是否显示label,默认显示
    private int mWidth;//屏幕宽度,在构造方法中传进来赋值
    private float hViewLength = Utils.convertDpToPixel(30f);//vie宽30dp
    private float vViewLength = Utils.convertDpToPixel(20f);//view高20dp
    private float viewRect= Utils.convertDpToPixel(4f);//矩形高低差

    /**
     * path that is used for drawing highlight-lines (drawLines(...) cannot be used because of dashes)
     */
    private Path mHighlightLinePath = new Path();

    public HbFundLineChartRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler, int width, Context context) {
        super(chart, animator, viewPortHandler);

        mContext = context;
        mWidth = width;
        mShadePaint.setColor(Color.parseColor("#2F4587F0"));
        mShadePaint.setStyle(Paint.Style.FILL);
        mShadePaint.setStrokeCap(Paint.Cap.ROUND);
        mShadePaint.setStrokeJoin(Paint.Join.ROUND);
        mDotPaint.setStyle(Paint.Style.FILL);
        mDotPaint.setStrokeCap(Paint.Cap.ROUND);
        mDotPaint.setStrokeJoin(Paint.Join.ROUND);
        mDotPaint.setColor(mHighlightPaint.getColor());
        mHbBarLineChartBase = (HbFundLineChartBase) mChart;
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {
        LineData lineData = mChart.getLineData();
        mTouchPointList.clear();

        boolean canDrawShade = drawAreaNew(c, indices, lineData, mTouchPointList);
        if (canDrawShade) {
            drawShadeArea(c, indices, mTouchPointList);
        }
    }

    private void drawShadeArea(Canvas canvas, Highlight[] indices, List<Float> pointX) {
        if (indices != null) {
            if (indices.length == 1 && pointX.size() == 1) {
                rectDoubleShade.left = pointX.get(0);
                rectDoubleShade.right = rectDoubleShade.left;
            } else {
                rectDoubleShade.left = pointX.get(0);
                if (pointX.size() == 2) {
                    rectDoubleShade.right = pointX.get(1);
                } else {
                    rectDoubleShade.right = rectDoubleShade.left;
                }
            }
            rectDoubleShade.top = mViewPortHandler.contentTop();
            rectDoubleShade.bottom = mViewPortHandler.contentBottom();
            canvas.drawRect(rectDoubleShade, mShadePaint);
        }
    }

    @Override
    protected void drawHighlightLines(Canvas c, float x, float y, ILineScatterCandleRadarDataSet set) {
        mHighlightPaint.setColor(set.getHighLightColor());
        mHighlightPaint.setStrokeWidth(3.5f);

        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 0);
        mHighlightPaint.setPathEffect(effects);

        // draw vertical highlight lines
        if (set.isVerticalHighlightIndicatorEnabled()) {

            // create vertical path
            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(x, mViewPortHandler.contentTop());
            mHighlightLinePath.lineTo(x, mViewPortHandler.contentBottom());

            c.drawPath(mHighlightLinePath, mHighlightPaint);
        }

        // draw horizontal highlight lines
        if (set.isHorizontalHighlightIndicatorEnabled()) {

            // create horizontal path
            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(mViewPortHandler.contentLeft(), y);
            mHighlightLinePath.lineTo(mViewPortHandler.contentRight(), y);

            c.drawPath(mHighlightLinePath, mHighlightPaint);
        }
    }

    /**
     * 这里API在3.0.3-beta版本上的代码,使用时,需要把注释的代码打开
     *
     * @param c
     * @param indices
     * @param lineData
     * @param pointX
     */
    private boolean drawAreaNew(Canvas c, Highlight[] indices, LineData lineData, ArrayList<Float> pointX) {
        if (indices == null) {
            return false;
        }
        boolean hasHight = false;
        for (Highlight high : indices) {


            ILineDataSet set = lineData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            Entry e = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(e, set))
                continue;

            MPPointD pix = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getX(),
                    e.getY() * mAnimator.getPhaseY());

            high.setDraw((float) pix.x, (float) pix.y);

            // draw the lines (两端的线)
            drawHighlightLines(c, (float) pix.x, (float) pix.y, set);

            if (indices.length == 1 && (mHbBarLineChartBase != null && mHbBarLineChartBase.ismCustomGestureSingleTap())) {
                //如果图表中只有一个手指触摸,就显示小圆点,画笔使用和高亮线一样的颜色
                c.drawCircle((float) pix.x, (float) pix.y, set.getCircleRadius(), mDotPaint);
            }

            //把两端的点添加到集合中
            pointX.add((float) pix.x);
            hasHight = true;
        }
        return hasHight;
    }

    private boolean clipPathSupported() {
        return Utils.getSDKInt() >= 18;
    }

    @Override
    public void drawData(Canvas c) {
        int width = (int) mViewPortHandler.getChartWidth();
        int height = (int) mViewPortHandler.getChartHeight();

        if (mDrawBitmap == null
                || (mDrawBitmap.get().getWidth() != width)
                || (mDrawBitmap.get().getHeight() != height)) {

            if (width > 0 && height > 0) {

                mDrawBitmap = new WeakReference<Bitmap>(Bitmap.createBitmap(width, height, mBitmapConfig));
                mBitmapCanvas = new Canvas(mDrawBitmap.get());
            } else
                return;
        }

        if (mDrawBitmap != null && mDrawBitmap.get() != null) {
            mDrawBitmap.get().eraseColor(Color.TRANSPARENT);
        }

        LineData lineData = mChart.getLineData();

        for (ILineDataSet set : lineData.getDataSets()) {

            if (set.isVisible())
                drawDataSet(c, set);
        }
        if (mDrawBitmap != null && mDrawBitmap.get() != null) {
            c.drawBitmap(mDrawBitmap.get(), 0, 0, mRenderPaint);
        }

    }


    /******************************* 点标记，类似多个markview默认显示 ************************************/
    @Override
    public void drawValues(Canvas c) {
        super.drawValues(c);
        if (isShowLabel) {
            LineDataSet dataSetByIndex = (LineDataSet) mChart.getLineData().getDataSetByIndex(0);
            Transformer trans = mChart.getTransformer(dataSetByIndex.getAxisDependency());
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿画笔
            paint.setTextSize(Utils.convertDpToPixel(textSixe));//设置字体大小

            //画首中尾三个label
            float[] firstFloat = getFloat(dataSetByIndex.getValues(), 0);//根据数据集获取点
            drawPointLabel(trans, paint, c, firstFloat);
            float[] middleFloat = getFloat(dataSetByIndex.getValues(), (dataSetByIndex.getValues().size() - 1) / 2);
            drawPointLabel(trans, paint, c, middleFloat);
            float[] endFloat = getFloat(dataSetByIndex.getValues(), dataSetByIndex.getValues().size() - 1);
            drawPointLabel(trans, paint, c, endFloat);
        }
    }

    private void drawPointLabel(Transformer trans, Paint paint, Canvas c, float[] floatPosition) {
        MPPointD maxPoint = trans.getPixelForValues(floatPosition[0], floatPosition[1]);
        float highX = (float) maxPoint.x;
        float highY = (float) maxPoint.y;
        TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.mark_view, null, false);
        if (highX > mWidth - mWidth / 4) {//标识朝左
            view.setBackgroundResource(R.mipmap.sm_lable_bg_buy_r);
            Bitmap bitmap = createBitmap(view, (int) hViewLength, (int) vViewLength);
            c.drawBitmap(bitmap, (int) (highX - hViewLength), (int) (highY - vViewLength - viewRect), paint);
        } else if (highX < mWidth / 4) {//标识朝右
            view.setBackgroundResource(R.mipmap.sm_lable_bg_buy_l);
            Bitmap bitmap = createBitmap(view, (int) hViewLength, (int) vViewLength);
            c.drawBitmap(bitmap, (int) (highX), (int) (highY - vViewLength - viewRect), paint);
        } else {//标识居中
            view.setBackgroundResource(R.mipmap.sm_lable_bg_buy_c);
            Bitmap bitmap = createBitmap(view, (int) hViewLength, (int) vViewLength);
            c.drawBitmap(bitmap, (int) (highX - hViewLength / 2), (int) (highY - vViewLength - viewRect), paint);
        }
    }

    private float[] getFloat(List<Entry> lists, int index) {
        float[] maxEntry = new float[2];
        maxEntry[0] = lists.get(index).getX();
        maxEntry[1] = lists.get(index).getY();
        return maxEntry;
    }

    private Bitmap createBitmap(View v, int width, int height) {
        //测量使得view指定大小
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth, measuredHeight);
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        v.draw(c);
        return bmp;
    }

    private void drawLowPoint(LineDataSet dataSetByIndex, Transformer trans, Paint paint, Canvas c) {
        float[] minFloat = getMinFloat(dataSetByIndex.getValues());//根据数据集获取最低点
        //通过trans得到最低点的屏幕位置
        MPPointD minPoint = trans.getPixelForValues(minFloat[0], minFloat[1]);
        float lowX = (float) minPoint.x;
        float lowY = (float) minPoint.y;
        paint.setColor(Color.parseColor("#1ab546"));
        float rectLength = Utils.convertDpToPixel((minFloat[1] + "").length() * Utils.convertDpToPixel(1.7f));//矩形框长
        //画横竖线
        c.drawLine(lowX, lowY, lowX, lowY + vLength, paint);
        if (lowX > mWidth - mWidth / 3) {//标识朝左
            c.drawLine(lowX, lowY + vLength, lowX - hLength, lowY + vLength, paint);
            //画矩形
            c.drawRect(new Rect((int) (lowX - hLength - rectLength), (int) (lowY + vLength - rect), (int) (lowX - hLength), (int) (lowY + vLength + rect)), paint);
            //写数字
            paint.setColor(Color.WHITE);
            c.drawText(minFloat[1] + "", lowX - rectLength - hLength + textX, lowY + vLength + textY, paint);
        } else {//标识朝右
            c.drawLine(lowX, lowY + vLength, lowX + hLength, lowY + vLength, paint);
            c.drawRect(new Rect((int) (lowX + hLength), (int) (lowY + vLength - rect), (int) (lowX + hLength + rectLength), (int) (lowY + vLength + rect)), paint);
            paint.setColor(Color.WHITE);
            c.drawText(minFloat[1] + "", lowX + hLength + textX, lowY + vLength + textY, paint);
        }
    }

    private float[] getMinFloat(List<Entry> lists) {
        float[] mixEntry = new float[2];
        for (int i = 0; i < lists.size() - 1; i++) {
            if (i == 0) {
                mixEntry[0] = lists.get(i).getX();
                mixEntry[1] = lists.get(i).getY();
            }
            if (mixEntry[1] > lists.get(i + 1).getY()) {
                mixEntry[0] = lists.get(i + 1).getX();
                mixEntry[1] = lists.get(i + 1).getY();
            }
        }
        return mixEntry;
    }
}
