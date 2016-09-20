package com.gattaca.team.service.analysis;

public enum NotifyEvent {
    // PC events
    PC_Period_5_min(NotifyEventTag.Yellow),
    PC_Period_15_min(NotifyEventTag.Yellow),
    PC_Period_30_min(NotifyEventTag.Yellow),
    PC_Period_1_hour(NotifyEventTag.Yellow),
    PC_Period_12_hours(NotifyEventTag.Yellow),
    PC_Period_24_hours(NotifyEventTag.Yellow),
    PC_2(NotifyEventTag.Yellow),
    PC_more_limit_per_hour(NotifyEventTag.Yellow),
    PC_more_limit_per_day(NotifyEventTag.Yellow),
    PC_3(NotifyEventTag.Red),
    // PULSE (BPM) events
    BPM_less_40(NotifyEventTag.Yellow),
    BPM_less_50_more_40(NotifyEventTag.Yellow),
    BPM_more_100(NotifyEventTag.Yellow),
    // AD events
    AD_more_140_160(),
    AD_more_160_180(),
    AD_more_180(),
    AD_again_less_140_90(),
    AD_again_more_140_90(),;

    private final NotifyEventTag[] tags;
    private long time = 0L;

    NotifyEvent(NotifyEventTag... tags) {
        this.tags = tags;
    }

    public NotifyEventTag[] getTags() {
        return this.tags;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
