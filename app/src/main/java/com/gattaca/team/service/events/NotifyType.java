package com.gattaca.team.service.events;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({NotifyType.PC_Period_5_min,
        NotifyType.PC_Period_15_min,
        NotifyType.PC_Period_30_min,
        NotifyType.PC_Period_1_hour,
        NotifyType.PC_Period_12_hours,
        NotifyType.PC_Period_24_hours,
        NotifyType.PC_more_limit_per_hour,
        NotifyType.PC_more_limit_per_day,
        NotifyType.PC_2,
        NotifyType.PC_3,
        NotifyType.BPM_less_40,
        NotifyType.BPM_less_50_more_40,
        NotifyType.BPM_more_100,
        NotifyType.AD_more_140_160,
        NotifyType.AD_more_160_180,
        NotifyType.AD_more_180,
        NotifyType.AD_trouble,
        NotifyType.AD_again_less_140_90,
        NotifyType.AD_again_more_140_90})
@Retention(RetentionPolicy.SOURCE)
public @interface NotifyType {
    int PC_Period_5_min = 0;
    int PC_Period_15_min = 1;
    int PC_Period_30_min = 2;
    int PC_Period_1_hour = 3;
    int PC_Period_12_hours = 4;
    int PC_Period_24_hours = 5;
    int PC_more_limit_per_hour = 6;
    int PC_more_limit_per_day = 7;
    int PC_2 = 8;
    int PC_3 = 9;
    // PULSE (BPM) events
    int BPM_less_40 = 10;
    int BPM_less_50_more_40 = 11;
    int BPM_more_100 = 12;
    // AD events
    int AD_more_140_160 = 13;
    int AD_more_160_180 = 14;
    int AD_more_180 = 15;
    int AD_trouble = 16;
    int AD_again_less_140_90 = 17;
    int AD_again_more_140_90 = 18;
}
