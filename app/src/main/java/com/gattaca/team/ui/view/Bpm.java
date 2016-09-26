package com.gattaca.team.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.gattaca.team.ui.model.impl.BpmModel;

import java.util.ArrayList;

public class Bpm extends TextView {
    private final static int times = 12;
    private static float heightTextPts = 0;
    private final DashPathEffect dashPathEffect = new DashPathEffect(new float[]{5, 5}, 0);
    private Paint mainLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mainRedZonePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mTimePath = new Path(), mPointsPath = new Path();
    private float selectedValue = 0;
    private ArrayList<String> timeSrc = null;

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

    public void install(BpmModel model) {
        model.print();
        setText("" + selectedValue);
        timeSrc = model.formatTimes(times, 3);
        mPointsPath.reset();
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
        final int width = getMeasuredWidth(), x0 = width / 2, y0 = width / 2;
        final int innerRadius = (int) (0.1 * width);
        final int bpmRadius = (int) (0.30 * width);
        final int outRadius = (int) (0.05 * width);
        final int fullRadius = bpmRadius + innerRadius + outRadius;
        final int linesOffset = width - 2 * fullRadius;
        final int minBPM = 20;
        final int maxBPM = 140;
        final int pxsPer20Bpm = minBPM * bpmRadius / (maxBPM - minBPM);
        final int pointBPM = pxsPer20Bpm / minBPM;
        final double arc = 2 * Math.PI * (fullRadius - outRadius) / times;

        // draw main elements ======================================================================
        mainRedZonePaint.setStrokeWidth(pxsPer20Bpm);
        canvas.drawCircle(x0, y0, fullRadius - outRadius - pxsPer20Bpm / 2, mainRedZonePaint);
        canvas.drawCircle(x0, y0, innerRadius + pxsPer20Bpm / 2, mainRedZonePaint);

        mainLinePaint.setStyle(Paint.Style.STROKE);
        mainLinePaint.setColor(Color.GRAY);
        mainLinePaint.setAlpha(60);
        mainLinePaint.setStrokeWidth((float) (innerRadius * 0.1));
        canvas.drawCircle(x0, y0, (float) (innerRadius * 0.95), mainLinePaint);
        mainLinePaint.setStrokeWidth(outRadius);
        canvas.drawCircle(x0, y0, fullRadius - outRadius / 2, mainLinePaint);

        mainLinePaint.setStrokeWidth(6);
        mainLinePaint.setColor(Color.BLACK);
        mainLinePaint.setAlpha(100);
        canvas.save();
        for (int i = 0; i < times; i++) {
            canvas.drawLine(x0, linesOffset, x0, linesOffset - outRadius, mainLinePaint);
            canvas.rotate(30, x0, y0);
        }
        canvas.restore();

        mainLinePaint.setStrokeWidth(2);
        mainLinePaint.setStyle(Paint.Style.FILL);
        mTimePath.addCircle(x0, y0, fullRadius - outRadius, Path.Direction.CW);
        for (int i = 0; i < times; i++) {
            canvas.drawTextOnPath(
                    timeSrc.get(i),
                    mTimePath,
                    (float) (arc * 0.05 + i * arc),
                    -(outRadius - heightTextPts) / 2,
                    mainLinePaint);
        }
        //==========================================================================================
        mainLinePaint.setStrokeWidth(2);
        canvas.drawPath(mPointsPath, mainLinePaint);
        super.onDraw(canvas);
    }

    private void init() {
        mainRedZonePaint.setStyle(Paint.Style.STROKE);
        mainRedZonePaint.setColor(Color.RED);
        mainRedZonePaint.setAlpha(90);

        mainLinePaint.setTextSize(14 * getResources().getDisplayMetrics().density);
        final Rect r = new Rect();
        final String text = "99:99";
        mainLinePaint.getTextBounds(text, 0, text.length(), r);
        heightTextPts = r.height();

        setGravity(Gravity.CENTER);
        setTextColor(Color.GRAY);
        setTextSize(20);
        setAlpha(0.8f);
    }
}
