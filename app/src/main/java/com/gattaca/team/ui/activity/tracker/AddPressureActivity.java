package com.gattaca.team.ui.activity.tracker;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gattaca.team.R;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.tracker.Day;
import com.gattaca.team.db.tracker.PressureMeasurement;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.service.tracker.TimeNotification;
import com.gattaca.team.ui.tracker.v2.ModelDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

public class AddPressureActivity extends AppCompatActivity {


    private static GregorianCalendar calendar = new GregorianCalendar();
    private Holder holder;

    Boolean isReminder = false;

    class Holder {

        ImageView stateButton;


        List<View> reminded = new ArrayList<>();
        List<View> justAdd = new ArrayList<>();
        int hours;
        int minutes;

        public Holder(EditText timeField,
                      TextView systolic,
                      EditText systolicField,
                      TextView dyastolic,
                      EditText dyastloicField,
                      TextView pulse,
                      EditText pulseField,
                      ImageView stateButton,
                      CheckBox remindEveryDay) {

            this.stateButton = stateButton;

            justAdd.add(systolic);
            justAdd.add(systolicField);
            justAdd.add(dyastolic);
            justAdd.add(dyastloicField);
            justAdd.add(pulse);
            justAdd.add(pulseField);
            reminded.add(remindEveryDay);


            hours = calendar.get(Calendar.HOUR);
            minutes = calendar.get(Calendar.MINUTE);

            timeField.setText(String.format(Locale.ROOT, "%02d:%02d", hours, minutes));
            timeField.setOnClickListener((View v) -> {
                        // Get Current Time
                        final Calendar c = Calendar.getInstance();
                        hours = c.get(Calendar.HOUR_OF_DAY);
                        minutes = c.get(Calendar.MINUTE);
                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AddPressureActivity.this,
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

            stateButton.setOnClickListener((View v) -> {
                isReminder = !isReminder;
                ((ImageView) v).setImageResource(isReminder ? R.drawable.fucking_alarm : R.drawable.fucking_perncil);
                if (isReminder) {
                    setInvisible(justAdd);
                    setVisible(reminded);
                } else {
                    setVisible(justAdd);
                    setInvisible(reminded);
                }

            });

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

        public int getHours() {
            return hours;
        }

        public int getMinutes() {
            return minutes;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pressure);

        Button cancel = (Button) findViewById(R.id.add_pressure_cancel);
        Button ok = (Button) findViewById(R.id.add_pressure_ok);
        EditText timeField = (EditText) findViewById(R.id.add_pressure_time_field);

        TextView systolic = (TextView) findViewById(R.id.add_pressure_systolic);
        EditText systolicField = (EditText) findViewById(R.id.add_pressure_systolic_field);
        TextView dyastolic = (TextView) findViewById(R.id.add_pressure_dyastolic);
        EditText dyastolicField = (EditText) findViewById(R.id.add_pressure_dyastolic_field);
        ImageView stateButton = (ImageView) findViewById(R.id.add_pressure_state);
        CheckBox everyDayReminder = (CheckBox) findViewById(R.id.add_pressure_every_day_reminder);
        TextView pulse = (TextView) findViewById(R.id.add_pressure_pulse);
        EditText pulseField = (EditText) findViewById(R.id.add_pressure_pulse_field);
        holder = new Holder(timeField, systolic, systolicField, dyastolic, dyastolicField, pulse, pulseField, stateButton, everyDayReminder);
        cancel.setOnClickListener((View v) -> AddPressureActivity.this.finish());

        ok.setOnClickListener((View v) -> {
            if (!isReminder && isEmpty(edtToStr(pulseField))) {
                Toast.makeText(AddPressureActivity.this, "Пожалуйста введите пульс", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isReminder &&( isEmpty(edtToStr(dyastolicField)) || isEmpty(edtToStr(systolicField)))) {
                Toast.makeText(AddPressureActivity.this, "Пожалуйста введите давление", Toast.LENGTH_SHORT).show();
                return;
            }
            Realm realm = RealmController.getRealm();
            realm.beginTransaction();
            if (isReminder) {
                for (Day day : RealmController.getCurrentWeek().getDays()) {
                    if (day.getNumber() == ModelDao.currentDayOfWeek() ||
                            (everyDayReminder.isChecked() && day.getNumber() > ModelDao.currentDayOfWeek())) {
                        PressureMeasurement pm = realm.createObject(PressureMeasurement.class);
                        pm.setHours(holder.getHours());
                        pm.setMinutes(holder.getMinutes());
                        pm.setCompleted(false);
                        pm.setName("Давление");
                        pm.setPrimaryKey(AppUtils.generateUniqueId());
                        pm.setCreatedFromWarning(false);
                        day.getPressureMeasurements().add(pm);
                        GregorianCalendar gr = new GregorianCalendar();
                        gr.set(Calendar.HOUR_OF_DAY, pm.getHours());
                        gr.set(Calendar.MINUTE, pm.getMinutes());
                        gr.set(Calendar.SECOND,0);
                        gr.add(Calendar.DAY_OF_MONTH, day.getNumber() - ModelDao.currentDayOfWeek());
                        TimeNotification.setAlarm(getApplicationContext(), gr.getTimeInMillis(), "pressure", pm.getPrimaryKey());
                    }
                }

            } else {
                for(Day day : RealmController.getCurrentWeek().getDays()) {
                    if (day.getNumber() == ModelDao.currentDayOfWeek()) {

                        PressureMeasurement pm = realm.createObject(PressureMeasurement.class);
                        pm.setHours(holder.getHours());
                        pm.setMinutes(holder.getMinutes());
                        pm.setCompleted(true);
                        pm.setName("Давление");
                        pm.setCreatedFromWarning(false);
                        pm.setSystolic(Integer.parseInt(edtToStr(systolicField)));
                        pm.setDiastolic(Integer.parseInt(edtToStr(dyastolicField)));
                        pm.setPulse(Integer.parseInt(edtToStr(pulseField)));
                        pm.setPrimaryKey(AppUtils.generateUniqueId());
                        day.getPressureMeasurements().add(pm);
                    }

                }
            }
            realm.commitTransaction();
            if (!isReminder) {
                PressureInfoActivity.checkPressure(Integer.parseInt(edtToStr(systolicField)), Integer.parseInt(edtToStr(dyastolicField)), false);
            }
            AddPressureActivity.this.finish();
        });


    }

    static boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }
    static String edtToStr(EditText editText) {
        return editText.getText().toString();
    }
}
