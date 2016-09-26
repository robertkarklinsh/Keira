package com.gattaca.team.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.gattaca.team.ui.model.impl.BpmModel;

import java.util.ArrayList;
import java.util.List;

public class Bpm extends TextView {
    private final static int times = 15;
    private static float heightTextPts = 0;
    final int minBPM = 20;
    final int maxBPM = 140;
    private final CornerPathEffect cornerPathEffect = new CornerPathEffect(30);
    double arc = 0;
    private int
            width = 0,
            x0 = 0,
            y0 = 0,
            innerRadius = 0,
            bpmRadius = 0,
            outRadius = 0,
            fullRadius = 0,
            linesOffset = 0,
            pxsPer20Bpm = 0,
            pxsPer1Bpm = 0,
            selectedValue = 666;
    private Paint
            mainLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG),
            redZone = new Paint(Paint.ANTI_ALIAS_FLAG),
            greenZone = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Path
            mTimePath = new Path(),
            mGreenPath = new Path(),
            mPointsPath = new Path();
    private ArrayList<String> timeSrc = new ArrayList<>();
    private BpmModel model = null;
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            if (width == 0) {
                new Handler().postDelayed(this, 100);
            } else {
                mPointsPath.reset();
                timeSrc = model.formatTimes(times);
                final List<Float> data = model.getData();
                double a = (double) 360 / data.size();
                int idx = 0;
                for (float f : data) {
                    setPoint(mPointsPath, math(f, idx++, a));
                }
                final Path p1 = new Path(), p2 = new Path();
                mGreenPath.reset();
                idx = 0;
                final List<BpmModel.BpmGreenRegion> greens = model.getGreenData();
                a = (double) 360 / greens.size();
                for (BpmModel.BpmGreenRegion greenRegion : greens) {
                    setPoint(p1, math(greenRegion.getBottom(), idx++, a));
                    setPoint(p2, math(greenRegion.getTop(), idx++, a));
                }
                // mGreenPath.addPath(p1);
                mGreenPath.addPath(p2);
                //postInvalidate();
                setText("" + selectedValue);
            }
        }

        private float[] math(float f, final int idx, final double a) {
            final float[] ret = new float[2];
            final double radian = Math.toRadians(idx * a);
            ret[0] = x0 + (float) ((innerRadius + f * pxsPer1Bpm) * Math.sin(radian));
            ret[1] = y0 - (float) ((innerRadius + f * pxsPer1Bpm) * Math.cos(radian));
            return ret;
        }

        private void setPoint(Path p, float... points) {
            if (p.isEmpty()) {
                p.setLastPoint(points[0], points[1]);
            } else {
                p.lineTo(points[0], points[1]);
            }
        }
    };

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
        this.model = model;
        new Handler().postDelayed(this.r, 100);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int w = MeasureSpec.getSize(widthMeasureSpec);
        if (w > 0) {
            this.width = w;
            x0 = w / 2;
            y0 = w / 2;
            innerRadius = (int) (0.1 * width);
            bpmRadius = (int) (0.30 * width);
            outRadius = (int) (0.05 * width);
            fullRadius = bpmRadius + innerRadius + outRadius;
            linesOffset = x0 - fullRadius;
            pxsPer1Bpm = bpmRadius / (maxBPM - minBPM);
            pxsPer20Bpm = minBPM * pxsPer1Bpm;
            arc = 2 * Math.PI * (fullRadius - outRadius) / times;

            mTimePath.addCircle(x0, y0, fullRadius - outRadius, Path.Direction.CW);

            setMeasuredDimension(width, width);
        }
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mGreenPath, greenZone);
        // draw main elements ======================================================================
        redZone.setStrokeWidth(pxsPer20Bpm);
        mainLinePaint.setPathEffect(null);
        canvas.drawCircle(x0, y0, fullRadius - outRadius - pxsPer20Bpm / 2, redZone);
        canvas.drawCircle(x0, y0, innerRadius + pxsPer20Bpm / 2, redZone);

        mainLinePaint.setStyle(Paint.Style.STROKE);
        mainLinePaint.setColor(Color.GRAY);
        // mainLinePaint.setAlpha(100);
        mainLinePaint.setStrokeWidth((float) (innerRadius * 0.1));
        canvas.drawCircle(x0, y0, (float) (innerRadius * 0.95), mainLinePaint);
        mainLinePaint.setStrokeWidth(outRadius);
        canvas.drawCircle(x0, y0, fullRadius - outRadius / 2, mainLinePaint);

        mainLinePaint.setStrokeWidth(6);
        mainLinePaint.setColor(Color.WHITE);
        // mainLinePaint.setAlpha(255);
        canvas.save();
        for (int i = 0; i < times; i++) {
            canvas.drawLine(x0, linesOffset, x0, linesOffset + outRadius, mainLinePaint);
            canvas.rotate(360 / times, x0, y0);
        }
        canvas.restore();

        mainLinePaint.setStrokeWidth(1);
        mainLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.save();
        canvas.rotate(-90, x0, y0);
        for (int i = 0; i < timeSrc.size(); i++) {
            canvas.drawTextOnPath(
                    timeSrc.get(i),
                    mTimePath,
                    (float) (arc * 0.05 + i * arc),
                    -(outRadius - heightTextPts) / 2,
                    mainLinePaint);
        }
        canvas.restore();
        //==========================================================================================
        mainLinePaint.setStrokeWidth(8);
        mainLinePaint.setColor(Color.CYAN);
        mainLinePaint.setStyle(Paint.Style.STROKE);
        mainLinePaint.setPathEffect(cornerPathEffect);
        canvas.drawPath(mPointsPath, mainLinePaint);
        super.onDraw(canvas);
    }

    private void init() {
        greenZone.setStyle(Paint.Style.STROKE);
        greenZone.setColor(Color.GREEN);
        greenZone.setPathEffect(cornerPathEffect);
        greenZone.setStrokeWidth(10);

        redZone.setStyle(Paint.Style.STROKE);
        redZone.setColor(Color.RED);
        //redZone.setAlpha(50);

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
