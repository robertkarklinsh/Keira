package com.gattaca.team.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.gattaca.team.annotation.NotifyEventTag;

public final class MarkerByNotifyTag extends View {
    public MarkerByNotifyTag(Context context) {
        super(context);
    }

    public MarkerByNotifyTag(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarkerByNotifyTag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTag(@NotifyEventTag int tag) {
        switch (tag) {
            case NotifyEventTag.Red:
                setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                break;
            case NotifyEventTag.Yellow:
                setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                break;
            case NotifyEventTag.Green:
                setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                break;
            default:
                setBackgroundColor(getResources().getColor(android.R.color.transparent));
                break;
        }
    }
}
