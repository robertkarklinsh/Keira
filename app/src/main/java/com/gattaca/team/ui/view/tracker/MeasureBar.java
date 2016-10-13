package com.gattaca.team.ui.view.tracker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.gattaca.team.R;
import com.gattaca.team.annotation.GraphPeriod;
import com.gattaca.team.root.AppUtils;

import java.util.ArrayList;


public final class MeasureBar extends View {
    final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final ArrayList<Long> poinst = new ArrayList<>();
    int radiusBackground, colorGray, colorRed;
    double stepBy1sec;
    Color color = Color.Green;
    private long value = 0;
    private
    @GraphPeriod
    long period = GraphPeriod.period_5min;

    public MeasureBar(Context context) {
        super(context);
        init(context);
    }

    public MeasureBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeasureBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(final Context context) {
        final Resources res = context.getResources();
        radiusBackground = res.getDimensionPixelSize(R.dimen.pressureBar_background_radius);
        colorGray = res.getColor(R.color.pressure_colorGray);
        colorRed = res.getColor(R.color.measure_red);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(2 * radiusBackground);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            AppUtils.registerBus(this);
        } else {
//            AppUtils.unregisterBus(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int w = MeasureSpec.getSize(widthMeasureSpec);
        if (w > 0) {
            setMeasuredDimension(w, radiusBackground * 2);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (stepBy1sec > 0) {
            mPaint.setColor(colorGray);
            mPaint.setStrokeWidth(2 * radiusBackground);
            canvas.drawLine(radiusBackground, radiusBackground, getMeasuredWidth() - radiusBackground, radiusBackground, mPaint);
            mPaint.setColor(getResources().getColor(color == Color.Green ? R.color.pressure_colorGreenMain : R.color.pressure_colorOrangeMain));
            canvas.drawLine(radiusBackground, radiusBackground, (float) (radiusBackground + value * stepBy1sec), radiusBackground, mPaint);

            if (!poinst.isEmpty()) {
                mPaint.setColor(colorRed);
                mPaint.setStrokeWidth(6);
                for (long a : poinst) {
                    canvas.drawLine((float) (radiusBackground + a * stepBy1sec), 0, (float) (radiusBackground + a * stepBy1sec), 2 * radiusBackground, mPaint);
                }
            }
        }
    }

    public void addPoint() {
        poinst.add(value);
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long v) {
        this.value = v;
        boolean b = false;

        if (value >= period) {
            if (period == GraphPeriod.period_5min) {
                period = (GraphPeriod.period_15min);
                b = true;
            } else if (period == GraphPeriod.period_15min) {
                period = (GraphPeriod.period_30min);
                b = true;
            } else if (period == GraphPeriod.period_30min) {
                period = (GraphPeriod.period_1hour);
                b = true;
            }
        }
        if (stepBy1sec <= 0 || b) {
            stepBy1sec = (((double) getMeasuredWidth() - 2 * radiusBackground) / period);
        }
        invalidate();
    }

}
