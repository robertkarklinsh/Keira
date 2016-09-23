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

    public static String convert(final long time, final String scheme) {
        final SimpleDateFormat formatter = new SimpleDateFormat(scheme);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }

    public void setTime(final long time) {
        setText(convert(time, "dd/MM/yyyy hh:mm:ss:SSS"));
    }
}
