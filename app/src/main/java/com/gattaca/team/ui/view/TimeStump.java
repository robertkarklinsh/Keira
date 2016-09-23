package com.gattaca.team.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class TimeStump extends TextView {

    public TimeStump(Context context) {
        super(context);
    }

    public TimeStump(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeStump(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTime(final long time) {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:SSS");
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        setText(formatter.format(calendar.getTime()));
    }
}
