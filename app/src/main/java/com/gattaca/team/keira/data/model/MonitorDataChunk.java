package com.gattaca.team.keira.data.model;

import io.realm.RealmObject;

/**
 * Created by Robert on 16.08.2016.
 */
public class MonitorDataChunk extends RealmObject {

    private int offset;
    private int dimensions;
    private int frequency;
    private byte[] rawData;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getDimensions() {
        return dimensions;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }
}
