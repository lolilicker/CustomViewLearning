package com.okoer.customviewlearning.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.okoer.customviewlearning.DpAndPx;
import com.okoer.customviewlearning.R;

/**
 * Created by WangBo on 2017/3/6.
 */

public class MiClockView extends View {
    private final int DEFAULT_SIZE = DpAndPx.Dp2Px(200);
    private final int DEFAULT_PADDING = DpAndPx.Dp2Px(20);

    private int themeColor;
    private Paint paint;

    private float radius;
    //渐变色
    private SweepGradient sweepGradient;
    //刻度线长度
    private float scaleLength;

    Matrix matrix = new Matrix();
    Camera camera = new Camera();

    public MiClockView(Context context) {
        this(context, null);
    }

    public MiClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MiClockView,
                defStyleAttr, 0);
        themeColor = typedArray.getColor(R.styleable.MiClockView_themeColor, 0);
        typedArray.recycle();

        paint = new Paint();
        paint.setColor(themeColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = (Math.min(w, h) - DEFAULT_PADDING) / 2;
        scaleLength = 0.12f * radius;
        sweepGradient = new SweepGradient(w / 2, h / 2, new int[]{R.color.darker_gray, themeColor},
                new float[]{0.75f, 1});
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        if (heightMode == MeasureSpec.EXACTLY
                || widthMode == MeasureSpec.EXACTLY) {
            width = Math.min(widthSize, heightSize);
            height = heightSize;
            setMeasuredDimension(width, height);
        } else {
            setMeasuredDimension(DEFAULT_SIZE, DEFAULT_SIZE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(getWidth() / 2, getHeight() / 2);
        paint.setStrokeWidth(DpAndPx.Dp2Px(2));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(0, 0, radius, paint);
        paint.setStrokeWidth(DpAndPx.Dp2Px(3));
        for (int i = 0; i <= 180; i++) {
            canvas.drawLine(0, radius * 0.95f, 0, radius * 0.85f, paint);
            canvas.rotate(2);
        }
    }


}
