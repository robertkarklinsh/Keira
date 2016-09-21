package com.gattaca.team.ui.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class Bpm extends View {
    private Paint mPaint;
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

    private void init() {

    }
}
