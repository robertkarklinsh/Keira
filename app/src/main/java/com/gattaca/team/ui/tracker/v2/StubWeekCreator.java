package com.gattaca.team.ui.tracker.v2;

import com.gattaca.team.db.tracker.Day;
import com.gattaca.team.db.tracker.Drug;
import com.gattaca.team.db.tracker.Intake;
import com.gattaca.team.db.tracker.PressureMeasurement;
import com.gattaca.team.db.tracker.PulseMeasurement;
import com.gattaca.team.db.tracker.Task;
import com.gattaca.team.db.tracker.TaskAction;
import com.gattaca.team.db.tracker.Week;
import com.gattaca.team.root.AppUtils;

import java.util.Calendar;

import io.realm.Realm;

/**
 * Created by Artem on 24.09.2016.
 */

public class StubWeekCreator {

    Week week;
    Realm realm;

    public StubWeekCreator(Week week, Realm realm) {
        this.week = week;
        this.realm = realm;
    }

    public void fillStubWeek() {
        String[] days = new String[]{"ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС"};
        for (int i = 0; i < 7; i++) {
            week.addDay(createDay(days[i], i));
        }

    }

    private Intake createIntake(Boolean status, int hours, int minutes) {
        try {
            Intake intake = realm.createObject(Intake.class);

            intake.setHours(hours);
            intake.setMinutes(minutes);
            intake.setTaken(status);
            //TODO REDO!!!!
            intake.setCreationDate(ModelDao.getTimeInMillis() + hours);
            intake.setPrimaryKey(AppUtils.generateUniqueId());
            return intake;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private TaskAction createTaskAction(Boolean status) {
        TaskAction taskAction = realm.createObject(TaskAction.class);
        taskAction.setCompleted(status);
        taskAction.setPrimaryKey(AppUtils.generateUniqueId());
        return taskAction;
    }

    private Drug createDrug(String name, int dose, String units) {
        Drug drug = realm.createObject(Drug.class);
        drug.setName(name);
        drug.setDose(dose);
        drug.setUnits(units);
        drug.setCreationDate(ModelDao.getTimeInMillis());
        drug.setPrimaryKey(AppUtils.generateUniqueId());
        for (int i = 0; i < 4; i++) {
            drug.getIntakes().add(createIntake(i < 2, 12 + i * 2, 0));
        }
        return drug;
    }

    private Task createTask(String name, int time, String units) {
        Task task = realm.createObject(Task.class);
        task.setName(name);
        task.setUnits(units);
        task.setTime(time);
        task.setPrimaryKey(AppUtils.generateUniqueId());
        for (int i = 0; i < 2; i++) {
            task.getActions().add(createTaskAction(i % 2 == 0));
        }
        return task;
    }

    private PulseMeasurement createPulseMeasurement(String name, int duration, String units) {
        PulseMeasurement pulseMeasurement = realm.createObject(PulseMeasurement.class);
        pulseMeasurement.setName(name);
        pulseMeasurement.setDuration(duration);
        pulseMeasurement.setUnits(units);
        pulseMeasurement.setPrimaryKey(AppUtils.generateUniqueId());
        return pulseMeasurement;
    }

    private PressureMeasurement createPressureMeasurement(String name, int pulse, int sys, int dyas, int hours, int minutes, boolean completed) {
        PressureMeasurement pressureMeasurement = realm.createObject(PressureMeasurement.class);
        pressureMeasurement.setName(name);
        pressureMeasurement.setDiastolic(dyas);
        pressureMeasurement.setSystolic(sys);
        pressureMeasurement.setPulse(pulse);
        pressureMeasurement.setCompleted(completed);
        pressureMeasurement.setHours(hours);
        pressureMeasurement.setMinutes(minutes);
        return pressureMeasurement;
    }

    private Day createDay(String name, int number) {
        try {
            Day day = realm.createObject(Day.class);
            day.setName(name);
            day.setNumber(number);
            day.setYearDayOfTheYear(Calendar.getInstance().get(Calendar.YEAR) * 1000 + Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
            for (int i = 0; i < 3; i++) {
                day.getDrugs().add(createDrug("Вазилип " + i, 20, "мг"));
                day.getTasks().add(createTask("Лежать", 24, "часа"));
                day.getPulseMeasurements().add(createPulseMeasurement("Пульс", 15, "мин"));
                day.getPressureMeasurements().add(createPressureMeasurement("Давление",90,100,100,15,20,false));
            }
            return day;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
