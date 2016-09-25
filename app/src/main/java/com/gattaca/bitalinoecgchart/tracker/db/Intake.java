package com.gattaca.bitalinoecgchart.tracker.db;

import java.util.Locale;

import io.realm.RealmObject;

/**
 * Created by Artem on 24.09.2016.
 */

public class Intake extends RealmObject {
    private boolean taken;
    private int hours;
    private int minutes;

    public boolean isTaken() {
        return taken;
    }
    public String getTimeAsString () {
//            return String.format(Locale.ROOT, "%02d:%02d", time.get(Calendar.HOUR_OF_DAY),time.get(Calendar.MINUTE));
        return String.format(Locale.ROOT, "%02d:%02d", hours,minutes);
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public int getHours(){
        return hours;
    }

    public int getMinutes() {

        return minutes;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}