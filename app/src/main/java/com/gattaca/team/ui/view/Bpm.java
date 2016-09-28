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
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.gattaca.team.R;
import com.gattaca.team.root.AppConst;
import com.gattaca.team.ui.model.impl.BpmModel;

import java.util.ArrayList;
import java.util.List;

public final class Bpm extends TextView implements View.OnTouchListener {
    private final static int pointsInRealTimeMode = 180;
    private final static int times = 15;
    private static float heightTextPts = 0;
    private final CornerPathEffect cornerPathEffect = new CornerPathEffect(30);
    double arc = 0;
    private int
            green,
            red,
            width = 0,
            x0 = 0,
            y0 = 0,
            innerRadius = 0,
            outRadius = 0,
            fullRadius = 0,
            linesOffset = 0,
            pxsPer1Bpm = 0,
            pinSize = 0,
            selectedPosition = 0;
    private Paint
            mainLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG),
            redZone = new Paint(Paint.ANTI_ALIAS_FLAG),
            greenZone = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Path
            mTimePath = new Path(),
            mRedOutResetPath = new Path(),
            mRedInPath = new Path(),
            mGreenPath = new Path(),
            mGreenResetPath = new Path(),
            mPointsPath = new Path();

    private ArrayList<String> timeSrc = new ArrayList<>();
    private BpmModel model = null;
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            if (width == 0) {
                new Handler().postDelayed(this, 100);
            } else {
                mPointsPath.rewind();
                timeSrc = model.formatTimes(times);
                final List<Float> data = model.getData();
                double a = (double) 360 / (model.isRealTime() ? pointsInRealTimeMode : data.size());
                int idx = 0;
                for (float f : data) {
                    if (f > AppConst.maxBPM) f = AppConst.maxBPM;
                    setPoint(mPointsPath, mathBpm(f, idx++, a));
                }
                mGreenPath.rewind();
                mGreenResetPath.rewind();
                idx = 0;
                final List<BpmModel.BpmColorRegion> greens = model.getGreenData();
                a = (double) 360 / greens.size();
                for (BpmModel.BpmColorRegion region : greens) {
                    setPoint(mGreenResetPath, mathBpm(region.getBottom(), idx, a));
                    setPoint(mGreenPath, mathBpm(region.getTop(), idx, a));
                    idx++;
                }
                mGreenResetPath.close();
                mGreenPath.close();

                mRedOutResetPath.rewind();
                mRedInPath.rewind();
                idx = 0;
                final List<BpmModel.BpmColorRegion> red = model.getRedData();
                a = (double) 360 / greens.size();
                for (BpmModel.BpmColorRegion region : red) {
                    setPoint(mRedInPath, mathBpm(region.getBottom(), idx, a));
                    setPoint(mRedOutResetPath, mathBpm(region.getTop(), idx, a));
                    idx++;
                }
                mRedOutResetPath.close();
                mRedInPath.close();
                updatePin(data.size() - 1);
                postInvalidate();
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

    private static void setPoint(Path p, float... points) {
        if (p.isEmpty()) {
            p.setLastPoint(points[0], points[1]);
        } else {
            p.lineTo(points[0], points[1]);
        }
    }

    public void install(BpmModel model) {
        this.model = model;
        new Handler().postDelayed(this.r, 100);
    }

    public void addRealTimePoint(float point, final long time) {
        model.addPoint(point, time);
        setPoint(mPointsPath, mathBpm(point, model.getData().size(), (double) 360 / pointsInRealTimeMode));

        if (model.isRealTime() && selectedPosition == model.getData().size() - 2) {
            updatePin(model.getData().size() - 1);
        } else {
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int w = MeasureSpec.getSize(widthMeasureSpec);
        if (w > 0) {
            this.width = w;
            x0 = w / 2;
            y0 = w / 2;
            innerRadius = (int) (0.10 * width);
            outRadius = (int) (0.05 * width);
            fullRadius = innerRadius + outRadius;
            final int bpmRadius = (int) (x0 - fullRadius - 0.05 * width);
            fullRadius += bpmRadius;
            linesOffset = x0 - fullRadius;
            pxsPer1Bpm = bpmRadius / (AppConst.maxBPM - AppConst.minBPM);
            arc = 2 * Math.PI * (fullRadius - outRadius) / times;
            pinSize = (int) (fullRadius - innerRadius + 0.025 * width);

            mTimePath.addCircle(x0, y0, fullRadius - outRadius, Path.Direction.CW);

            setMeasuredDimension(width, width);
        }
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (model != null && !model.getData().isEmpty()) {
            // draw time elements ======================================================================
            mainLinePaint.setColor(Color.GRAY);
            mainLinePaint.setStrokeWidth(outRadius);
            canvas.drawCircle(x0, y0, fullRadius - outRadius / 2, mainLinePaint);

            mainLinePaint.setStrokeWidth(6);
            mainLinePaint.setColor(Color.WHITE);
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
            mainLinePaint.setStyle(Paint.Style.STROKE);
            // draw zones ==============================================================================
            redZone.setColor(red);
            canvas.drawCircle(x0, y0, fullRadius - outRadius, redZone);
            redZone.setColor(Color.WHITE);
            canvas.drawPath(mRedOutResetPath, redZone);
            greenZone.setColor(green);
            canvas.drawPath(mGreenPath, greenZone);
            greenZone.setColor(Color.WHITE);
            canvas.drawPath(mGreenResetPath, greenZone);
            redZone.setColor(red);
            canvas.drawPath(mRedInPath, redZone);
            mainLinePaint.setStrokeWidth(1);
            mainLinePaint.setColor(Color.BLACK);
            float[] pin = math(
                    pinSize,
                    selectedPosition,
                    (double) 360 / (model.isRealTime() ? pointsInRealTimeMode : model.getData().size()),
                    1);
            canvas.drawLine(x0, y0, pin[0], pin[1], mainLinePaint);
            mainLinePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(pin[0], pin[1], (float) (0.01 * width), mainLinePaint);
            redZone.setColor(Color.WHITE);
            canvas.drawCircle(x0, y0, innerRadius, redZone);

            mainLinePaint.setStyle(Paint.Style.STROKE);
            mainLinePaint.setColor(Color.GRAY);
            mainLinePaint.setStrokeWidth((float) (innerRadius * 0.1));
            canvas.drawCircle(x0, y0, (float) (innerRadius * 0.95), mainLinePaint);
            //==========================================================================================
            mainLinePaint.setStrokeWidth(8);
            mainLinePaint.setColor(Color.BLACK);
            mainLinePaint.setPathEffect(cornerPathEffect);
            canvas.drawPath(mPointsPath, mainLinePaint);

            mainLinePaint.setStrokeWidth(4);
            mainLinePaint.setColor(Color.CYAN);
            pin = mathBpm(
                    model.getIntValueByPosition(selectedPosition),
                    selectedPosition,
                    (double) 360 / (model.isRealTime() ? pointsInRealTimeMode : model.getData().size()));
            canvas.drawCircle(pin[0], pin[1], (float) (0.01 * width), mainLinePaint);
        }
        super.onDraw(canvas);
    }

    private void init() {
        greenZone.setStyle(Paint.Style.FILL_AND_STROKE);
        greenZone.setPathEffect(cornerPathEffect);
        greenZone.setStrokeWidth(5);

        redZone.setStyle(Paint.Style.FILL_AND_STROKE);

        mainLinePaint.setTextSize(14 * getResources().getDisplayMetrics().density);
        final Rect r = new Rect();
        final String text = "99:99";
        mainLinePaint.getTextBounds(text, 0, text.length(), r);
        heightTextPts = r.height();

        setGravity(Gravity.CENTER);
        setTextColor(Color.GRAY);
        setTextSize(20);

        green = getResources().getColor(R.color.green);
        red = getResources().getColor(R.color.red);

        setOnTouchListener(this);
    }

    private void updatePin(int position) {
        selectedPosition = position;
        setText(model.getStringValueByPosition(selectedPosition));
    }

    private float[] math(float f, final int idx, final double a, int pointsPerValue) {
        final float[] ret = new float[2];
        final double radian = Math.toRadians(idx * a);
        ret[0] = x0 + (float) ((innerRadius + f * pointsPerValue) * Math.sin(radian));
        ret[1] = y0 - (float) ((innerRadius + f * pointsPerValue) * Math.cos(radian));
        return ret;
    }

    private float[] mathBpm(float f, final int idx, final double a) {
        return math(f, idx, a, pxsPer1Bpm);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
            final double a = (double) 360 / (model.isRealTime() ? pointsInRealTimeMode : model.getData().size());
            final float x = event.getX() - x0, y = y0 - event.getY();
            final double b1 = Math.atan(x / y);
            double b = Math.toDegrees(b1);
            if ((y < 0 && x > 0) || (y < 0 && x < 0)) {
                b += 180;
            } else if (y > 0 && x < 0) {
                b += 360;
            }

            for (int idx = 0; idx < model.getData().size(); idx++) {
                if (idx * a >= b) {
                    updatePin(idx);
                    break;
                }
            }
        }
        return true;
    }
}
