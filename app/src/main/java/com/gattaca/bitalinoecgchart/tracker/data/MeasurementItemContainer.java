package com.gattaca.bitalinoecgchart.tracker.data;

/**
 * Created by Artem on 28.08.2016.
 */
public abstract class MeasurementItemContainer extends TrackerItemContainer {

    public MeasurementItemContainer(String blackText, String grayText, int icon) {
        super( blackText,grayText, icon);
    }

    public abstract Boolean isDiscrete() ;


    @Override
    public int getType() {
        return 2;
    }

}
