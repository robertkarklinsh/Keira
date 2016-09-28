package com.gattaca.team.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public final class BpmValue extends TextView {

    public BpmValue(Context context) {
        super(context);
    }

    public BpmValue(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BpmValue(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        setVisibility(GONE);
    }

    public void setBpm(final float value) {
        setVisibility(VISIBLE);
        setText("" + (int) value);
    }
}
