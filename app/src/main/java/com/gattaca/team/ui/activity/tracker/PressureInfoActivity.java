package com.gattaca.team.ui.activity.tracker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gattaca.team.R;
import com.gattaca.team.annotation.ModuleName;
import com.gattaca.team.annotation.NotifyType;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.event.NotifyEventObject;
import com.gattaca.team.db.tracker.Day;
import com.gattaca.team.db.tracker.Drug;
import com.gattaca.team.db.tracker.Intake;
import com.gattaca.team.db.tracker.PressureMeasurement;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.service.tracker.TimeNotification;
import com.gattaca.team.ui.activity.MainActivity;
import com.gattaca.team.ui.tracker.v2.ModelDao;
import com.gattaca.team.ui.utils.ActivityTransferData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.gattaca.team.db.RealmController.getRealm;
import static com.gattaca.team.service.tracker.TimeNotification.notificationId;
import static com.gattaca.team.ui.activity.tracker.AddPressureActivity.edtToStr;
import static com.gattaca.team.ui.activity.tracker.AddPressureActivity.isEmpty;

public class PressureInfoActivity extends AppCompatActivity {

    boolean isReminders = false;
    int minutes;
    int hours;
    PressureMeasurement pm;
    Realm realm;

    List<View> justAdd = new ArrayList<>();
    List<View> reminded = new ArrayList<>();
    List<PressureInfoActivity.ReminderHolder> reminderHolders = new ArrayList<>();
    LinearLayout remindersHolder;

    private class ReminderHolder {
        LinearLayout ll;
        TextView date;
        Button button;
        long id;

        public ReminderHolder(LinearLayout ll, TextView date, Button button, long id) {
            this.ll = ll;
            this.date = date;
            this.button = button;
            this.id = id;
            button.setOnClickListener((View v) -> {
                if (id == pm.getPrimaryKey()) {
                    Toast.makeText(PressureInfoActivity.this, "Что бы удалить данное измерение, воспользуйтесь конопку удалить измерение", Toast.LENGTH_SHORT).show();
                    return;
                }
                reminderHolders.remove(ReminderHolder.this);
                TimeNotification.removeAlarm(PressureInfoActivity.this, "pressure", id);
                realm.executeTransaction(r -> r.where(PressureMeasurement.class).equalTo("primaryKey", id).findAll().deleteAllFromRealm());
                remindersHolder.removeView(ll);
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure_info);
        long pmId = (long) ActivityTransferData.getBindData(getIntent());
        pm = Realm.getDefaultInstance().where(PressureMeasurement.class).equalTo("primaryKey", pmId).findFirst();
        realm = Realm.getDefaultInstance();
        minutes = pm.getMinutes();
        hours = pm.getHours();
        Button cancel = (Button) findViewById(R.id.pressure_info_cancel);
        Button ok = (Button) findViewById(R.id.pressure_info_ok);
        TextView time = (TextView) findViewById(R.id.pressure_info_time);
        EditText timeField = (EditText) findViewById(R.id.pressure_info_time_field);
        timeField.setText(String.format(Locale.ROOT, "%02d:%02d", hours, minutes));
        timeField.setOnClickListener((View v) -> {
                    // Get Current Time
                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(PressureInfoActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    timeField.setText(hourOfDay + ":" + minute);
                                    hours = hourOfDay;
                                    minutes = minute;
                                }
                            }, hours, minutes, true);
                    timePickerDialog.show();
                }
        );
        TextView systolic = (TextView) findViewById(R.id.pressure_info_systolic);
        EditText systolicField = (EditText) findViewById(R.id.pressure_info_systolic_field);
        systolicField.setText(String.valueOf(pm.getSystolic()));
        TextView dyastolic = (TextView) findViewById(R.id.pressure_info_dyastolic);
        EditText dyastolicField = (EditText) findViewById(R.id.pressure_info_dyastolic_field);
        dyastolicField.setText(String.valueOf(pm.getDiastolic()));
        ImageView stateButton = (ImageView) findViewById(R.id.pressure_info_state);
        TextView pulse = (TextView) findViewById(R.id.pressure_info_pulse);
        EditText pulseField = (EditText) findViewById(R.id.pressure_info_pulse_field);
        pulseField.setText(String.valueOf(pm.getPulse()));
        Button delete = (Button) findViewById(R.id.pressure_info_delete);
        delete.setOnClickListener(v -> {
            TimeNotification.removeAlarm(PressureInfoActivity.this, "pressure", pm.getPrimaryKey());
            Realm.getDefaultInstance().executeTransaction(r -> {

                r.where(PressureMeasurement.class).equalTo("primaryKey", pm.getPrimaryKey()).findAll().deleteAllFromRealm();
            });
            PressureInfoActivity.this.finish();
        });
        remindersHolder = (LinearLayout) findViewById(R.id.pressure_info_reminders);


        justAdd.add(cancel);
        justAdd.add(ok);
        justAdd.add(timeField);
        justAdd.add(systolic);
        justAdd.add(dyastolic);
        justAdd.add(systolicField);
        justAdd.add(time);
        justAdd.add(dyastolicField);
        justAdd.add(pulse);
        justAdd.add(pulseField);
        justAdd.add(delete);
        reminded.add(remindersHolder);


        cancel.setOnClickListener((View v) -> PressureInfoActivity.this.finish());

        stateButton.setOnClickListener((View v) -> {
            isReminders = !isReminders;
            ((ImageView) v).setImageResource(isReminders ? R.drawable.fucking_alarm : R.drawable.fucking_perncil);
            if (isReminders) {
                setInvisible(justAdd);
                setVisible(reminded);
            } else {
                setVisible(justAdd);
                setInvisible(reminded);
            }

        });


        ok.setOnClickListener(v -> {
            if (isEmpty(edtToStr(pulseField))) {
                Toast.makeText(PressureInfoActivity.this, "Пожалуйста введите пульс", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isEmpty(edtToStr(dyastolicField)) || isEmpty(edtToStr(systolicField))) {
                Toast.makeText(PressureInfoActivity.this, "Пожалуйста введите давление", Toast.LENGTH_SHORT).show();
                return;
            }
            Realm realm = getRealm();
            realm.beginTransaction();
            pm.setHours(hours);
            pm.setMinutes(minutes);
            pm.setDiastolic(Integer.parseInt(edtToStr(dyastolicField)));
            pm.setSystolic(Integer.parseInt(edtToStr(systolicField)));
            pm.setPulse(Integer.parseInt(edtToStr(pulseField)));
            pm.setCompleted(true);
            realm.commitTransaction();
            checkPressure(Integer.parseInt(edtToStr(systolicField)), Integer.parseInt(edtToStr(dyastolicField)), pm.getCreatedFromWarning());
            PressureInfoActivity.this.finish();
        });


        RealmResults<PressureMeasurement> remindersResults = realm.where(PressureMeasurement.class).findAll();
        for (PressureMeasurement hmm : remindersResults) {
            Long date = TimeNotification.getTimeStamp("pressure", hmm.getPrimaryKey());
            if (date > ModelDao.getTimeInMillis()) {
                LinearLayout reminderLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drug_info_notification, null);
                TextView dateView = (TextView) reminderLayout.findViewById(R.id.drug_info_reminder_date);
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTimeInMillis(date);
                dateView.setText(String.format(Locale.ROOT, "%02d/%02d/%02d %02d:%02d",
                        gc.get(Calendar.DAY_OF_MONTH),
                        gc.get(Calendar.MONTH),
                        gc.get(Calendar.YEAR),
                        gc.get(Calendar.HOUR_OF_DAY),
                        gc.get(Calendar.MINUTE)));
                Button reminderDeleteBtn = (Button) reminderLayout.findViewById(R.id.drug_info_reminder_button);
                reminderHolders.add(new PressureInfoActivity.ReminderHolder(reminderLayout, dateView, reminderDeleteBtn, hmm.getPrimaryKey()));
                remindersHolder.addView(reminderLayout);

            }
        }

    }

    private void setInvisible(List<View> list) {
        for (View v : list) {
            v.setVisibility(View.GONE);
        }
    }

    private void setVisible(List<View> list) {
        for (View v : list) {
            v.setVisibility(View.VISIBLE);
        }
    }

    public static void checkPressure(int systolic, int dyastolic, boolean fromWarning) {
        if (systolic >= 140 || dyastolic >= 90) {
            //NOT OK
            if (fromWarning) {
                NotifyEventObject neo = new NotifyEventObject()
                        .setModuleNameResId(ModuleName.Tracker)
                        .setTime(ModelDao.getTimeInMillis())
                        .setEventType(NotifyType.Critical_Warning)
                        .setPrimaryKey(AppUtils.generateUniqueId())
                        .realData();
                RealmController.getRealm().executeTransaction((Realm r) -> {
                    r.copyToRealmOrUpdate(neo);
                });
                //TODO REDO this is just
                {

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainApplication.getContext());
                    mBuilder.setSmallIcon(R.drawable.fucking_perncil);
                    String notification = "Обратитесь к врачу!";
                    mBuilder.setContentTitle(notification);
                    Intent intentTL = new Intent(MainApplication.getContext(), MainActivity.class);

                    mBuilder.setContentIntent(PendingIntent.getActivity(MainApplication.getContext(),
                            0,
                            intentTL,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT))
                            .setAutoCancel(true);


                    int mNotificationId = notificationId;
                    notificationId++;
// Gets an instance of the NotificationManager service
                    NotificationManager mNotifyMgr =
                            (NotificationManager) MainApplication.getContext().getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
                }
                //OK
            }
            if (systolic <= 160) {
                createDrug("Копатен", "мг", 5);
            } else if (systolic <= 180) {
                createDrug("Коринфар", "мг", 5);
            } else {
                createDrug("Коринфар", "мг", 5);
                createPressureMsrmnt();
            }

        } else if (fromWarning) {
            NotifyEventObject neo = new NotifyEventObject()
                    .setModuleNameResId(ModuleName.Tracker)
                    .setTime(ModelDao.getTimeInMillis())
                    .setEventType(NotifyType.Pressure_OK)
                    .setPrimaryKey(AppUtils.generateUniqueId())
                    .realData();
            RealmController.getRealm().executeTransaction((Realm r) -> {
                r.copyToRealmOrUpdate(neo);
            });
            //OK
        }


//        day.getDrugs().add(drug);
    }

    private static void createPressureMsrmnt() {
        Realm realm = RealmController.getRealm();
        for (Day day : RealmController.getCurrentWeek().getDays()) {
            if (day.getNumber() == ModelDao.currentDayOfWeek()) {
                realm.beginTransaction();
                PressureMeasurement pm = realm.createObject(PressureMeasurement.class);
                int hours = ModelDao.getHours();
                int minutes = ModelDao.getMinutes() + 15;
                if (minutes >= 60) {
                    minutes -= 60;
                    hours ++;
                    if (hours >= 24) {
                        hours --;
                    }
                }
                pm.setHours(hours);
                pm.setMinutes(minutes);
                pm.setCompleted(false);
                pm.setName("Давление");
                pm.setCreatedFromWarning(true);
                pm.setPrimaryKey(AppUtils.generateUniqueId());
                day.getPressureMeasurements().add(pm);
                GregorianCalendar gr = new GregorianCalendar();
                gr.set(Calendar.HOUR_OF_DAY, ModelDao.getHours());
                gr.set(Calendar.MINUTE, ModelDao.getMinutes());
                gr.add(Calendar.MINUTE, 15);
                gr.set(Calendar.SECOND, 0);
                TimeNotification.setAlarm(MainApplication.getContext(), gr.getTimeInMillis(), "pressure", pm.getPrimaryKey());
                realm.commitTransaction();
            }
        }
    }

    private static void createDrug(String name, String units, int dose) {
        for (Day day : RealmController.getCurrentWeek().getDays()) {
            if (day.getNumber() == ModelDao.currentDayOfWeek()) {
                Realm realm = RealmController.getRealm();
                realm.beginTransaction();
                Drug drug = realm.createObject(Drug.class, AppUtils.generateUniqueId());
                drug.setName(name);
                drug.setUnits(units);
                drug.setDose(dose);
                drug.setCreationDate(ModelDao.getTimeInMillis());

                Intake intake = realm.createObject(Intake.class);
                intake.setTaken(false);
                int hours = ModelDao.getHours();
                int minutes = ModelDao.getMinutes() + 1;
                if (minutes >= 60) {
                    minutes -= 60;
                    hours ++;
                    if (hours >= 24) {
                        hours --;
                    }
                }
                intake.setHours(hours);
                intake.setMinutes(minutes);
                intake.setCreationDate(ModelDao.getTimeInMillis());
                intake.setPrimaryKey(AppUtils.generateUniqueId());
                drug.getIntakes().add(intake);
                GregorianCalendar gr = new GregorianCalendar();
                gr.set(Calendar.HOUR_OF_DAY, intake.getHours());
                gr.set(Calendar.MINUTE, intake.getMinutes());
                gr.add(Calendar.MINUTE, 1);
                gr.set(Calendar.SECOND, 0);

                TimeNotification.setAlarm(MainApplication.getContext(), gr.getTimeInMillis(), "intake", intake.getPrimaryKey());
                day.getDrugs().add(drug);
                realm.commitTransaction();
            }
        }
    }

}
