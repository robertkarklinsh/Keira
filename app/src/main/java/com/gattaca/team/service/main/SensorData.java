package com.gattaca.team.service.main;

public final class SensorData {
    private final long timeStumpStart, timeStumpStep;
    private final FormattedSensorItem[][] data;
    private int frameIndex = 0;

    public SensorData(final int maxChannels, final int frames, final long timeStumpStart, final long timeStumpEnd) {
        this.timeStumpStart = timeStumpStart;
        this.data = new FormattedSensorItem[frames][maxChannels];
        this.timeStumpStep = (timeStumpEnd - timeStumpStart) / frames;
    }

    public void next(int... row) {
        if (row.length <= this.data.length) {
            int channel = 0;
            for (int value : row) {
                setValue(value, channel++);
            }
        }
        nextCursor();
    }

    public int getMaxChannelCount() {
        return this.data[0].length;
    }

    public FormattedSensorItem getItem(int channel) {
        return data[frameIndex][channel];
    }

    public void setValue(double value, int channel) {
        data[frameIndex][channel] = new FormattedSensorItem(value, timeStumpStart + timeStumpStep * frameIndex);
    }

    public boolean nextCursor() {
        return ++frameIndex < data.length;
    }

    public void resetCursor() {
        frameIndex = 0;
    }


    public static class FormattedSensorItem {
        private final long time;
        private double value;

        FormattedSensorItem(double value, long time) {
            this.setNewValue(value);
            this.time = time;
        }

        public void setNewValue(double value) {
            this.value = value;
        }

        public double getValue() {
            return this.value;
        }

        public long getTime() {
            return this.time;
        }
    }
}
