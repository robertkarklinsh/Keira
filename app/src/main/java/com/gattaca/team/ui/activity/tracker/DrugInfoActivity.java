package com.gattaca.team.ui.activity.tracker;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.service.tracker.TimeNotification;
import com.gattaca.team.ui.container.ActivityTransferData;
import com.gattaca.team.ui.tracker.v2.ModelDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.gattaca.team.service.tracker.TimeNotification.getTimeStamp;

public class DrugInfoActivity extends AppCompatActivity {


    Realm realm;
    private List<Intake> deletedIntakes = new ArrayList<>();
    private List<Intake> addedIntakes = new ArrayList<>();
    LinearLayout remindersHolder;
    boolean reminders = false;


    private class TimeHolder {
        LinearLayout linearLayout;
        Button button;
        TextView editText;
        Intake intake;
        int hours = 12;
        int minutes = 00;

        public TimeHolder(LinearLayout linearLayout, Intake intake) {
            this.linearLayout = linearLayout;
            this.button = (Button) linearLayout.findViewById(R.id.remove_drug_button);
            this.editText = (TextView) linearLayout.findViewById(R.id.add_drug_time_field);
            this.intake = intake;

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (times.size() == 1) {
                        Toast.makeText(DrugInfoActivity.this, "У лекарства не может быть меньше 1 приема", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (getTimeStamp("intake", intake.getPrimaryKey()) < ModelDao.getTimeInMillis()) {
                        timeHolder.removeView(linearLayout);
                        deletedIntakes.add(intake);
                        times.remove(DrugInfoActivity.TimeHolder.this);
                    } else {
                        realm.executeTransaction((Realm r) -> intake.setTaken(false));
                        timeHolder.removeView(linearLayout);
                    }

                }
            });
            editText.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                TimePickerDialog timePickerDialog = new TimePickerDialog(DrugInfoActivity.this,
                                                        new TimePickerDialog.OnTimeSetListener() {
                                                            @Override
                                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                                  int minute) {
                                                                hours = hourOfDay;
                                                                minutes = minute;
                                                                editText.setText(hourOfDay + ":" + minute);
                                                            }
                                                        }, hours, minutes, true);
                                                timePickerDialog.show();
                                            }
                                        }

            );
        }
    }

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
                reminderHolders.remove(ReminderHolder.this);
                TimeNotification.removeAlarm(DrugInfoActivity.this, "intake", id);
                remindersHolder.removeView(ll);
            });

        }


    }

    List<DrugInfoActivity.TimeHolder> times = new ArrayList<>();
    List<DrugInfoActivity.ReminderHolder> reminderHolders = new ArrayList<>();
    LinearLayout timeHolder;
    CheckBox everyDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long drugId = (long) ActivityTransferData.getBindData(getIntent());
        setContentView(R.layout.activity_drug_info);
        Drug drug = Realm.getDefaultInstance().where(Drug.class).equalTo("primaryKey", drugId).findFirst();
        realm = RealmController.getRealm();
        times = new ArrayList<>();
        Button okButton = (Button) findViewById(R.id.add_drug_ok_button);
        Button deleteButton = (Button) findViewById(R.id.drug_info_delete_button);
        EditText name = (EditText) findViewById(R.id.add_drug_name_field);
        EditText dose = (EditText) findViewById(R.id.add_drug_dose_field);
        EditText units = (EditText) findViewById(R.id.add_drug_units_field);
        TextView time = (TextView) findViewById(R.id.add_drug_time);
        LinearLayout timeHolder = (LinearLayout) findViewById(R.id.add_drug_container);
        remindersHolder = (LinearLayout) findViewById(R.id.drug_info_reminders);
        RealmResults<Drug> remindersResults = realm.where(Drug.class)
                .equalTo("name", drug.getName())
                .equalTo("dose", drug.getDose())
                .equalTo("units", drug.getUnits())
                .findAll();
        for (Drug hmm : remindersResults) {
            for (Intake intake : hmm.getIntakes()) {
                Long date = TimeNotification.getTimeStamp("intake", intake.getPrimaryKey());
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
                    reminderHolders.add(new ReminderHolder(reminderLayout, dateView, reminderDeleteBtn, intake.getPrimaryKey()));
                    remindersHolder.addView(reminderLayout);
                }
            }
        }


        this.timeHolder = timeHolder;
        if (drug != null) {
            name.setText(drug.getName());
            dose.setText(String.valueOf(drug.getDose()));
            units.setText(drug.getUnits());
            for (Intake intake : drug.getIntakes()) {
                if (intake.isTaken()) {
                    LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.add_drug_add_time_item, null);
                    timeHolder.addView(ll);
                    TimeHolder th = new DrugInfoActivity.TimeHolder(ll, intake);
                    th.hours = intake.getHours();
                    th.minutes = intake.getMinutes();
                    th.editText.setText(String.format(Locale.ROOT, "%02d:%02d", th.hours, th.minutes));
                    times.add(th);
                }
            }
        }


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(edtToStr(name))) {
                    Toast.makeText(DrugInfoActivity.this, "Пожалуйста введите название", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmpty(edtToStr(dose)) || isEmpty(edtToStr(units))) {
                    Toast.makeText(DrugInfoActivity.this, "Пожалуйста введите дозу и единицы измерения", Toast.LENGTH_SHORT).show();
                    return;
                }
                realm.beginTransaction();
                drug.setName(edtToStr(name));
                drug.setDose(Integer.parseInt(edtToStr(dose)));
                drug.setUnits(edtToStr(units));
                for (Intake intake : addedIntakes) {
                    drug.getIntakes().add(intake);
                }
                for (Intake intake : deletedIntakes) {
                    RealmResults<Intake> res = realm.where(Intake.class).equalTo("primaryKey", intake.getPrimaryKey()).findAll();
                    res.deleteAllFromRealm();
                }

                for (TimeHolder th : times) {
                    th.intake.setMinutes(th.minutes);
                    th.intake.setHours(th.hours);
                    realm.copyToRealmOrUpdate(th.intake);
                }
                realm.commitTransaction();
                DrugInfoActivity.this.finish();
            }
        });

        Button addDrugBtn = (Button) findViewById(R.id.add_drug_add_time);
        addDrugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(DrugInfoActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.add_drug_add_time_item, null);
                                timeHolder.addView(ll);
                                Intake intake = new Intake();
                                intake.setTaken(true);
                                intake.setCreationDate(AppUtils.generateUniqueId());
                                intake.setPrimaryKey(AppUtils.generateUniqueId());
                                addedIntakes.add(intake);
                                TimeHolder th = new DrugInfoActivity.TimeHolder(ll, intake);
                                th.hours = hourOfDay;
                                th.minutes = minute;
                                th.editText.setText(hourOfDay + ":" + minute);
                                times.add(th);
                            }
                        }, AppUtils.getCurrentHour(), AppUtils.getCurrentMinute(), true);
                timePickerDialog.show();
            }
        });

        Button cancelButton = (Button) findViewById((R.id.add_drug_cancel_button));
        cancelButton.setOnClickListener((View v) -> {
            DrugInfoActivity.this.finish();
        });

        ImageView state = (ImageView) findViewById(R.id.add_drug_state);
        state.setOnClickListener((View v) -> {
            if (!reminders) {
                okButton.setVisibility(View.GONE);
                addDrugBtn.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                timeHolder.setVisibility(View.GONE);
                time.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                remindersHolder.setVisibility(View.VISIBLE);
                state.setImageResource(R.drawable.fucking_perncil);
                reminders = !reminders;
            } else {
                okButton.setVisibility(View.VISIBLE);
                addDrugBtn.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                timeHolder.setVisibility(View.VISIBLE);
                time.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                remindersHolder.setVisibility(View.GONE);
                state.setImageResource(R.drawable.fucking_alarm);
                reminders = !reminders;
            }
        });

        deleteButton.setOnClickListener(v -> {
            for (Intake intake : drug.getIntakes()) {
                TimeNotification.removeAlarm(this,"intake", intake.getPrimaryKey());
            }
//            realm = Realm.getDefaultInstance();
            realm.executeTransaction(r -> {
                for (Intake intake : drug.getIntakes()) {
                    realm.where(Intake.class).equalTo("primaryKey", intake.getPrimaryKey()).findAll().deleteAllFromRealm();
                }
                realm.where(Drug.class).equalTo("primaryKey", drug.getPrimaryKey()).findAll().deleteAllFromRealm();
            });
            DrugInfoActivity.this.finish();
        });
    }

    static boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }

    static String edtToStr(EditText editText) {
        return editText.getText().toString();
    }


}
