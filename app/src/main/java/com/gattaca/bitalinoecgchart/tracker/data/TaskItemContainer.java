package com.gattaca.bitalinoecgchart.tracker.data;

import com.gattaca.team.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem on 28.08.2016.
 */
public class TaskItemContainer extends TrackerItemContainer {

    public TaskItemContainer(String blackText, String grayText, int icon) {
        super(blackText, grayText, icon);
    }

    @Override
    public ItemType getType() {
        return ItemType.TASK;
    }

    List<Boolean> tasks = new ArrayList<>();

    public void addTask(Boolean task)
    {
        tasks.add(task);
    }

    public List<Boolean> getTasks() {
        return tasks;
    }

    public static TaskItemContainer example(int pos){
        TaskItemContainer  taskItemContainer = new TaskItemContainer("Ходьба", "30 мин" , R.drawable.walking_icon);
        taskItemContainer.addTask(true);
        taskItemContainer.addTask(false);
        return taskItemContainer;
    }
}
