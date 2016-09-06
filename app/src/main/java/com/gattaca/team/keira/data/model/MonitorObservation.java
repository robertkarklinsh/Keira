package com.gattaca.team.keira.data.model;

import static com.gattaca.team.keira.utils.ObservationInfo.ObservationType;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Robert on 14.08.2016.
 */
public class MonitorObservation extends RealmObject {

    @PrimaryKey
    private long id;

    private Date startDateTime;
    private Date endDateTime;

    private int observationType;

   // private List<MonitorDataChunk> dataChunks;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    @ObservationType
    public int getObservationType() {
        return observationType;
    }

    public void setObservationType(@ObservationType  int observationType) {
        this.observationType = observationType;
    }

/*    public List<MonitorDataChunk> getDataChunks() {
        return dataChunks;
    }

    public void setDataChunks(List<MonitorDataChunk> dataChunks) {
        this.dataChunks = dataChunks;
    }*/
}
