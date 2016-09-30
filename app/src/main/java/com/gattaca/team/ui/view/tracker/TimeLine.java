package com.gattaca.team.ui.view.tracker;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.team.R;
import com.gattaca.team.annotation.GraphPeriod;
import com.gattaca.team.root.AppUtils;
import com.squareup.otto.Subscribe;

public final class TimeLine extends LinearLayout implements ITick {
    private TextView first, second;
    private long value = 0;
    private
    @GraphPeriod
    long period;

    public TimeLine(Context context) {
        super(context);
    }

    public TimeLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        first = (TextView) findViewById(R.id.first);
        second = (TextView) findViewById(R.id.second);
        setMaxTime(GraphPeriod.period_5min);
        setTextColor(Color.Green);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            AppUtils.registerBus(this);
        } else {
            AppUtils.unregisterBus(this);
        }
    }

    void setMaxTime(@GraphPeriod long period) {
        this.period = period;
        second.setText(period / DateUtils.MINUTE_IN_MILLIS + " мин ");
    }

    @Override
    public void setValue(long ms) {
        value = ms;
        final int m = (int) (ms / DateUtils.MINUTE_IN_MILLIS);
        final StringBuilder sb = new StringBuilder();
        if (m > 0) {
            sb.append(m).append(" мин ");
        }
        sb.append(ms / DateUtils.SECOND_IN_MILLIS - m * 60).append(" сек / ");
        first.setText(sb.toString());

        if (ms > period) {
            if (period == GraphPeriod.period_5min) {
                setMaxTime(GraphPeriod.period_15min);
            } else if (period == GraphPeriod.period_15min) {
                setMaxTime(GraphPeriod.period_30min);
            } else if (period == GraphPeriod.period_30min) {
                setMaxTime(GraphPeriod.period_1hour);
            }
        }
    }

    public void setTextColor(Color value) {
        first.setTextColor(getResources().getColor(value == Color.Green ? R.color.pressure_colorGreenLow : R.color.pressure_colorOrangeLow));
    }

    @Override
    @Subscribe
    public void tick(Integer a) {
        setValue(value + DateUtils.SECOND_IN_MILLIS);
    }
}
