package com.gattaca.team.ui.activity.tracker;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.gattaca.team.db.tracker.Drug;
import com.gattaca.team.db.tracker.Intake;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.ui.container.ActivityTransferData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class DrugInfoActivity extends AppCompatActivity {


    Realm realm;
    private List<Intake> deletedIntakes = new ArrayList<>();
    private List<Intake> addedIntakes = new ArrayList<>();


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
                    timeHolder.removeView(linearLayout);
                    deletedIntakes.add(intake);
                    times.remove(DrugInfoActivity.TimeHolder.this);

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


    List<DrugInfoActivity.TimeHolder> times = new ArrayList<>();
    LinearLayout timeHolder;
    CheckBox everyDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long drugId = (long) ActivityTransferData.getBindData(getIntent());
        Drug drug = Realm.getDefaultInstance().where(Drug.class).equalTo("primaryKey", drugId).findFirst();
        realm = RealmController.getRealm();
        times = new ArrayList<>();
        setContentView(R.layout.activity_drug_info);
        Button okButton = (Button) findViewById(R.id.add_drug_ok_button);
        EditText name = (EditText) findViewById(R.id.add_drug_name_field);
        EditText dose = (EditText) findViewById(R.id.add_drug_dose_field);
        EditText units = (EditText) findViewById(R.id.add_drug_units_field);
        LinearLayout timeHolder = (LinearLayout) findViewById(R.id.add_drug_container);
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
                for(Intake intake : addedIntakes) {
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


    }

    static boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }

    static String edtToStr(EditText editText) {
        return editText.getText().toString();
    }


}
