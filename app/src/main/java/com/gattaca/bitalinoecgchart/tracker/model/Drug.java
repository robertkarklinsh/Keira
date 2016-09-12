package com.gattaca.bitalinoecgchart.tracker.model;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Artem on 12.09.2016.
 */
public class Drug implements Completable {
    private boolean completed = false;
    private String name;
    private int count;
    private List<Intake> intakes;

    @Override
    public boolean isCompleted() {
        return completed;
    }

    private static class Intake {
        private boolean taken;
        static GregorianCalendar time;
    }
}
