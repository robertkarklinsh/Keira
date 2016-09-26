package com.gattaca.team.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.gattaca.team.db.sensor.optimizing.SensorPoint_5_min;

import java.util.List;

public class Bpm extends View {
    private final DashPathEffect dashPathEffect = new DashPathEffect(new float[]{5, 5}, 0);
    private Paint mainLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mainRedZonePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mPath;

    public Bpm(Context context) {
        super(context);
        init();
    }

    public Bpm(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Bpm(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void install(List<SensorPoint_5_min> raw) {
        Log.e("ttt", "" + raw.size());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        if (width > 0) {
            setMeasuredDimension(width, width);
        }
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int width = getMeasuredWidth();
        final int linesOffset = (int) (width * 0.05);
        final int radius = (int) (0.4 * width);
        final int minBPM = 20;
        final int maxBPM = 140;
        final int pxsPer20Bpm = minBPM * radius / (maxBPM - minBPM);
        final int pointBPM = pxsPer20Bpm / minBPM;
        // draw main elements ======================================================================
        mainLinePaint.setPathEffect(null);
        mainLinePaint.setColor(Color.GRAY);
        mainLinePaint.setStrokeWidth(1);
        canvas.drawLine(
                linesOffset,
                width / 2,
                width - linesOffset,
                width / 2,
                mainLinePaint);
        canvas.drawLine(
                width / 2,
                linesOffset,
                width / 2,
                width - linesOffset,
                mainLinePaint);
        canvas.drawLine(
                width / 2,
                linesOffset,
                width / 2,
                width - linesOffset,
                mainLinePaint);
        mainLinePaint.setPathEffect(dashPathEffect);
        for (int i = 2; i < (maxBPM / minBPM) - 2; i++) {
            canvas.drawCircle(width / 2, width / 2, i * pxsPer20Bpm, mainLinePaint);
        }
        mainLinePaint.setPathEffect(null);

        mainRedZonePaint.setStrokeWidth(pxsPer20Bpm);
        mainRedZonePaint.setAlpha(50);
        canvas.drawCircle(width / 2, width / 2, (float) (pxsPer20Bpm * ((maxBPM / minBPM) - 1.5)), mainRedZonePaint);
        canvas.drawCircle(width / 2, width / 2, pxsPer20Bpm / 2, mainRedZonePaint);

        mainRedZonePaint.setStrokeWidth(3);
        mainRedZonePaint.setAlpha(100);
        canvas.drawCircle(width / 2, width / 2, (pxsPer20Bpm * ((maxBPM / minBPM) - 2)), mainRedZonePaint);
        canvas.drawCircle(width / 2, width / 2, pxsPer20Bpm, mainRedZonePaint);

        mainLinePaint.setColor(Color.BLACK);
        mainLinePaint.setStrokeWidth(3);
        canvas.drawCircle(width / 2, width / 2, radius, mainLinePaint);
        //==========================================================================================
    }

    private void init() {
        mainRedZonePaint.setStyle(Paint.Style.STROKE);
        mainRedZonePaint.setColor(Color.RED);
        mainLinePaint.setStyle(Paint.Style.STROKE);
    }
}
