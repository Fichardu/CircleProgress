package me.fichardu.circleprogress;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;

public class CircleProgress extends View {

    private static final int RED = 0xFFE5282C;
    private static final int YELLOW = 0xFF1F909A;
    private static final int BLUE = 0xFFFC9E12;
    private static final int[] COLORS = new int[]{RED, BLUE, YELLOW};
    private TimeInterpolator mInterpolator = new EaseInOutCubicInterpolator();

    private final double DEGREE = Math.PI / 180;
    private Paint mPaint;
    private int mRadius;
    private int mPointRadius;
    private long mStartTime;
    private boolean mStartAnim = false;
    private Point mCenter = new Point();

    private ArcPoint[] mArcPoint;
    private static final int POINT_NUM = 15;
    private long mDuration = 3600;

    public CircleProgress(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mArcPoint = new ArcPoint[POINT_NUM];

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = getResources().getDimensionPixelSize(R.dimen.default_circle_view_size);
        int width = getDefaultSize(defaultSize, widthMeasureSpec);
        int height = getDefaultSize(defaultSize, heightMeasureSpec);
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);

        mRadius = size / 3;
        mPointRadius = mRadius / 12;
        mCenter.set(size / 2, size / 2);
        calPoints();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mCenter.x, mCenter.y);
        if (mStartAnim) {
            float factor = getFactor();
            canvas.rotate(36 * factor);
            float x, y;
            for (int i = 0; i < POINT_NUM; ++i) {
                mPaint.setColor(mArcPoint[i].color);
                float itemFactor = getItemFactor(i, factor);
                x = mArcPoint[i].x - 2 * mArcPoint[i].x * itemFactor;
                y = mArcPoint[i].y - 2 * mArcPoint[i].y * itemFactor;
                canvas.drawCircle(x, y, mPointRadius, mPaint);
            }
            postInvalidate();
        } else {
            for (ArcPoint item : mArcPoint) {
                mPaint.setColor(item.color);
                canvas.drawCircle(item.x, item.y, mPointRadius, mPaint);
            }
        }
        canvas.restore();

    }

    private void calPoints() {
        for (int i = 0; i < POINT_NUM; ++i) {
            float x = mRadius * -(float) Math.sin(DEGREE * 24 * i);
            float y = mRadius * -(float) Math.cos(DEGREE * 24 * i);

            ArcPoint point = new ArcPoint(x, y, COLORS[i % 3]);
            mArcPoint[i] = point;
        }
    }





    private float getFactor() {
        if (mStartAnim) {
            long offset = AnimationUtils.currentAnimationTimeMillis() - mStartTime;
            float factor = offset / (float) mDuration;
            return factor % 1f;
        } else {
            return 0f;
        }
    }

    private float getItemFactor(int index, float factor) {
        float itemFactor = (factor - 0.66f / POINT_NUM * index) * 3;
        if (itemFactor < 0f) {
            itemFactor = 0f;
        } else if (itemFactor > 1f) {
            itemFactor = 1f;
        }
        return mInterpolator.getInterpolation(itemFactor);
    }

    public void startAnim() {
        mStartAnim = true;
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        postInvalidate();
    }

    public void stopAnim() {
        mStartAnim = false;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    static class ArcPoint {
        float x;
        float y;
        int color;

        ArcPoint(float x, float y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

}
