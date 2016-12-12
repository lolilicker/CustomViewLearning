package com.okoer.customviewlearning.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.okoer.customviewlearning.DpAndPx;

/**
 * Created by WangBo on 2016/12/12.
 */

public class Radar extends View {
    private static final int RADAR_MARGIN = DpAndPx.Dp2Px(10);
    private int radius;
    private static final float data[] = {110, 120, 140, 130, 132};
    //每个角的弧度
    private static final float RADIAN = (float) ((Math.PI * 2) / data.length);
    private static final int MAX_DATA = 150;

    private Paint borderPaint;
    private Paint areaPaint;

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
        radius = Math.min(w, h) / 2;
        postInvalidate();
    }

    private void init() {
        borderPaint = new Paint();
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2);

        areaPaint = new Paint();
        areaPaint.setColor(Color.WHITE);
        areaPaint.setAlpha(100);
        areaPaint.setStyle(Paint.Style.FILL);
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
        canvas.translate(radius, radius);
        drawEdge(canvas);
        drawLine(canvas);
        drawArea(canvas);
    }

    private void drawArea(Canvas canvas) {
        Path path = new Path();
        Point point = null;
        for (int i = 0; i < data.length; i++) {
            point = getPoint(i, 0, data[i] / MAX_DATA);
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
                y = -radius + radarMargin;
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
