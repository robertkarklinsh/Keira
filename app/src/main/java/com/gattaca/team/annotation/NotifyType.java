package com.gattaca.team.annotation;

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
        NotifyType.PC_detect,
        NotifyType.BPM_less_40,
        NotifyType.BPM_less_50_more_40,
        NotifyType.BPM_more_100,
        NotifyType.AD_more_140_160,
        NotifyType.AD_more_160_180,
        NotifyType.AD_more_180,
        NotifyType.AD_trouble,
        NotifyType.AD_again_less_140_90,
        NotifyType.AD_again_more_140_90,
        NotifyType.Tracker_reminder,
        NotifyType.Pressure_OK,
        NotifyType.Critical_Warning})
@Retention(RetentionPolicy.SOURCE)
public @interface NotifyType {
    int PC_Period_5_min = 0;
    int PC_Period_15_min = PC_Period_5_min + 1;
    int PC_Period_30_min = PC_Period_15_min + 1;
    int PC_Period_1_hour = PC_Period_30_min + 1;
    int PC_Period_12_hours = PC_Period_1_hour + 1;
    int PC_Period_24_hours = PC_Period_12_hours + 1;
    int PC_more_limit_per_hour = PC_Period_24_hours + 1;        //
    int PC_more_limit_per_day = PC_more_limit_per_hour + 1;
    int PC_2 = PC_more_limit_per_day + 1;                       //
    int PC_3 = PC_2 + 1;                                        //
    int PC_detect = PC_3 + 1;
    // PULSE (BPM) events
    int BPM_less_40 = 100;
    int BPM_less_50_more_40 = BPM_less_40 + 1;
    int BPM_more_100 = BPM_less_50_more_40 + 1;
    // AD events
    int AD_more_140_160 = 1000;
    int AD_more_160_180 = AD_more_140_160 + 1;
    int AD_more_180 = AD_more_160_180 + 1;
    int AD_trouble = AD_more_180 + 1;
    int AD_again_less_140_90 = AD_trouble + 1;
    int AD_again_more_140_90 = AD_again_less_140_90 + 1;
    int Tracker_reminder = AD_again_more_140_90 + 1;
    int Pressure_OK = Tracker_reminder + 1;
    int Critical_Warning = Pressure_OK + 1;
}
