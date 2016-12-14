package com.okoer.customviewlearning.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.okoer.customviewlearning.DpAndPx;

/**
 * Created by WangBo on 2016/12/12.
 */

public class Radar extends View {
    private static final String TAG = "Radar";

    private static final int RADAR_MARGIN = DpAndPx.Dp2Px(20);
    //动画时长
    private static final int ANIM_DURATION = 1000;
    private int radius;
    private static final float data[] = {6.3f, 7.2f, 8.8f, 4.1f, 9.0f};
    private static final String title[] = {"功效", "价格", "品牌", "外观", "耐用"};
    //每个角的弧度
    private static final float RADIAN = (float) ((Math.PI * 2) / data.length);
    private static final int MAX_DATA = 10;
    //标题大小
    private static final float TITLE_TEXT_SIZE = 16;

    private Paint borderPaint;
    private Paint areaPaint;
    private Paint titlePaint;

    private float percent = 0;

    public Radar(Context context) {
        this(context, null);
    }

    public Radar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Radar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(w, h) / 3;
    }

    private void init() {
        borderPaint = new Paint();
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(DpAndPx.Dp2Px(2));

        areaPaint = new Paint();
        areaPaint.setColor(Color.WHITE);
        areaPaint.setAlpha(100);
        areaPaint.setStyle(Paint.Style.FILL);

        titlePaint = new Paint();
        titlePaint.setColor(Color.WHITE);
        titlePaint.setStyle(Paint.Style.STROKE);
        titlePaint.setTextSize(DpAndPx.sp2px(getContext(), TITLE_TEXT_SIZE));
        titlePaint.setTextAlign(Paint.Align.CENTER);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                percent = (float) valueAnimator.getAnimatedValue();
                Log.d(TAG, "anim " + percent);
                invalidate();
            }
        });
        valueAnimator.setDuration(ANIM_DURATION);
        valueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMySize(100, widthMeasureSpec);
        int height = getMySize(100, heightMeasureSpec);

//        setMeasuredDimension(Math.min(width, height),Math.min(width, height));
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                mySize = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                mySize = size;
                break;
        }
        return mySize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLUE);
        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        drawEdge(canvas);
        drawLine(canvas);
        drawArea(canvas);
        drawTitle(canvas);
    }

    private void drawTitle(Canvas canvas) {
        Point point;
        for (int i = 0; i < data.length; i++) {
            point = getPoint(i, RADAR_MARGIN, 1);
            Paint.FontMetrics fontMetrics = titlePaint.getFontMetrics();
            float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
            float offY = fontTotalHeight / 2 - fontMetrics.bottom;
            float newY = point.y + offY;
            canvas.drawText(title[i], point.x, newY, titlePaint);
//            canvas.drawLine(0,0,point.x, newY,titlePaint);
        }
    }

    private void drawArea(final Canvas canvas) {
        final Path path = new Path();

        Point point = null;
        for (int i = 0; i < data.length; i++) {
            point = getPoint(i, 0, data[i] / MAX_DATA * percent);
            if (i == 0) {
                path.moveTo(point.x, point.y);
            } else {
                path.lineTo(point.x, point.y);
            }
        }
        path.close();
        canvas.drawPath(path, areaPaint);



    }

    private void drawLine(Canvas canvas) {
        for (int i = 0; i < data.length; i++) {
            canvas.drawLine(0, 0, getPoint(i).x, getPoint(i).y, borderPaint);
        }
    }

    private void drawEdge(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < data.length; i++) {
            if (i == 0) {
                path.moveTo(getPoint(i).x, getPoint(i).y);
            } else {
                path.lineTo(getPoint(i).x, getPoint(i).y);
            }
        }
        path.close();
        canvas.drawPath(path, borderPaint);
    }

    private Point getPoint(int position) {
        return getPoint(position, 0, 1);
    }

    private Point getPoint(int position, int radarMargin, float percent) {
        int x = 0;
        int y = 0;
        switch (position) {
            case 0:
                x = 0;
                y = -radius - radarMargin;
                break;
            case 1:
                x = (int) (Math.sin(RADIAN) * (radius + radarMargin));
                y = (int) (-Math.cos(RADIAN) * (radius + radarMargin));
                break;
            case 2:
                x = (int) (Math.sin(Math.PI - 2 * RADIAN) * (radius + radarMargin));
                y = (int) (Math.cos(Math.PI - 2 * RADIAN) * (radius + radarMargin));
                break;
            case 3:
                x = (int) (-Math.sin(Math.PI - 2 * RADIAN) * (radius + radarMargin));
                y = (int) (Math.cos(Math.PI - 2 * RADIAN) * (radius + radarMargin));
                break;
            case 4:
                x = (int) (-Math.sin(RADIAN) * (radius + radarMargin));
                y = (int) (-Math.cos(RADIAN) * (radius + radarMargin));
                break;
            default:
                break;
        }
        Point point = new Point((int) (x * percent), (int) (y * percent));
        return point;
    }
}
