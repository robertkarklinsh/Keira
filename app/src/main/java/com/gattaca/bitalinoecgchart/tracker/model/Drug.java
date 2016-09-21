package com.gattaca.bitalinoecgchart.tracker.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Artem on 12.09.2016.
 */
public class Drug implements Completable {
    private boolean completed = false;
    private String name;
    private int dose;
    //TODO maybe we must unify doses Enum etc;
    private String units;
    private int count;
    private List<Intake> intakes;

    @Override
    public boolean isCompleted() {
        return completed;
    }

    public static class Intake {
        private boolean taken;
        private GregorianCalendar time;

        public boolean isTaken() {
            return taken;
        }
        public String getTimeAsString () {
            return time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE);
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
