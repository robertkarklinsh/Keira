package com.gattaca.bitalinoecgchart.tracker.data;

import com.gattaca.bitalinoecgchart.R;

/**
 * Created by Artem on 03.09.2016.
 */
public class ProgressBarItemContainer extends MeasurementItemContainer {
    public ProgressBarItemContainer(String blackText, String grayText, int icon) {
        super(blackText, grayText, icon);
    }
    String progressBarTopText;
    String progressBarBottomText;

    public String getProgressBarTopText() {
        return progressBarTopText;
    }

    public void setProgressBarTopText(String progressBarTopText) {
        this.progressBarTopText = progressBarTopText;
    }

    public String getProgressBarBottomText() {
        return progressBarBottomText;
    }

    public void setProgressBarBottomText(String progressBarBottomText) {
        this.progressBarBottomText = progressBarBottomText;
    }

    @Override
    public Boolean isDiscrete() {
        return null;
    }

    public static ProgressBarItemContainer example(int pos) {
        ProgressBarItemContainer cont = new ProgressBarItemContainer("Vicodin" + pos, "2 mg", R.drawable.cardiogram_icon);
        cont.setProgressBarTopText("Обнаружено 1 отклонение");
        cont.setProgressBarBottomText("в 2873 ударов");
        return cont;
    }

}
