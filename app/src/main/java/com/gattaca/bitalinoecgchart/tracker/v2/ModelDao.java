package com.gattaca.bitalinoecgchart.tracker.v2;

import com.gattaca.bitalinoecgchart.tracker.model.Day;
import com.gattaca.bitalinoecgchart.tracker.model.Drug;
import com.gattaca.bitalinoecgchart.tracker.model.Measurement;
import com.gattaca.bitalinoecgchart.tracker.model.Task;
import com.gattaca.bitalinoecgchart.tracker.model.Week;
import com.gattaca.bitalinoecgchart.tracker.ui.DrugItem;
import com.gattaca.bitalinoecgchart.tracker.ui.HeaderItem;
import com.gattaca.bitalinoecgchart.tracker.ui.TaskItem;
import com.gattaca.bitalinoecgchart.tracker.ui.TrackerItem;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Artem on 18.09.2016.
 */
public class ModelDao {
    private static final String TODAY = "сегодня";
    private static final String YESTERDAY = "вчера";

    public ModelDao() {
    }

    public ModelDao(Week week) {
        this.week = week;
    }

    Week week = Week.stub();
    static GregorianCalendar time = new GregorianCalendar();

    public int getCount() {
        int res = 0;
        int currentDay = currentDayOfWeek();
        for (int i = currentDay; i >= 0; i--) {
            Day day = week.getDays().get(i);
            res++;
            res += day.getDrugs().size();
            res += day.getMeasurements().size();
            res += day.getTasks().size();
        }
        return res;
    }

    /**
     * 0 - Monday, 6- Sunday
     *
     * @return day
     */
    public static int currentDayOfWeek() {
        int currentDay = time.get(Calendar.DAY_OF_WEEK);
        currentDay = currentDay - 2;
        if (currentDay < 0) {
            currentDay = 6;
        }
        return currentDay;
    }


    private String dayModifier(Day day, int currentDay) {
        if (day.getNumber() == currentDay) {
            return TODAY;
        }
        if (day.getNumber() - 1 == currentDay) {
            return YESTERDAY;
        }
        return day.getName();
    }

    public int getFirstItemOfDay(int dayNumber) {
        if (dayNumber > currentDayOfWeek()) {
            return 0;
        }
        int res = 1;
        for (int i = currentDayOfWeek(); i > dayNumber; i--) {
            Day day = week.getDays().get(i);
            res++;
            res += day.getDrugs().size();
            res += day.getMeasurements().size();
            res += day.getTasks().size();
        }
        return res;
    }


    public List<AbstractItem> getAllItemList() {
        List<AbstractItem> res = new ArrayList<>();
        int currentDay = currentDayOfWeek();
        for (int i = currentDay; i >= 0; i--) {
            Day day = week.getDays().get(i);
            res.add(new HeaderItem().withName(dayModifier(day, currentDay)));
            for (Drug drug : day.getDrugs()) {
                res.add(new DrugItem().withItemContainer(drug));
            }
            for (Measurement measurement : day.getMeasurements()) {
                res.add(new TrackerItem().withItemContainer(measurement));
            }
            for (Task task : day.getTasks()) {
                res.add(new TaskItem().withItemContainer(task));
            }
        }
        return res;
    }

    public AbstractItem getItem(int position) {
        int count = getCount();
        if (position > count) {
            return null;
        }
        return getAllItemList().get(position);
    }



}
