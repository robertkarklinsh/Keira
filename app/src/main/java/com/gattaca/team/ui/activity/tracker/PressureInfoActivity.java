package com.gattaca.team.ui.activity.tracker;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.tracker.Drug;
import com.gattaca.team.db.tracker.Intake;
import com.gattaca.team.db.tracker.PressureMeasurement;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.service.tracker.TimeNotification;
import com.gattaca.team.ui.utils.ActivityTransferData;
import com.gattaca.team.ui.tracker.v2.ModelDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.R.attr.name;
import static com.gattaca.team.db.RealmController.getRealm;
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
            checkPressure(Integer.parseInt(edtToStr(systolicField)), Integer.parseInt(edtToStr(dyastolicField)));
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

    private static void checkPressure(int systolic, int dyastolic) {
        if (systolic >= 140 && dyastolic > 90) {
            if (systolic <= 160) {
                //AddDrug
            } else if (systolic <= 180) {
                //Add 2drug
            } else {

            }

        }

        Realm realm = RealmController.getRealm();
        realm.beginTransaction();
        PressureMeasurement pm = realm.createObject(PressureMeasurement.class);
        pm.setHours(ModelDao.getHours());
        pm.setMinutes(ModelDao.getMinutes() + 15);
        pm.setCompleted(false);
        pm.setName("Давление");
        pm.setCreatedFromWarning(true);
        pm.setPrimaryKey(AppUtils.generateUniqueId());
        realm.commitTransaction();

        Drug drug = realm.createObject(Drug.class, AppUtils.generateUniqueId());
        drug.setName("bla");
        drug.setUnits("mg");
        drug.setDose(5);
        drug.setCreationDate(ModelDao.getTimeInMillis());
//        for (AddDrugActivity.TimeHolder holder : times) {
            Intake intake = realm.createObject(Intake.class);
            intake.setTaken(false);
            intake.setHours(ModelDao.getHours());
            intake.setMinutes(ModelDao.getMinutes() + 15);
            intake.setCreationDate(ModelDao.getTimeInMillis());
            intake.setPrimaryKey(AppUtils.generateUniqueId());
            drug.getIntakes().add(intake);
            GregorianCalendar gr = new GregorianCalendar();
            gr.set(Calendar.HOUR_OF_DAY, intake.getHours());
            gr.set(Calendar.MINUTE, intake.getMinutes());
            gr.set(Calendar.SECOND,0);
//            gr.add(Calendar.DAY_OF_MONTH, day.getNumber() - ModelDao.currentDayOfWeek());
            TimeNotification.setAlarm(getApplicationContext(), gr.getTimeInMillis(), "intake", intake.getPrimaryKey());
        }
//        day.getDrugs().add(drug);
    }

}
