package com.gattaca.team.ui.view.tracker;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.team.R;
import com.gattaca.team.db.event.NotifyEventObject;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.root.MainApplication;
import com.squareup.otto.Subscribe;


public final class MeasureProgress extends LinearLayout implements ITick {
    MeasureBar bar;
    TextView valueEvents;
    static View problems, noProblems;
    public static int events = 0;

    public static void resetView()
    {
        if (problems != null) {
            problems.setVisibility(GONE);
            ((TextView) problems.findViewById(R.id.counts)).setText("" + 0);
        }
        if (noProblems != null) {
            noProblems.setVisibility(VISIBLE);
        }
    }

    public MeasureProgress(Context context) {
        super(context);
    }

    public MeasureProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasureProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        bar = (MeasureBar) findViewById(R.id.bar);
        valueEvents = (TextView) findViewById(R.id.counts);
        problems = findViewById(R.id.hasProblems);
        noProblems = findViewById(R.id.noProblems);
        problems.setVisibility(GONE);
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

    @Override
    public void setValue(long value) {
        this.bar.setValue(value);
    }

    @Override
    @Subscribe
    public void tick(Integer a) {
        setValue(bar.getValue() + DateUtils.SECOND_IN_MILLIS);
    }

    @Subscribe
    public void event(NotifyEventObject a) {
        bar.addPoint();
        events++;
        if (events == 1) {
            problems.setVisibility(VISIBLE);
            noProblems.setVisibility(GONE);
        }
        valueEvents.setText("" + events);
    }
}
