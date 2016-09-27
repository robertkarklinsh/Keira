package com.gattaca.bitalinoecgchart.tracker.data;

/**
 * Created by Artem on 12.09.2016.
 */
public enum ViewType {
    HEADER(0), //0
    ITEM(2), // 2
    FOOTER(1), //1
    TOP(3);
    public final int number;

    ViewType(int number) {
        this.number = number;
    }
}
