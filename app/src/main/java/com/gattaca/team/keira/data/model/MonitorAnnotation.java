package com.gattaca.team.keira.data.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Robert on 16.08.2016.
 */
public class MonitorAnnotation extends RealmObject {

    private int source;
    private Date dateTime;
    private String text;

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
