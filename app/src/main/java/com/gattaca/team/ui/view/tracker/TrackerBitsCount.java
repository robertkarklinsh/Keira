package com.gattaca.team.ui.view.tracker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.team.R;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.root.MainApplication;
import com.squareup.otto.Subscribe;

public final class TrackerBitsCount extends LinearLayout {
    private TextView counts;
    public static long value = 0;

    public TrackerBitsCount(Context context) {
        super(context);
    }

    public TrackerBitsCount(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrackerBitsCount(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        counts = (TextView) findViewById(R.id.counts);
    }

    Boolean started = false;
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            if (!started) {
                AppUtils.registerBus(this);
                MainApplication.register(this);
                started = true;
            }
        } else {
//            AppUtils.unregisterBus(this);
        }
    }

    public void setValue(long value) {
        this.value = value;
        counts.setText("" + value);
    }

//    @Subscribe
    public void tick(Integer a) {
        setValue(value + a);
    }

    @Subscribe
    public void tick1(StubTrackerBit a) {
        setValue(value + 1);
    }
}
