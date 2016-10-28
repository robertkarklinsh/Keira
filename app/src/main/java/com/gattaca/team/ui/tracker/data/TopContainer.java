package com.gattaca.team.ui.tracker.data;

import com.gattaca.team.db.tracker.Drug;
import com.gattaca.team.db.tracker.PulseMeasurement;
import com.gattaca.team.db.tracker.Task;
import com.gattaca.team.db.tracker.TaskAction;
import com.gattaca.team.db.tracker.Week;
import com.gattaca.team.ui.tracker.v2.ModelDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem on 17.09.2016.
 */
public class TopContainer {
    List<Day> days;

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    int selected;

    public TopContainer(List<Day> days, int selected) {
        this.days = days;
        this.selected = selected;
    }

    public static class Day {
        boolean isFuture;
        int percent;
        String name;
        com.gattaca.team.db.tracker.Day day;

        public Day(boolean isFuture, int percent, String name,com.gattaca.team.db.tracker.Day day) {
            this.isFuture = isFuture;
            this.percent = percent;
            this.name = name;
            this.day = day;
        }

        public boolean isFuture() {
            return isFuture;
        }

        public void setFuture(boolean future) {
            isFuture = future;
        }

        public int getPercent() {
            return percent;
        }

        public void setPercent(int percent) {
            this.percent = percent;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getDrugCount() {
            return day.getDrugs().size();
        }
        public int getCompletedDrugs() {
            int res = 0;
            for (Drug drug : day.getDrugs()) {
                if (drug.isCompleted()) {
                    res ++;
                }
            }
            return res;
        }

        public int getTasksCount() {
            return day.getTasks().size();
        }
        public int getCompletedTasks() {
            int res = 0;
            for (Task task : day.getTasks()) {
                boolean completed = true;
                for (TaskAction ta : task.getActions()){
                    completed = completed & ta.isCompleted();
                }
                if (completed) {
                    res++;
                }
            }
            return res;
        }

        public int getMeasurementCount() {
            return day.getPulseMeasurements().size();
        }
        public int getCompletedMeasurements() {
            int res = 0;
            for (PulseMeasurement pm : day.getPulseMeasurements()) {
                if (pm.isCompleted()) {
                    res ++;
                }
            }
            return res;
        }
        public int getNum() {
            return day.getNumber();
        }
    }
    public static TopContainer example() {
        String[] day = new String[] {"пн","вт","ср","чт","пт","сб","вс"};
        int[] percents = new int[] {100,0,50,67,30,40,0};
        List<Day> list = new ArrayList<>();
        for(int i = 0 ; i < 7; i ++ ) {
            list.add(new Day(ModelDao.currentDayOfWeek() < i ,percents[i],day[i], null));
        }
        return new TopContainer(list, 4);
    }

    public static TopContainer createFromWeek(Week week) {
        List<Day> list = new ArrayList<>();
        for(int i = 0 ; i < 7; i ++ ) {
            com.gattaca.team.db.tracker.Day day = week.getDays().get(i);
            list.add(new Day(ModelDao.currentDayOfWeek() < i ,day.getPercent(),day.getName().toLowerCase(),day));
        }
        return new TopContainer(list, ModelDao.currentDayOfWeek());
    }

}
