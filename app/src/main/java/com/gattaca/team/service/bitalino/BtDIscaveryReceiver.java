package com.gattaca.team.service.bitalino;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class BtDiscaveryReceiver extends BroadcastReceiver {
    private static BtDiscaveryReceiver instance;

    public static BtDiscaveryReceiver getInstance() {
        if (instance == null) {
            instance = new BtDiscaveryReceiver();
        }
        return instance;
    }

    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        switch (action) {
            case BluetoothDevice.ACTION_FOUND:
                BitalinoConnection.bus.post(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
                break;
        }
    }
}