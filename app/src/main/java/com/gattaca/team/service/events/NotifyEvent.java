package com.gattaca.team.service.events;

import java.util.ArrayList;

public final class NotifyEvent {
    private final
    @NotifyType
    int eventType;
    private final
    @NotifyEventTag
    int[] tags;
    private ArrayList<Long> timings = new ArrayList<>();
    private int count = 0;

    public NotifyEvent(@NotifyType int eventType) {
        this.eventType = eventType;
        switch (this.eventType) {
            case NotifyType.AD_again_less_140_90:
                tags = new int[]{};
                break;
            case NotifyType.AD_again_more_140_90:
                tags = new int[]{NotifyEventTag.Red};
                break;
            case NotifyType.AD_more_140_160:
                tags = new int[]{};
                break;
            case NotifyType.AD_more_160_180:
                tags = new int[]{};
                break;
            case NotifyType.AD_more_180:
                tags = new int[]{};
                break;
            case NotifyType.AD_trouble:
                tags = new int[]{NotifyEventTag.Red};
                break;
            case NotifyType.BPM_less_40:
                tags = new int[]{NotifyEventTag.Yellow};
                break;
            case NotifyType.BPM_less_50_more_40:
                tags = new int[]{NotifyEventTag.Yellow};
                break;
            case NotifyType.BPM_more_100:
                tags = new int[]{NotifyEventTag.Yellow};
                break;
            case NotifyType.PC_2:
                tags = new int[]{NotifyEventTag.Yellow};
                break;
            case NotifyType.PC_3:
                tags = new int[]{NotifyEventTag.Red};
                break;
            case NotifyType.PC_Period_12_hours:
                tags = new int[]{NotifyEventTag.Yellow};
                break;
            case NotifyType.PC_Period_15_min:
                tags = new int[]{NotifyEventTag.Yellow};
                break;
            case NotifyType.PC_Period_1_hour:
                tags = new int[]{NotifyEventTag.Yellow};
                break;
            case NotifyType.PC_Period_24_hours:
                tags = new int[]{NotifyEventTag.Yellow};
                break;
            case NotifyType.PC_Period_30_min:
                tags = new int[]{NotifyEventTag.Yellow};
                break;
            case NotifyType.PC_Period_5_min:
                tags = new int[]{NotifyEventTag.Yellow};
                break;
            case NotifyType.PC_more_limit_per_day:
                tags = new int[]{NotifyEventTag.Yellow};
                break;
            case NotifyType.PC_more_limit_per_hour:
                tags = new int[]{NotifyEventTag.Yellow};
                break;
            default:
                tags = new int[]{};
                break;
        }
    }

    public
    @NotifyType
    int getEventType() {
        return this.eventType;
    }

    public
    @NotifyEventTag
    int[] getEventTags() {
        return this.tags;
    }

    public NotifyEvent addTime(long time) {
        this.timings.add(time);
        return this;
    }

}
