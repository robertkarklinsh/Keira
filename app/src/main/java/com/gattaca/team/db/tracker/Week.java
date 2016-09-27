package com.gattaca.team.db.tracker;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Artem on 12.09.2016.
 */

@RealmClass
public class Week implements RealmModel {
    private RealmList<Day> days = new RealmList<>();
    public static final int DAYS_IN_WEEK = 7;
    @PrimaryKey
    int weekNumber;

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public static String getNamedFieldWeekNum() {
        return "weekNumber";
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(RealmList<Day> days) {
        this.days = days;
    }

    public void addDay(Day day) {
        days.add(day);
    }

    public static Week stub() {
        String[] days = {"ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС"};
        ;
        Week week = new Week();
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            Day day = Day.example();
            day.setName(days[i]);
            week.addDay(day);
        }
        return week;
    }
}
