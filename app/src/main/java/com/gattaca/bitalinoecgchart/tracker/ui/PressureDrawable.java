package com.gattaca.bitalinoecgchart.tracker.ui;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by Artem on 27.09.2016.
 */

public class PressureDrawable extends Drawable {

    int min;
    int max;
    int pos;

    public PressureDrawable(int min, int max, int pos) {
        this.min = min;
        this.max = max;
        this.pos = pos;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();

        Paint paint = new Paint();
        paint.setARGB(255, 21, 165, 153);
//        float rad = 10;
//        int centerOfBigCircle = (int)Math.round((double)pos / (double)(max - min));
//        int readOfBigCircle = 33;

        canvas.drawRect(rect, paint);
//        canvas.drawCircle();

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
