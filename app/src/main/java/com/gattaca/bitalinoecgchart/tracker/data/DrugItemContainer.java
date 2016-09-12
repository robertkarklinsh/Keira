package com.gattaca.bitalinoecgchart.tracker.data;

import com.gattaca.bitalinoecgchart.R;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Artem on 28.08.2016.
 */
public class DrugItemContainer extends TrackerItemContainer {

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
        String time = "00:00";
        Long timeL = 0L;

        public boolean getStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Reception(boolean status, String time) {
            this.status = status;
            this.time = time;
        }
    }

    public static DrugItemContainer example(int pos) {
        DrugItemContainer cont = new DrugItemContainer("Vicodin" + pos, "2 mg", R.drawable.pills_icon);
        cont.addReception(new Reception(true, "12:00"));
        cont.addReception(new Reception(true, "14:00"));
        cont.addReception(new Reception(true, "15:00"));
        return cont;
    }
}
