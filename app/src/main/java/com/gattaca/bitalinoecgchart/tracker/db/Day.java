package com.gattaca.bitalinoecgchart.tracker.db;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Artem on 12.09.2016.
 */
public class Day extends RealmObject {
    RealmList<Measurement> measurements = new RealmList<>();
    RealmList<Drug> drugs = new RealmList<>();
    RealmList<Task> tasks = new RealmList<>();

    public String getName() {
        return name;
    }

    public int getPercent() {
        int count = 0;
        int completed = 0;
        for (Drug drug : drugs) {
            for (Intake intake : drug.getIntakes()) {
                count ++;
                if (intake.isTaken()) {
                    completed ++;
                }
            }
        }
        for (Task task : tasks) {
            for (TaskAction taskAction: task.getActions()) {
                count++;
                if (taskAction.isCompleted()) {
                    completed ++;
                }
            }
        }
        return (int)Math.round((double)completed/(double)count);
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
    int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(RealmList<Measurement> measurements) {
        this.measurements = measurements;
    }

    public List<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(RealmList<Drug> drugs) {
        this.drugs = drugs;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(RealmList<Task> tasks) {
        this.tasks = tasks;
    }

    public static Day example() {
        Day day = new Day();
        for (int i = 0; i < 3; i ++) {
            day.measurements.add(new Measurement());
            day.tasks.add(new Task());
            day.drugs.add(new Drug());
        }
        return day;
    }

}
