package com.gattaca.team.service;

public interface IServiceConnection {
    int[] getChannelsBitRate();

    void startConnection();

    void fakeGeneration();

    void stopConnection();
}
