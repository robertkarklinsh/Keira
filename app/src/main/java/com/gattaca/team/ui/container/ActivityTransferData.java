package com.gattaca.team.ui.container;

import android.app.Activity;
import android.content.Intent;

import com.gattaca.team.ui.activity.tracker.AddDrugActivity;
import com.gattaca.team.ui.activity.tracker.AddPressureActivity;
import com.gattaca.team.ui.activity.MonitorBpm;
import com.gattaca.team.ui.activity.MonitorEcg;
import com.gattaca.team.ui.activity.tracker.DrugInfoActivity;
import com.gattaca.team.ui.activity.tracker.PressureInfoActivity;

public final class ActivityTransferData {
    private final static String key = "ActivityTransferData.key";
    private final static String type = "ActivityTransferData.type";
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

    public static Object getBindData(final Intent i) {
        Object a = null;
        if (i.hasExtra(type)) {
            final String typeObject = i.getStringExtra(type);
            //TODO: extends cases here!
            if (typeObject.equals(Long.class.getSimpleName())) {
                a = i.getLongExtra(key, -1);
            }
        }
        return a;
    }

    public void launchRequestedActivity(final Activity activity) {
        final Intent i = new Intent(activity, launchActivity.getCls());
        //TODO: extends cases here!
        if (bindData instanceof Long) {
            i.putExtra(type, bindData.getClass().getSimpleName());
            i.putExtra(key, (long) bindData);
        }
        activity.startActivity(i);
    }

    public enum AvailableActivity {
        ECG(MonitorEcg.class),
        BPM(MonitorBpm.class),
        ADT(AddDrugActivity.class),
        APT(AddPressureActivity.class),
        DRUG_INFO(DrugInfoActivity.class),
        PM_INFO(PressureInfoActivity.class);

        final private Class cls;

        AvailableActivity(Class cls) {
            this.cls = cls;
        }

        public Class getCls() {
            return cls;
        }
    }
}
