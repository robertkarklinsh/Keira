package com.gattaca.team.keira.ui.model;

/**
 * Created by Robert on 28.08.2016.
 */
public class Category {

    String name;
    int itemCount;

    public Category(String name, int itemCount) {
        this.name = name;
        this.itemCount = itemCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

}
