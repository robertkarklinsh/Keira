package com.gattaca.bitalinoecgchart.tracker.data;

import com.gattaca.bitalinoecgchart.tracker.v2.ModelDao;

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

        public Day(boolean isFuture, int percent, String name) {
            this.isFuture = isFuture;
            this.percent = percent;
            this.name = name;
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
    }
    public static TopContainer example() {
        String[] day = new String[] {"пн","вт","ср","чт","пт","сб","вс"};
        int[] percents = new int[] {100,100,50,0,0,0,0};
        List<Day> list = new ArrayList<>();
        for(int i = 0 ; i < 7; i ++ ) {
            list.add(new Day(ModelDao.currentDayOfWeek() < i ,percents[i],day[i]));
        }
        return new TopContainer(list, 4);
    }

}
