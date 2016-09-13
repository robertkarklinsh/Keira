package com.gattaca.team.service;

import com.bitalino.comm.BITalinoFrame;

import java.util.ArrayList;

public final class SensorData {
    private final ArrayList<ItemData> list = new ArrayList<>();
    private final long timeStumpStart, timeStumpEnd;

    public SensorData(final long timeStumpStart, final long timeStumpEnd) {
        this.timeStumpStart = timeStumpStart;
        this.timeStumpEnd = timeStumpEnd;
    }

    public void addFrame(final BITalinoFrame frame) {
        ItemData item = new ItemData();
        for (int i = 0; i < getChannels(); i++) {
            item.setVolts(i, frame.getAnalog(i));
        }
        list.add(item);
    }

    public double getAvrVolt(int position) throws NoSuchMethodException {
        throw new NoSuchMethodException();
    }

    public double getVoltByChannel(int position, int channel) {
        return list.get(position).getVoltByChannel(channel);
    }

    public long getTimeStump(final int position) {
        return timeStumpStart + (timeStumpEnd - timeStumpStart) * position;
    }

    public int getChannels() {
        return ItemData.CHANNELS_BITRATE.length;
    }

    public int countTicks() {
        return list.size();
    }

    static class ItemData {
        static final int CHANNELS_BITRATE[] = {10, 10, 10, 10, 6, 6};
        private static final double VCC = 3.3;
        private static final double GECG = 1100;
        final double[] volts = new double[CHANNELS_BITRATE.length];

        public double getVoltByChannel(int channel) {
            return volts[channel];
        }

        public void setVolts(final int channnel, final int values) {
            volts[channnel] = convert(values, CHANNELS_BITRATE[channnel]);
        }

        private double convert(int analog, int bitRate) {
            return 1000 * ((double) analog / (1 << bitRate) - 0.5) * VCC / GECG;
        }
    }

}
