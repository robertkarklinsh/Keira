package com.gattaca.bitalinoecgchart.tracker.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by Artem on 12.09.2016.
 */
public class Drug extends RealmObject {
    private boolean completed = false;
    private String name;
    private int dose;
    //TODO maybe we must unify doses Enum etc;
    private String units;
    private int count;
    private List<Intake> intakes;

    public boolean isCompleted() {
        return completed;
    }

    public static class Intake extends RealmObject {
        private boolean taken;
        private GregorianCalendar time;


        public boolean isTaken() {
            return taken;
        }
        public String getTimeAsString () {
            return time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE);
        }

        public int getHours(){
            return time.get((Calendar.HOUR_OF_DAY));
        }

        public int getMinutes() {
//            time.getTime()
            return time.get(Calendar.MINUTE);
        }

    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public List<Intake> getIntakes() {
        return intakes;
    }

    public int getDose() {
        return dose;
    }

    public String getUnits() {
        return units;
    }
}
