package com.gattaca.bitalinoecgchart.tracker.v2;

import com.gattaca.bitalinoecgchart.tracker.db.Day;
import com.gattaca.bitalinoecgchart.tracker.db.Drug;
import com.gattaca.bitalinoecgchart.tracker.db.Intake;
import com.gattaca.bitalinoecgchart.tracker.db.Measurement;
import com.gattaca.bitalinoecgchart.tracker.db.Task;
import com.gattaca.bitalinoecgchart.tracker.db.TaskAction;
import com.gattaca.bitalinoecgchart.tracker.db.Week;

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
        String[] days = new String[] {"ПН","ВТ","СР","ЧТ","ПТ","СБ","ВС"};
        for (int i = 0 ; i < 7 ; i ++) {
            week.addDay(createDay(days[i], i));
        }

    }

    private Intake createIntake(Boolean status, int hours, int minutes) {
        Intake intake = realm.createObject(Intake.class);

        intake.setHours(hours);
        intake.setMinutes(minutes);
        intake.setTaken(status);
        return intake;
    }

    private TaskAction createTaskAction(Boolean status) {
        TaskAction taskAction = realm.createObject(TaskAction.class);
        taskAction.setCompleted(status);
        return taskAction;
    }

    private Drug createDrug(String name, int dose, String units) {
        Drug drug = realm.createObject(Drug.class);
        drug.setName(name);
        drug.setDose(dose);
        drug.setUnits(units);
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
        for (int i = 0; i < 2; i++) {
            task.getActions().add(createTaskAction(i % 2 == 0));
        }
        return task;
    }

    private Measurement createMeasurment(String name, int duration, String units) {
        Measurement measurement = realm.createObject(Measurement.class);
        measurement.setName(name);
        measurement.setDuration(duration);
        measurement.setUnits(units);
        return measurement;
    }

    private Day createDay(String name, int number) {
        Day day = realm.createObject(Day.class);
        day.setName(name);
        day.setNumber(number);
        for (int i = 0; i < 3; i ++) {
            day.getDrugs().add(createDrug("Вазилип " + i, 20 ,"мг" ));
            day.getTasks().add(createTask("Лежать", 24, "часа"));
            day.getMeasurements().add(createMeasurment("ЭКГ", 15, "мин"));
        }
        return day;
    }


}
