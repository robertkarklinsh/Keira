package com.gattaca.bitalinoecgchart.tracker.data;

import com.gattaca.bitalinoecgchart.tracker.model.Drug;
import com.gattaca.team.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Artem on 28.08.2016.
 */
public class DrugItemContainer extends TrackerItemContainer {

    Drug drug;

    public static DrugItemContainer fromDrug(Drug drug) {
        String name = drug.getName();
        int dose = drug.getDose();
        String units = drug.getUnits();
        DrugItemContainer result = new DrugItemContainer(name, dose + " " + units, R.drawable.pills_icon);
        result.drug = drug;
        for(Drug.Intake intake : drug.getIntakes()) {
            Reception reception = Reception.fromIntake(intake);

        }
        return result;
    }

    public DrugItemContainer(String blackText, String grayText, int icon) {
        super(blackText, grayText, icon);
    }

    @Override
    public ItemType getType() {
        return ItemType.DRUG;
    }

    public void addReception(Reception reception) {
        receptions.add(reception);
    }

    List<Reception> receptions = new ArrayList<>();

    public List<Reception> getReceptions() {
        return receptions;
    }

    public static class Reception {
        boolean status = false;
        Integer hours;
        Integer minutes;
        Drug.Intake intake;
        Long timeL = 0L;

        public static Reception fromIntake(Drug.Intake intake) {
            int hours = intake.getHours();
            int minutes = intake.getMinutes();
            Boolean status = intake.isTaken();
            return new Reception(status, hours, minutes);
        }

        public boolean getStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getTime() {
            return String.format(Locale.ROOT, "%02d:%02d", hours, minutes);

        }

        public Integer getHours() {
            return hours;
        }

        public void setHours(Integer hours) {
            this.hours = hours;
        }

        public Integer getMinutes() {
            return minutes;
        }

        public void setMinutes(Integer minutes) {
            this.minutes = minutes;
        }


        public Reception(boolean status, Integer hours, Integer minutes) {
            this.status = status;
            this.hours = hours;
            this.minutes = minutes;
        }
    }

    public static DrugItemContainer example(int pos) {
        DrugItemContainer cont = new DrugItemContainer("Vicodin" + pos, "2 mg", R.drawable.pills_icon);
        cont.addReception(new Reception(true, 12, 00));
        cont.addReception(new Reception(true, 14, 00));
        cont.addReception(new Reception(false, 22, 00));
        cont.addReception(new Reception(false, 23, 58));

        return cont;
    }
}
