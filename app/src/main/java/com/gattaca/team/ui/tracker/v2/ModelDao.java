package com.gattaca.team.ui.tracker.v2;

import android.support.v7.widget.RecyclerView;

import com.gattaca.team.db.tracker.Day;
import com.gattaca.team.db.tracker.Drug;
import com.gattaca.team.db.tracker.PressureMeasurement;
import com.gattaca.team.db.tracker.Task;
import com.gattaca.team.db.tracker.Week;
import com.gattaca.team.ui.tracker.ui.DrugItem;
import com.gattaca.team.ui.tracker.ui.HeaderItem;
import com.gattaca.team.ui.tracker.ui.PressureMeasurementItem;
import com.gattaca.team.ui.tracker.ui.TaskItem;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Artem on 18.09.2016.
 */
public class ModelDao {
    private static final String TODAY = "СЕГОДНЯ";
    private static final String YESTERDAY = "ВЧЕРА";

    public ModelDao() {
    }

    public ModelDao(Week week) {
        this.week = week;
    }

    public void setWeek(Week week) {
        this.week = week;
    }

    Week week ;
    static GregorianCalendar time = new GregorianCalendar();
    RecyclerView.Adapter adapter;

    public int getCount() {
        int res = 0;
        int currentDay = currentDayOfWeek();
        for (int i = currentDay; i >= 0; i--) {
            Day day = week.getDays().get(i);
            res++;
            res += day.getDrugs().size();
            res += day.getPulseMeasurements().size();
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

    public static long getTimeInMillis() {
        return time.getTimeInMillis();
    }

    public static int getHours(){
        return time.get(Calendar.HOUR_OF_DAY);
    }
    public static int getMinutes(){
        return time.get(Calendar.MINUTE);
    }

    private String dayModifier(Day day, int currentDay) {
        if (day.getNumber() == currentDay) {
            return TODAY;
        }
        if (day.getNumber() == currentDay - 1) {
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
//            res += day.getPulseMeasurements().size();
            res += day.getTasks().size();
            res += day.getPressureMeasurements().size();
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
//            for (PulseMeasurement pulseMeasurement : day.getPulseMeasurements()) {
//                res.add(new PulseMeasurementItem().withItemContainer(pulseMeasurement));
//            }
            for (Task task : day.getTasks()) {
                res.add(new TaskItem().withItemContainer(task));
            }
            for(PressureMeasurement pressureMeasurement: day.getPressureMeasurements()) {
                res.add(new PressureMeasurementItem().withItemContainer(pressureMeasurement));
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
