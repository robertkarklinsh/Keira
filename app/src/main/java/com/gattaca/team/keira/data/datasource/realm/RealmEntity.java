package com.gattaca.team.keira.data.datasource.realm;

import io.realm.RealmObject;

/**
 * Created by Robert on 10.08.2016.
 */
public class RealmEntity extends RealmObject {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String str) {
        text = str;
    }
}
