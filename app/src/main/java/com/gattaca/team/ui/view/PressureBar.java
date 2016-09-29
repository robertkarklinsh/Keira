package com.gattaca.team.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.gattaca.team.R;


public final class PressureBar extends View {
    final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    PressureType type = PressureType.systolic;
    double pressureBarStepPer1;
    int radiusBackground,
            radiusPin,
            offsetPin,
            colorRedMain,
            colorRedLow,
            colorOrangeMain,
            colorOrangeLow,
            colorGreenMain,
            colorGreenLow,
            colorGray,
            value;
    Rect r = new Rect();

    public PressureBar(Context context) {
        super(context);
        init(null, context);
    }

    public PressureBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public PressureBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    void init(final AttributeSet attrs, final Context context) {
        final Resources res = context.getResources();
        radiusBackground = res.getDimensionPixelSize(R.dimen.pressureBar_background_radius);
        radiusPin = res.getDimensionPixelSize(R.dimen.pressureBar_pin_radius);
        offsetPin = res.getDimensionPixelSize(R.dimen.pressureBar_offset);

        colorRedMain = res.getColor(R.color.pressure_colorRedMain);
        colorRedLow = res.getColor(R.color.pressure_colorRedLow);
        colorOrangeMain = res.getColor(R.color.pressure_colorOrangeMain);
        colorOrangeLow = res.getColor(R.color.pressure_colorOrangeLow);
        colorGreenMain = res.getColor(R.color.pressure_colorGreenMain);
        colorGreenLow = res.getColor(R.color.pressure_colorGreenLow);
        colorGray = res.getColor(R.color.pressure_colorGray);

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // mPaint.setLetterSpacing(-1);

        if (attrs != null) {
            final TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.PressureBarStyle);
            if (arr.hasValue(R.styleable.PressureBarStyle_type)) {
                setType(PressureType.values()[arr.getInt(R.styleable.PressureBarStyle_type, 0)]);
            }
            arr.recycle();
        }
    }

    public void setType(PressureType type) {
        this.type = type;
    }

    public void setValue(int v) {
        value = v;
        if (value < type.pressureMin) {
            value = type.pressureMin;
        }
        if (value > type.pressureMax) {
            value = type.pressureMax;
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int w = MeasureSpec.getSize(widthMeasureSpec);
        if (w > 0) {
            setMeasuredDimension(w, (int) (radiusPin * 2.1));
            pressureBarStepPer1 = ((w - 2 * (double) (radiusBackground + offsetPin)) / type.getSize());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (pressureBarStepPer1 > 0) {
            final int xPin = (int) (radiusBackground + offsetPin + (value - type.pressureMin) * pressureBarStepPer1);
            mPaint.setColor(colorGray);
            drawLine(canvas, xPin, getMeasuredWidth() - radiusBackground);
            canvas.drawCircle(getMeasuredWidth() - radiusBackground, radiusPin, radiusBackground, mPaint);

            final ColorRegion colorRegion = type.getRegion(value);
            switch (colorRegion) {
                case Red:
                    mPaint.setColor(colorRedLow);
                    break;
                case Orange:
                    mPaint.setColor(colorOrangeLow);
                    break;
                case Green:
                    mPaint.setColor(colorGreenLow);
                    break;
            }
            drawLine(canvas, radiusBackground, xPin);
            switch (colorRegion) {
                case Red:
                    mPaint.setColor(colorRedMain);
                    break;
                case Orange:
                    mPaint.setColor(colorOrangeMain);
                    break;
                case Green:
                    mPaint.setColor(colorGreenMain);
                    break;
            }
            canvas.drawCircle(radiusBackground, radiusPin, radiusBackground, mPaint);
            canvas.drawCircle(xPin, radiusPin, radiusPin, mPaint);

            mPaint.setColor(Color.WHITE);
            String text = getResources().getString(type.strRes);
            mPaint.getTextBounds(text, 0, text.length(), r);
            mPaint.setTextSize(type == PressureType.systolic ? 44 : 38);
            canvas.drawText(text, radiusBackground - r.width() / 2, radiusPin + r.height() / 2 - 4, mPaint);
            text = "" + value;
            mPaint.getTextBounds(text, 0, text.length(), r);
            mPaint.setTextSize(50);
            canvas.drawText(text, xPin - r.width() / 2 - 10, radiusPin + r.height() / 2, mPaint);
        }
    }

    private void drawLine(final Canvas canvas, int x0, int x1) {
        mPaint.setStrokeWidth(2 * radiusBackground);
        canvas.drawLine(x0, radiusPin, x1, radiusPin, mPaint);
        mPaint.setStrokeWidth(1);
    }

    enum ColorRegion {
        Red, Orange, Green
    }

    public enum PressureType {
        systolic(R.string.pressureBar_systolic, 60, 80, 90, 140, 180, 200),
        diastolic(R.string.pressureBar_diastolic, 30, 50, 60, 90, 110, 130);

        final int strRes;
        final int pressureMax;
        final int pressureMin;
        final int red1;
        final int orange1;
        final int orange2;
        final int red2;

        PressureType(int strRes, int pressureMin, int red1, int orange1, int orange2, int red2, int pressureMax) {
            this.strRes = strRes;
            this.pressureMax = pressureMax;
            this.pressureMin = pressureMin;
            this.red1 = red1;
            this.orange1 = orange1;
            this.orange2 = orange2;
            this.red2 = red2;
        }

        public int getSize() {
            return pressureMax - pressureMin;
        }

        public ColorRegion getRegion(int value) {
            ColorRegion a = ColorRegion.Green;
            if (value < red1 || value > red2) {
                a = ColorRegion.Red;
            } else if (value < orange1 || value > orange2) {
                a = ColorRegion.Orange;
            }
            return a;
        }
    }
}
