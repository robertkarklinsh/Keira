package com.gattaca.team.ui.container;

import android.app.Activity;
import android.content.Intent;

import com.gattaca.team.ui.activity.AddDrugActivity;
import com.gattaca.team.ui.activity.MonitorBpm;
import com.gattaca.team.ui.activity.MonitorEcg;

public final class ActivityTransferData {
    final private AvailableActivity launchActivity;
    //TODO: change to sub activity model
    final private Object bindData;

    public ActivityTransferData(AvailableActivity launchActivity) {
        this(launchActivity, null);
    }

    public ActivityTransferData(AvailableActivity launchActivity, Object bindData) {
        this.launchActivity = launchActivity;
        this.bindData = bindData;
    }

    public void launchRequestedActivity(final Activity activity) {
        activity.startActivity(new Intent(activity, launchActivity.getCls()));
    }

    public enum AvailableActivity {
        ECG(MonitorEcg.class),
        BPM(MonitorBpm.class),
        ADT(AddDrugActivity.class);

        final private Class cls;

        AvailableActivity(Class cls) {
            this.cls = cls;
        }

        public Class getCls() {
            return cls;
        }
    }
}
