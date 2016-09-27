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
    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();

        Paint paint = new Paint();
        paint.setARGB(255, 255, 0, 0);
        float rad = 10;
        canvas.drawRoundRect((float) rect.left,(float) rect.top, (float)rect.right, (float)rect.bottom, rad, rad , paint);
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
