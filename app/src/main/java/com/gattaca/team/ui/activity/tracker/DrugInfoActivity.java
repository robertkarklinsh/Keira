package com.gattaca.team.ui.activity.tracker;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gattaca.team.R;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.tracker.Day;
import com.gattaca.team.db.tracker.Drug;
import com.gattaca.team.db.tracker.Intake;
import com.gattaca.team.ui.tracker.v2.ModelDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

public class DrugInfoActivity extends AppCompatActivity {

    private class TimeHolder {
        LinearLayout linearLayout;
        Button button;
        TextView editText;
        int hours = 12;
        int minutes = 00;

        public TimeHolder(LinearLayout linearLayout) {
            this.linearLayout = linearLayout;
            this.button = (Button) linearLayout.findViewById(R.id.remove_drug_button);
            this.editText = (TextView) linearLayout.findViewById(R.id.add_drug_time_field);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (times.size() == 1) {
                        Toast.makeText(DrugInfoActivity.this, "У лекарства не может быть меньше 1 приема", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    timeHolder.removeView(linearLayout);
                    times.remove(DrugInfoActivity.TimeHolder.this);

                }
            });
            editText.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Get Current Time
                                                final Calendar c = Calendar.getInstance();
                                                hours = c.get(Calendar.HOUR_OF_DAY);
                                                minutes = c.get(Calendar.MINUTE);
                                                TimePickerDialog timePickerDialog = new TimePickerDialog(DrugInfoActivity.this,
                                                        new TimePickerDialog.OnTimeSetListener() {
                                                            @Override
                                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                                  int minute) {

                                                                editText.setText(hourOfDay + ":" + minute);
                                                            }
                                                        }, hours, minutes, true);
                                                timePickerDialog.show();
                                            }
                                        }

            );
        }
    }


    List<DrugInfoActivity.TimeHolder> times = new ArrayList<>();
    LinearLayout timeHolder;
    CheckBox everyDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        times = new ArrayList<>();
        setContentView(R.layout.activity_drug_info);
        Button okButton = (Button) findViewById(R.id.add_drug_ok_button);
        EditText name = (EditText) findViewById(R.id.add_drug_name_field);
        EditText dose = (EditText) findViewById(R.id.add_drug_dose_field);
        EditText units = (EditText) findViewById(R.id.add_drug_units_field);
        LinearLayout timeHolder = (LinearLayout) findViewById(R.id.add_drug_container);
        this.timeHolder = timeHolder;
        LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.add_drug_add_time_item, null);
        timeHolder.addView(ll);
        times.add(new DrugInfoActivity.TimeHolder(ll));


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
                Realm realm = RealmController.getRealm();
                realm.beginTransaction();
//                for (Day day : RealmController.getCurrentWeek().getDays()) {
//                    if (day.getNumber() == ModelDao.currentDayOfWeek() ||
//                            (checkBox.isChecked() && day.getNumber() > ModelDao.currentDayOfWeek())) {
//                        Drug drug = realm.createObject(Drug.class);
//                        drug.setName(name.getText().toString());
//                        drug.setUnits(units.getText().toString());
//                        drug.setDose(Integer.parseInt(dose.getText().toString()));
//                        drug.setCreationDate(ModelDao.getTimeInMillis() + drug.getDose());
//                        for (DrugInfoActivity.TimeHolder holder : times) {
//                            Intake intake = realm.createObject(Intake.class);
//                            intake.setTaken(false);
//                            intake.setHours(holder.hours);
//                            intake.setMinutes(holder.minutes);
//                            intake.setCreationDate(ModelDao.getTimeInMillis() + holder.hours);
//                            drug.getIntakes().add(intake);
//                        }
//                        day.getDrugs().add(drug);
//                    }
//                }
                realm.commitTransaction();
                DrugInfoActivity.this.finish();
            }
        });

        Button addDrugBtn = (Button) findViewById(R.id.add_drug_add_time);
        addDrugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.add_drug_add_time_item, null);
                timeHolder.addView(ll);
                times.add(new DrugInfoActivity.TimeHolder(ll));
            }
        });

        Button cancelButton = (Button) findViewById((R.id.add_drug_cancel_button));
        cancelButton.setOnClickListener((View v) -> DrugInfoActivity.this.finish());


    }

    static boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }

    static String edtToStr(EditText editText) {
        return editText.getText().toString();
    }
}
