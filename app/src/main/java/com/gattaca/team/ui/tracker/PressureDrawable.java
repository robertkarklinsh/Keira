package com.gattaca.team.ui.tracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Artem on 27.09.2016.
 */

public class PressureDrawable extends View {
    public PressureDrawable(Context context) {
        super(context);
        this.setWillNotDraw(false);
        this.setMeasuredDimension(996,99);
        this.invalidate();
    }

    public PressureDrawable(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setWillNotDraw(false);
        this.setMeasuredDimension(996,99);
        this.invalidate();
    }

    public PressureDrawable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setWillNotDraw(false);
        this.setMeasuredDimension(996,99);
        this.invalidate();
    }


    @Override
    public void onDraw(Canvas canvas) {
        Rect rect = new Rect(0,canvas.getHeight(),canvas.getWidth(),0);
        Paint paint = new Paint();
        paint.setARGB(255, 21, 165, 153);

        canvas.drawRect(rect, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(996,99);
    }

    //    int min;
//    int max;
//    int pos;
//
//
//
//
//    @Override
//    public int getIntrinsicWidth() {
//        return 996;
//    }
//
//    @Override
//    public int getIntrinsicHeight() {
//        return 99;
//    }
//
//    public PressureDrawable(int min, int max, int pos) {
//        this.min = min;
//        this.max = max;
//        this.pos = pos;
//
//    }
//
//    @Override
//    public void draw(Canvas canvas) {
//        Rect rect = getBounds();
//
//        Paint paint = new Paint();
//        paint.setARGB(255, 21, 165, 153);
////        float rad = 10;
////        int centerOfBigCircle = (int)Math.round((double)pos / (double)(max - min));
////        int readOfBigCircle = 33;
//
//        canvas.drawRect(rect, paint);
////        canvas.drawCircle();
//
//    }
//
//    @Override
//    public void setAlpha(int alpha) {
//
//    }
//
//    @Override
//    public void setColorFilter(ColorFilter colorFilter) {
//
//    }
//
//    @Override
//    public int getOpacity() {
//        return PixelFormat.TRANSLUCENT;
//    }
}
